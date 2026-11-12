# Guia de Deploy - Control API (VPS Integrator Host)

## Infraestrutura

| Componente | Container | Rede | Porta |
|-----------|-----------|------|-------|
| API (Spring Boot) | `tomcat` (icontainer/tomcat:11.0.2-jdk22) | `app-https_app-network` | 8080 |
| Proxy Reverso (HTTPS) | `nginx-proxy` (nginx:alpine) | `app-https_app-network` | 8443 → 443 |
| Banco de Dados | `ic-mysql-QZqy` (mysql:9.3.0) | `icontainer-network` | 3306 |
| Frontend | `nginx-proxy` serve via porta 8181 | — | 8181 |

### URLs

- **Frontend:** `https://entregaja.dmsolucoes.tech`
- **API (HTTPS via Nginx):** `https://tomcat.vps6912.panel.icontainer.net:8443/control-api`
- **API (HTTP direto):** `http://tomcat.vps6912.panel.icontainer.net:8080/control-api`

---

## Arquivos Importantes no Servidor

| Arquivo | Caminho |
|---------|---------|
| nginx.conf | `/root/app-https/nginx.conf` |
| Certificados SSL | `/root/app-https/ssl/` |
| Certbot (Let's Encrypt) | `/root/app-https/certbot/www` |

---

## Deploy da API (WAR)

### 1. Gerar o WAR

> ⚠️ O `pom.xml` tem dois perfis: `local` (Tomcat embutido, padrão) e `war` (Tomcat provided para servidor externo).
> **Sempre usar o perfil `war` para gerar o artefato de deploy.**

```bash
mvn clean package -P war -DskipTests
```

O arquivo será gerado em `target/control-api.war`
(o `<finalName>` no `pom.xml` é `control-api`, não `control-api-0.0.1-SNAPSHOT`).

### 2. Subir o WAR para o servidor

Copiar o `.war` para o diretório de webapps do Tomcat no servidor:

```bash
scp target/control-api.war usuario@vps6912.panel.icontainer.net:/caminho/webapps/
```

### 3. Verificar se subiu
```bash
docker logs tomcat
```
Procurar por:
- `Started ControlApiApplication in X seconds` → sucesso
- `Deployment of web application archive [...] has finished` → WAR deployado

### 4. Testar
```bash
curl http://localhost:8080/control-api/api/v1/auth
```
- **403 Forbidden** = API funcionando (Spring Security bloqueando sem credenciais) ✅
- **404 Not Found** = WAR não foi deployado
- **500** = Erro na aplicação (ver logs)

---

## WebSocket

O backend expõe WebSocket em `/control-api/ws/entregas?token=SEU_TOKEN`.

O token é validado pelo `TokenHandshakeInterceptor` via `EntregadorSessaoRepository`.

### Testar a conexão WebSocket

```bash
curl -k -i \
  -H "Connection: Upgrade" \
  -H "Upgrade: websocket" \
  -H "Sec-WebSocket-Key: CONFIGURE_NO_AMBIENTE" \
  -H "Sec-WebSocket-Version: 13" \
  "https://tomcat.vps6912.panel.icontainer.net:8443/control-api/ws/entregas?token=SEU_TOKEN"
```
- **101 Switching Protocols** = WebSocket funcionando ✅
- **403 Forbidden** = Spring Security bloqueando — verificar se o path está no `permitAll()` do `SpringSecurityConfig`
- **404** = path errado ou WAR não deployado

### nginx.conf — suporte a WebSocket

> ⚠️ O nginx precisa de headers específicos para fazer proxy de WebSocket.
> O `location /` atual usa `proxy_set_header Connection ""` que **quebra o upgrade de WebSocket**.
> É necessário um bloco separado para o path do WebSocket:

```nginx
# Bloco específico para WebSocket (deve vir ANTES do location /)
location /control-api/ws/ {
    proxy_pass http://tomcat;
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "upgrade";
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_read_timeout 3600s;   # manter conexão aberta
    proxy_send_timeout 3600s;
}

# Demais requisições HTTP normais
location / {
    proxy_pass http://tomcat;
    proxy_http_version 1.1;
    proxy_set_header Connection "";
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_connect_timeout 60s;
    proxy_send_timeout 60s;
    proxy_read_timeout 60s;
}
```

Após editar o `nginx.conf`, recarregar:
```bash
docker exec nginx-proxy nginx -t        # validar config
docker restart nginx-proxy              # aplicar
```

---

## Problema Comum: CORS / 502 Bad Gateway

### Sintoma
Frontend retorna erro:
```
Access to XMLHttpRequest blocked by CORS policy
```
Ou o Nginx retorna `502 Bad Gateway`.

### Causa
O container `tomcat` e o `nginx-proxy` estão em redes Docker diferentes, ou o IP do Tomcat mudou.

### Solução (Passo a Passo)

#### 1. Verificar em qual rede o Tomcat está
```bash
docker inspect tomcat --format '{{range $k,$v := .NetworkSettings.Networks}}{{$k}}: {{$v.IPAddress}}{{end}}'
```

#### 2. Se NÃO estiver na rede `app-https_app-network`, conectar
```bash
docker network connect app-https_app-network tomcat
```

#### 3. Verificar o novo IP na rede
```bash
docker inspect tomcat --format '{{range $k,$v := .NetworkSettings.Networks}}{{$k}}: {{$v.IPAddress}}{{end}}'
```
Anotar o IP da rede `app-https_app-network` (ex: `172.20.0.4`).

#### 4. Atualizar o IP no nginx.conf
```bash
sed -i 's/IP_ANTIGO/IP_NOVO/' /root/app-https/nginx.conf
```

#### 5. Verificar a config
```bash
cat /root/app-https/nginx.conf | grep server
```

#### 6. Reiniciar o Nginx
```bash
docker restart nginx-proxy
```

#### 7. Testar
```bash
curl -X OPTIONS https://tomcat.vps6912.panel.icontainer.net:8443/control-api/api/v1/auth \
  -H "Origin: https://entregaja.dmsolucoes.tech" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type, Authorization" -I -k
```
Deve retornar **200** com header `Access-Control-Allow-Origin`.

---

## Dica: Evitar Problema de IP no Futuro

Em vez de usar IP fixo no `nginx.conf`, use o nome do container:

```nginx
upstream tomcat {
    server tomcat:8080;
}
```

Para isso funcionar, ambos os containers precisam estar na mesma rede Docker (`app-https_app-network`).

---

## Estrutura completa do nginx.conf (com WebSocket)

```nginx
events {
    worker_connections 1024;
}

http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;
    sendfile on;
    keepalive_timeout 65;

    upstream tomcat {
        server tomcat:8080;   # usar nome do container (mais estável que IP fixo)
    }

    # HTTP → redireciona pra HTTPS
    server {
        listen 80;
        server_name tomcat.vps6912.panel.icontainer.net;

        location /.well-known/acme-challenge/ {
            root /var/www/certbot;
        }

        location / {
            return 301 https://$host$request_uri;
        }
    }

    # HTTPS
    server {
        listen 443 ssl;
        server_name tomcat.vps6912.panel.icontainer.net;

        ssl_certificate /etc/nginx/ssl/certificate.crt;
        ssl_certificate_key /etc/nginx/ssl/private.key;
        ssl_protocols TLSv1.2 TLSv1.3;
        ssl_ciphers HIGH:!aNULL:!MD5;

        client_max_body_size 50M;

        # WebSocket — deve vir ANTES do location /
        location /control-api/ws/ {
            proxy_pass http://tomcat;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_read_timeout 3600s;
            proxy_send_timeout 3600s;
        }

        # Demais requisições HTTP
        location / {
            proxy_pass http://tomcat;
            proxy_http_version 1.1;
            proxy_set_header Connection "";
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_connect_timeout 60s;
            proxy_send_timeout 60s;
            proxy_read_timeout 60s;
        }
    }
}
```

---

## Comandos Úteis

| Ação | Comando |
|------|---------|
| Ver containers rodando | `docker ps` |
| Ver logs do Tomcat | `docker logs tomcat` |
| Ver logs em tempo real | `docker logs -f tomcat` |
| Reiniciar Tomcat | `docker restart tomcat` |
| Reiniciar Nginx | `docker restart nginx-proxy` |
| Validar nginx.conf | `docker exec nginx-proxy nginx -t` |
| Ver redes Docker | `docker network ls` |
| Ver IP de um container | `docker inspect CONTAINER --format '{{range $k,$v := .NetworkSettings.Networks}}{{$k}}: {{$v.IPAddress}}{{end}}'` |
| Conectar container a rede | `docker network connect REDE CONTAINER` |

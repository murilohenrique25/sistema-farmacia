package br.com.starkstecnologia.control_api.config;

import br.com.starkstecnologia.control_api.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableMethodSecurity
@EnableWebMvc
@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilitar o CORS
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/docs-control.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                        .requestMatchers("/api/app/v1/entregador").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/app/v1/entrega/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/app/v1/entrega/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/app/v1/entrega/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/app/v1/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/app/v1/login/dados").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/entrega-localizacao/**").permitAll()
                        .requestMatchers("/ws/entregas").permitAll()

                        .requestMatchers(
                                antMatcher("/docs-control.html"),
                                antMatcher("/docs-control/**"),
                                antMatcher("/swagger-ui.html"),
                                antMatcher("/swagger-ui/**"),
                                antMatcher("/webjars/**"))
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // Permitir qualquer origem
        config.addAllowedHeader("*"); // Permitir todos os cabeçalhos
        config.addAllowedMethod("*"); // Permitir todos os métodos

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

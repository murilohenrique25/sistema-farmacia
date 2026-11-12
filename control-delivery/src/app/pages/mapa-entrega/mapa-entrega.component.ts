import { Component, AfterViewInit } from '@angular/core';
import * as L from 'leaflet';
import { MapaEntregaService } from '../../services/mapa-entrega.service';
import { EntregaResponse } from '../../interfaces/entrega-response';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MapaEntrega } from '../../interfaces/mapa-entrega';

@Component({
  selector: 'app-mapa-entrega',
  templateUrl: './mapa-entrega.component.html',
  styleUrls: ['./mapa-entrega.component.scss']
})
export class MapaEntregaComponent implements AfterViewInit {

  mostrarModal = true;

  map!: L.Map;
  markersLayer = L.layerGroup();

  dataInicio!: string;
  dataTermino!: string;

  entregas: MapaEntrega[] = [];
  diasComQuantidade: { dia: string; quantidade: number }[] = [];
  diaSelecionado: string | null = null;

  constructor(
    private entregaService: MapaEntregaService,
    private snackbar: MatSnackBar
  ) { }

  /* ================= MAPA ================= */

  ngAfterViewInit(): void {
    this.inicializarMapa();
  }

  inicializarMapa(): void {
    if (this.map) return;

    this.map = L.map('mapa');

    // Base do mapa
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap'
    }).addTo(this.map);

    this.markersLayer.addTo(this.map);

    this.obterLocalizacaoAtual();
  }

  obterLocalizacaoAtual(): void {

    if (!navigator.geolocation) {
      this.definirLocalizacaoPadrao();
      return;
    }

    navigator.geolocation.getCurrentPosition(
      position => {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;

        this.map.setView([lat, lng], 15);

        // Marker opcional: você está aqui
        L.marker([lat, lng])
          .addTo(this.map)
          .bindPopup('📍 Você está aqui')
          .openPopup();
      },
      error => {
        console.warn('Geolocalização negada ou indisponível', error);
        this.definirLocalizacaoPadrao();
      },
      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 0
      }
    );
  }

  definirLocalizacaoPadrao(): void {
    // Exemplo: centro da cidade
    this.map.setView(
      [-18.41150582032579, -49.19573362882008],
      12
    );
  }

  /* ================= BUSCA ================= */

  carregarEntregas(): void {
    if (!this.dataInicio || !this.dataTermino) {
      this.snackbar.open(
        'Informe data início e data término',
        'Fechar',
        { duration: 3000 }
      );
      return;
    }

    const inicio = this.formatDate(new Date(this.dataInicio), 'start');
    const fim = this.formatDate(new Date(this.dataTermino), 'end');

    this.entregaService
      .listarEntregasIntervalo(inicio, fim)
      .subscribe((response: EntregaResponse) => {

        // Todas já são concluídas
        this.entregas = response.entregas || [];

        this.montarDias(response);
        this.exibirEntregasNoMapa();

        this.mostrarModal = false;
      });
  }

  montarDias(response: EntregaResponse): void {
    this.diasComQuantidade = Object.entries(response.quantidadePorData || {})
      .map(([dia, quantidade]) => ({ dia, quantidade }))
      .sort((a, b) => a.dia.localeCompare(b.dia));
  }

  /* ================= MAPA ================= */

  exibirEntregasNoMapa(): void {
    this.markersLayer.clearLayers();

    const entregasFiltradas = this.diaSelecionado
      ? this.entregas.filter(e => e.dataEntrega === this.diaSelecionado)
      : this.entregas;

    if (entregasFiltradas.length === 0) return;

    const bounds = L.latLngBounds([]);

    entregasFiltradas.forEach(entrega => {
      const lat = parseFloat(entrega.latitude);
      const lng = parseFloat(entrega.longitude);

      if (isNaN(lat) || isNaN(lng)) return;

      const marker = L.marker([lat, lng], {
        icon: this.customIcon()
      })
        .addTo(this.markersLayer)
        .bindPopup(`
          <b>Entrega:</b> #${entrega.idEntrega}<br>
          <b>Data:</b> ${entrega.dataEntrega}
        `);

      bounds.extend(marker.getLatLng());
    });

    this.map.fitBounds(bounds, { padding: [50, 50] });
  }

  filtrarPorDia(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.diaSelecionado = value || null;
    this.exibirEntregasNoMapa();
  }

  /* ================= UTIL ================= */

  customIcon(): L.Icon {
    return L.icon({
      iconUrl: 'assets/google-maps.png',
      iconSize: [32, 32],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32]
    });
  }

  formatDate(date: Date, type: 'start' | 'end'): string {
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');

    return type === 'start'
      ? `${y}-${m}-${d}T00:00:00`
      : `${y}-${m}-${d}T23:59:59`;
  }
}

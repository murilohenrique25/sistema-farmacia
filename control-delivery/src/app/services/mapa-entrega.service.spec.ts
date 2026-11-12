import { TestBed } from '@angular/core/testing';

import { MapaEntregaService } from './mapa-entrega.service';

describe('MapaEntregaService', () => {
  let service: MapaEntregaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MapaEntregaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapaEntregaComponent } from './mapa-entrega.component';

describe('MapaEntregaComponent', () => {
  let component: MapaEntregaComponent;
  let fixture: ComponentFixture<MapaEntregaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MapaEntregaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MapaEntregaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

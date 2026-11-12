import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalVisualizarEntregaComponent } from './modal-visualizar-entrega.component';

describe('ModalVisualizarEntregaComponent', () => {
  let component: ModalVisualizarEntregaComponent;
  let fixture: ComponentFixture<ModalVisualizarEntregaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalVisualizarEntregaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalVisualizarEntregaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

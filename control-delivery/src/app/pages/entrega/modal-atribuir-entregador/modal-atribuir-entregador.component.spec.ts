import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalAtribuirEntregadorComponent } from './modal-atribuir-entregador.component';

describe('ModalAtribuirEntregadorComponent', () => {
  let component: ModalAtribuirEntregadorComponent;
  let fixture: ComponentFixture<ModalAtribuirEntregadorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalAtribuirEntregadorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalAtribuirEntregadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

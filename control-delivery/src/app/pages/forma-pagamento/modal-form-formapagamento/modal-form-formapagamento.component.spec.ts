import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalFormFormapagamentoComponent } from './modal-form-formapagamento.component';

describe('ModalFormFormapagamentoComponent', () => {
  let component: ModalFormFormapagamentoComponent;
  let fixture: ComponentFixture<ModalFormFormapagamentoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalFormFormapagamentoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalFormFormapagamentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

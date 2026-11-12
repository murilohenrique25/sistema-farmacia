import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalFormEntregadorComponent } from './modal-form-entregador.component';

describe('ModalFormEntregadorComponent', () => {
  let component: ModalFormEntregadorComponent;
  let fixture: ComponentFixture<ModalFormEntregadorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalFormEntregadorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalFormEntregadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

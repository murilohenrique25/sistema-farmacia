import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalFormEntregaComponent } from './modal-form-entrega.component';

describe('ModalFormEntregaComponent', () => {
  let component: ModalFormEntregaComponent;
  let fixture: ComponentFixture<ModalFormEntregaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalFormEntregaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalFormEntregaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

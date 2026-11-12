import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalAlterarEntregadorComponent } from './modal-alterar-entregador.component';

describe('ModalAlterarEntregadorComponent', () => {
  let component: ModalAlterarEntregadorComponent;
  let fixture: ComponentFixture<ModalAlterarEntregadorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalAlterarEntregadorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalAlterarEntregadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalPesquisaEntregaComponent } from './modal-pesquisa-entrega.component';

describe('ModalPesquisaEntregaComponent', () => {
  let component: ModalPesquisaEntregaComponent;
  let fixture: ComponentFixture<ModalPesquisaEntregaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalPesquisaEntregaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalPesquisaEntregaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

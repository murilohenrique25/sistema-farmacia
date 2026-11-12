import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalViewEntregadorComponent } from './modal-view-entregador.component';

describe('ModalViewEntregadorComponent', () => {
  let component: ModalViewEntregadorComponent;
  let fixture: ComponentFixture<ModalViewEntregadorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalViewEntregadorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalViewEntregadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

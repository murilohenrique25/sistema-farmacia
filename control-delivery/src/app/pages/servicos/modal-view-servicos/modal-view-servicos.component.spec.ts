import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalViewServicosComponent } from './modal-view-servicos.component';

describe('ModalViewServicosComponent', () => {
  let component: ModalViewServicosComponent;
  let fixture: ComponentFixture<ModalViewServicosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalViewServicosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalViewServicosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

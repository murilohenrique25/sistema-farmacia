import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InserirEntregaComponent } from './inserir-entrega.component';

describe('InserirEntregaComponent', () => {
  let component: InserirEntregaComponent;
  let fixture: ComponentFixture<InserirEntregaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InserirEntregaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InserirEntregaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssinarEntregaComponent } from './assinar-entrega.component';

describe('AssinarEntregaComponent', () => {
  let component: AssinarEntregaComponent;
  let fixture: ComponentFixture<AssinarEntregaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AssinarEntregaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AssinarEntregaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntregadorComponent } from './entregador.component';

describe('EntregadorComponent', () => {
  let component: EntregadorComponent;
  let fixture: ComponentFixture<EntregadorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EntregadorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EntregadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

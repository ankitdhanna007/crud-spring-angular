import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SaveCarComponent } from './save-car.component';

describe('CreateCarComponent', () => {
  let component: SaveCarComponent;
  let fixture: ComponentFixture<SaveCarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SaveCarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SaveCarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

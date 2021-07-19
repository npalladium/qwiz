import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventlistenerComponent } from './eventlistener.component';

describe('EventlistenerComponent', () => {
  let component: EventlistenerComponent;
  let fixture: ComponentFixture<EventlistenerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EventlistenerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EventlistenerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

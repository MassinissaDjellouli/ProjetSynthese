import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectUserPageComponent } from './select-user-page.component';

describe('SelectUserPageComponent', () => {
  let component: SelectUserPageComponent;
  let fixture: ComponentFixture<SelectUserPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SelectUserPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectUserPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

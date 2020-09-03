import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizeRecordingsComponent } from './organize-recordings.component';

describe('OrganizeRecordingsComponent', () => {
  let component: OrganizeRecordingsComponent;
  let fixture: ComponentFixture<OrganizeRecordingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OrganizeRecordingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrganizeRecordingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstanceDetailsDialogComponent } from './instance-details-dialog.component';

describe('InstanceDetailsDialogComponent', () => {
  let component: InstanceDetailsDialogComponent;
  let fixture: ComponentFixture<InstanceDetailsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstanceDetailsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstanceDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditMappingsDialogComponent } from './edit-mappings-dialog.component';

describe('EditMappingsDialogComponent', () => {
  let component: EditMappingsDialogComponent;
  let fixture: ComponentFixture<EditMappingsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditMappingsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditMappingsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

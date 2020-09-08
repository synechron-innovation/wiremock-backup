import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Instance } from './../../model/Instance';

@Component({
  selector: 'app-instance-details-dialog',
  templateUrl: './instance-details-dialog.component.html',
  styleUrls: ['./instance-details-dialog.component.scss']
})
export class InstanceDetailsDialogComponent implements OnInit {
  instance: Instance;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<InstanceDetailsDialogComponent>
  ) { }

  ngOnInit(): void {
    if (this.data.operationType === 'new') {
      this.instance = new Instance();
    } else {
      this.instance = { ...this.data.instance };
    }
  }

  onSave(): void {
    this.dialogRef.close({
      instance: this.instance,
      operationType: this.data.operationType
    });
  }

}

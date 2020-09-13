import { Component, OnInit, Inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Instance } from './../../model/Instance';

@Component({
  selector: 'app-instance-details-dialog',
  templateUrl: './instance-details-dialog.component.html',
  styleUrls: ['./instance-details-dialog.component.scss']
})
export class InstanceDetailsDialogComponent implements OnInit {
  instance: Instance;
  instanceDetailForm: FormGroup;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<InstanceDetailsDialogComponent>
  ) { }

  get instanceName(): FormControl {
    return this.instanceDetailForm.get('instanceName') as FormControl;
  }

  get targetURL(): FormControl {
    return this.instanceDetailForm.get('targetURL') as FormControl;
  }

  get wiremockURL(): FormControl {
    return this.instanceDetailForm.get('wiremockURL') as FormControl;
  }

  ngOnInit(): void {
    if (this.data.operationType === 'new') {
      this.instance = new Instance();
    } else {
      this.instance = { ...this.data.instance };
    }

    this.instanceDetailForm = new FormGroup({
      instanceName: new FormControl(this.instance.instanceName, Validators.required),
      targetURL: new FormControl(this.instance.targetURL, Validators.required),
      wiremockURL: new FormControl(this.instance.wiremockURL, Validators.required)
    });
  }

  onSave(): void {
    if (this.instanceDetailForm.valid) {
      this.instance = Object.assign(this.instance, this.instanceDetailForm.value);
      this.dialogRef.close({
        instance: this.instance,
        operationType: this.data.operationType
      });
    }
  }

}

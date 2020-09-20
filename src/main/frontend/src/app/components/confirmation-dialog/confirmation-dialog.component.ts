import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent implements OnInit {
  confirmationForm: FormGroup;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<ConfirmationDialogComponent>
  ) { }

  get comment(): FormControl {
    return this.confirmationForm.get('comment') as FormControl;
  }

  ngOnInit(): void {
    this.confirmationForm = new FormGroup({
      comment: new FormControl('', Validators.required)
    });
  }

  onSave(): void {
    if (this.confirmationForm.valid) {
      this.dialogRef.close(this.comment.value);
    }
  }

}

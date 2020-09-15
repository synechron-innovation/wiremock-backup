import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RecordingNameMappingPipe } from 'src/app/pipes/recording-name-mapping.pipe';

@Component({
  selector: 'app-edit-mappings-dialog',
  templateUrl: './edit-mappings-dialog.component.html',
  styleUrls: ['./edit-mappings-dialog.component.scss'],
  providers: [RecordingNameMappingPipe]
})
export class EditMappingsDialogComponent implements OnInit {
  mappingForm: FormGroup;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<EditMappingsDialogComponent>,
    private nameMappingPipe: RecordingNameMappingPipe
  ) { }

  get folderMapping(): FormControl {
    return this.mappingForm.get('folderMapping') as FormControl;
  }

  get recordingName(): FormControl {
    return this.mappingForm.get('recordingName') as FormControl;
  }

  ngOnInit(): void {
    this.mappingForm = new FormGroup({
      folderMapping: new FormControl('', [Validators.pattern(/^\w+( [\w>]+)*$/)])
    });

    if (!this.data.multipleMappings) {
      const mappingStringArray = (this.data.mappingString as string).split(':::');
      const folderStructure = mappingStringArray[0];
      let recordingName = mappingStringArray[1];

      if (recordingName) {
        this.mappingForm.get('folderMapping').setValue(this.nameMappingPipe.transform(folderStructure));
      } else {
        recordingName = folderStructure;
      }

      this.mappingForm.addControl(
        'recordingName',
        new FormControl(recordingName, [Validators.required, Validators.pattern(/^\w+$/)])
      );
    }
  }

  onSave(): void {
    if (this.mappingForm.valid) {
      let updatedMapping = this.mappingForm.get('folderMapping').value.split('>').map((mapping: string) => mapping.trim()).join('::');
      if (!this.data.multipleMappings) {
        updatedMapping += `${this.mappingForm.get('folderMapping').value ? ':::' : ''}` + this.mappingForm.get('recordingName').value;
      }
      this.dialogRef.close(updatedMapping);
    }
  }

}

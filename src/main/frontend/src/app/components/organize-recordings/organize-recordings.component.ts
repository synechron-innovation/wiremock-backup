import { InstanceMappingService } from './../../services/instance-mapping.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { MatSnackBar } from '@angular/material/snack-bar';

import { Instance } from './../../model/Instance';
import { Recording } from 'src/app/model/Recording';

@Component({
  selector: 'app-organize-recordings',
  templateUrl: './organize-recordings.component.html',
  styleUrls: ['./organize-recordings.component.scss']
})
export class OrganizeRecordingsComponent implements OnInit {
  instanceId: number;
  instance: Instance;
  recordings: Recording[];
  selectedRecording: Recording;
  editableRecording: string;

  constructor(
    private route: ActivatedRoute,
    private snackbar: MatSnackBar,
    private instanceMappingService: InstanceMappingService
  ) { }

  ngOnInit(): void {
    this.instanceId = +this.route.snapshot.paramMap.get('instanceId');

    // get instance details
    this.instanceMappingService.getInstanceByInstanceId(this.instanceId)
      .subscribe((instance) => {
        this.instance = instance;

        // get recordings for instance id
        this.instanceMappingService.getMappingsByInstanceId(this.instanceId)
          .subscribe((recordings) => this.recordings = recordings);
      });
  }

  onRecordingSelection(recordingName: string): void {
    this.selectedRecording = this.recordings.find((recording: Recording) => recording.name === recordingName);
    this.editableRecording = JSON.stringify(this.selectedRecording, undefined, 4);
    console.log('editableRecording: ', this.editableRecording);
  }

  applyChanges(): void {
    this.selectedRecording = Object.assign(this.selectedRecording, JSON.parse(this.editableRecording));
    this.recordings = Array.from(this.recordings);
    this.resetSelection();
  }

  resetSelection(): void {
    this.editableRecording = '';
    this.selectedRecording = null;
  }

  saveRecordings(): void {
    this.instanceMappingService.exportMappingsToDatabase(this.recordings, this.instanceId)
      .subscribe((response) => {
        this.snackbar.open('Recordings saved to database', 'Close', {
          duration: 2500
        });
      }, (error) => {
        this.snackbar.open('Error occurred. Please contact administrator.', 'Close', {
          duration: 2500
        });
      });
  }

  publishRecordings(): void {
    this.instanceMappingService.exportMappingsToWireMock(this.instanceId)
      .subscribe((response) => {
        this.snackbar.open('Recordings published to WireMock', 'Close', {
          duration: 2500
        });
      }, (error) => {
        this.snackbar.open('Error occurred. Please contact administrator.', 'Close', {
          duration: 2500
        });
      });
  }

}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

import { Instance } from './../../model/Instance';
import { Recording } from 'src/app/model/Recording';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { InstanceMappingService } from './../../services/instance-mapping.service';
import { TreeAction, TreeActionTypes } from './../../model/FolderNode';
import { RecordingResponse } from 'src/app/model/RecordingResponse';
import { RecordingRequest } from 'src/app/model/RecordingRequest';

@Component({
  selector: 'app-organize-recordings',
  templateUrl: './organize-recordings.component.html',
  styleUrls: ['./organize-recordings.component.scss']
})
export class OrganizeRecordingsComponent implements OnInit, OnDestroy {
  instanceId: number;
  instance: Instance;
  recordings: Recording[];
  selectedRecording: Recording;
  editableRecording: string;

  subscriptions: Subscription;

  constructor(
    private route: ActivatedRoute,
    private snackbar: MatSnackBar,
    private dialog: MatDialog,
    private instanceMappingService: InstanceMappingService
  ) { }

  ngOnInit(): void {
    this.subscriptions = new Subscription();
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
    const dialog = this.dialog.open(ConfirmationDialogComponent, {
      disableClose: true,
      width: '500px',
      height: '250px',
      data: {
        operationType: 'save',
      }
    });

    const dialogCloseSubscription = dialog.afterClosed().subscribe((comment: string) => {
      if (comment) {
        // TODO - add comment to the API call
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
    });

    this.subscriptions.add(dialogCloseSubscription);
  }

  publishRecordings(): void {
    const dialog = this.dialog.open(ConfirmationDialogComponent, {
      disableClose: true,
      width: '500px',
      height: '250px',
      data: {
        operationType: 'publish',
      }
    });

    const dialogCloseSubscription = dialog.afterClosed().subscribe((comment: string) => {
      if (comment) {
        // TODO - add comment to the API call
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
    });

    this.subscriptions.add(dialogCloseSubscription);
  }

  handleTreeAction(treeAction: TreeAction): void {
    if (treeAction.type === TreeActionTypes.REMOVE) {
      const recordingIndex = this.recordings.findIndex(recording => recording.name === treeAction.node.recordingPath);
      this.recordings.splice(recordingIndex, 1);
    } else if (treeAction.type === TreeActionTypes.ADD) {
      const recording = new Recording();
      recording.request = new RecordingRequest();
      recording.response = new RecordingResponse();
      recording.name = treeAction.node.recordingPath;
      this.recordings.push(recording);
    } else {
      const recordingToUpdate = this.recordings.find(recording => recording.name === treeAction.node.recordingPath);
      recordingToUpdate.name = treeAction.updatedRecordingPath;
    }
    this.resetSelection();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.subscriptions = null;
  }
}

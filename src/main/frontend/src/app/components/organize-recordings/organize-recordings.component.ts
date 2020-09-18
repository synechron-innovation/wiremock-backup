import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Location } from '@angular/common';

import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

import { Instance } from './../../model/Instance';
import { Recording } from 'src/app/model/Recording';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { InstanceMappingService } from './../../services/instance-mapping.service';
import { TreeAction, TreeActionTypes } from './../../model/FolderNode';
import { RecordingResponse } from 'src/app/model/RecordingResponse';
import { RecordingRequest } from 'src/app/model/RecordingRequest';
import { EditMappingsDialogComponent } from '../edit-mappings-dialog/edit-mappings-dialog.component';

@Component({
  selector: 'app-organize-recordings',
  templateUrl: './organize-recordings.component.html',
  styleUrls: ['./organize-recordings.component.scss']
})
export class OrganizeRecordingsComponent implements OnInit, OnDestroy {
  instanceId: number;
  instance: Instance;
  recordings: Recording[];
  recordingToBeEdited: Recording;
  recordingAsString: string;

  subscriptions: Subscription;

  constructor(
    private route: ActivatedRoute,
    private snackbar: MatSnackBar,
    private dialog: MatDialog,
    private location: Location,
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

  onEditRecording(recordingName: string): void {
    this.recordingToBeEdited = this.recordings.find((recording: Recording) => recording.name === recordingName);
    this.recordingAsString = JSON.stringify(this.recordingToBeEdited, undefined, 4);
  }

  applyChanges(): void {
    this.recordingToBeEdited = Object.assign(this.recordingToBeEdited, JSON.parse(this.recordingAsString));
    this.recordings = Array.from(this.recordings);
    this.resetSelection();
  }

  resetSelection(): void {
    this.recordingAsString = '';
    this.recordingToBeEdited = null;
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

  openEditMappingsDialog(selectedRecordings: Set<string>): void {
    const isSingleRecording = selectedRecordings.size === 1;
    const dialogData = {
      multipleMappings: !isSingleRecording,
      mappingString: (isSingleRecording) ? Array.from(selectedRecordings)[0] : ''
    };

    const dialog = this.dialog.open(EditMappingsDialogComponent, {
      disableClose: true,
      width: '500px',
      height: '250px',
      data: dialogData
    });

    const dialogCloseSubscription = dialog.afterClosed().subscribe((updatedMapping: string) => {

      selectedRecordings.forEach(recordingId => {
        const recording = this.recordings.find(record => record.name === recordingId);

        if (recording && isSingleRecording) {
          recording.name = updatedMapping;
        } else if (recording && !isSingleRecording) {
          const [mapping, recordingName] = recording.name.split(':::');
          recording.name = (updatedMapping) ? updatedMapping + ':::' : '';
          if (recordingName) {
            recording.name += recordingName;
          } else {
            recording.name += mapping;
          }
        }

      });

      this.recordings = Array.from(this.recordings);
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
    } else if (treeAction.type === TreeActionTypes.CLONE) {
      const existingRecording = this.recordings.find(recording => recording.name === treeAction.node.recordingPath);
      if (!!existingRecording) {
        const randomHexString = this.getRandomHex(10);
        const clonedRecording = {
          ...existingRecording,
          ...{ id: randomHexString, uuid: randomHexString, name: treeAction.updatedRecordingPath }
        };
        this.recordings.push(clonedRecording);
      }
    } else {
      const recordingToUpdate = this.recordings.find(recording => recording.name === treeAction.node.recordingPath);
      recordingToUpdate.name = treeAction.updatedRecordingPath;
    }
    this.resetSelection();
  }

  private getRandomHex(size: number): string {
    const result = [];
    const hexRef = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'];

    for (let n = 0; n < size; n++) {
      result.push(hexRef[Math.floor(Math.random() * 16)]);
    }
    return result.join('');
  }

  onGoBack(): void {
    this.location.back();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.subscriptions = null;
  }
}

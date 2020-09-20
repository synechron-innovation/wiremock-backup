import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';

import { Instance } from 'src/app/model/Instance';
import { InstanceRecordingData } from './../../model/Instance';
import { InstanceDetailsDialogComponent } from './../instance-details-dialog/instance-details-dialog.component';
import { InstanceMappingService } from 'src/app/services/instance-mapping.service';
import { HistoryDialogComponent } from '../history-dialog/history-dialog.component';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit, OnDestroy {
  instances: Instance[] = [];
  selectedInstance: Instance;
  subscription: Subscription;
  instanceDataArray: InstanceRecordingData[];

  constructor(
    private dialog: MatDialog,
    private snackbar: MatSnackBar,
    private instanceMappingService: InstanceMappingService
  ) { }

  ngOnInit(): void {
    this.subscription = new Subscription();
    this.getAllInstances();
  }

  updateSelection(instance: Instance): void {
    if (!this.selectedInstance || (this.selectedInstance.id !== instance.id)) {
      this.selectedInstance = instance;
    } else {
      this.selectedInstance = null;
    }
  }

  getAllInstances(): void {
    this.instanceMappingService.getAllInstances().subscribe((listOfInstances) => {
      this.instances = listOfInstances;
      this.refreshRecordingStatusAndMappingCount();
    });
  }

  openInstanceDetailsDialog(instance?: Instance): void {
    const instanceDialog = this.dialog.open(InstanceDetailsDialogComponent, {
      disableClose: true,
      width: '500px',
      data: {
        operationType: (!!instance) ? 'edit' : 'new',
        instance
      }
    });

    const instanceDialogSub = instanceDialog.afterClosed().subscribe((result: any) => {
      if (typeof result === 'object') {
        if (result.operationType === 'new') {
          const modifiedInstance = result.instance;

          this.instanceMappingService.createInstance(
            modifiedInstance.instanceName, modifiedInstance.targetURL, modifiedInstance.wiremockURL)
            .subscribe((response) => {
              this.showSnackbar('New instance created successfully');
              this.getAllInstances();
            }, (error) => {
              this.showSnackbar('Error occurred while creating a new instance');
            });

        } else {
          this.instanceMappingService.updateInstance(result.instance)
            .subscribe((response) => {
              this.showSnackbar('Instance updated successfully');
              this.getAllInstances();
            }, (error) => {
              this.showSnackbar('Error occurred while updating the instance');
            });
        }
      }
    });

    this.subscription.add(instanceDialogSub);
  }

  openHistoryDialog(instanceId: number): void {
    this.instanceMappingService.importHistoryFromDB(instanceId).subscribe((histories) => {
      this.dialog.open(HistoryDialogComponent, {
        disableClose: true,
        width: '500px',
        data: {
          histories
        }
      });
    });
  }

  deleteInstance(): void {
    if (this.selectedInstance) {
      this.instanceMappingService.deleteInstance(this.selectedInstance.id)
        .subscribe((response) => {
          this.showSnackbar('Successfully deleted');
          this.getAllInstances();
        }, (error) => {
          this.showSnackbar('Error occurred while deleting the instance');
        });
    } else {
      this.showSnackbar('Please select an instance to delete');
    }
  }

  startRecording(instance: Instance): void {
    const recordingStatus = this.getMappingCountOrRecordingStatus(instance.id, 'status');

    if (recordingStatus !== 'RECORDING') {
      this.instanceMappingService.startRecording(instance.id)
        .subscribe((response) => {
          this.refreshRecordingStatusAndMappingCount();
          this.showSnackbar('Recording started successfully');
        }, (error) => {
          this.showSnackbar('Error occurred while starting the recording');
        });
    } else {
      this.showSnackbar('Recording is already started');
    }
  }

  stopRecording(instance: Instance): void {
    const recordingStatus = this.getMappingCountOrRecordingStatus(instance.id, 'status');

    if (recordingStatus !== 'NEVERSTARTED' && recordingStatus !== 'STOPPED') {
      this.instanceMappingService.stopRecording(instance.id)
        .subscribe((response) => {
          this.refreshRecordingStatusAndMappingCount();
          this.showSnackbar('Recording stopped successfully');
        }, (error) => {
          this.showSnackbar('Error occurred while stopping the recording');
        });
    } else {
      this.showSnackbar('Recording is already stopped');
    }
  }

  stageMappings(instanceId: number): void {
    this.instanceMappingService.importMappingsFromWireMock(instanceId, 100, 0)
      .subscribe((response) => {
        this.refreshRecordingStatusAndMappingCount();
        this.showSnackbar('Successfully staged mappings');
      }, (error) => {
        this.showSnackbar('Error occurred while trying to stage mappings');
      });
  }

  exportMappings(instanceId: number): void {
    this.instanceMappingService.exportMappingsToWireMock(instanceId)
      .subscribe((response) => {
        this.showSnackbar('Successfully exported mappings');
      }, (error) => {
        this.showSnackbar('Error occurred while trying to export mappings');
      });
  }

  refreshRecordingStatusAndMappingCount(): void {
    if (!this.instances || !this.instances.length) {
      return;
    }
    this.instanceMappingService.getRecordingStatusAndMappingCount(
      this.instances.map(instance => instance.id)
    ).subscribe((response) => {
      this.instanceDataArray = response;
    });
  }

  getMappingCountOrRecordingStatus(instanceId: number, query: string): string {
    const instanceData = this.instanceDataArray && this.instanceDataArray.find(instanceDataObj => instanceDataObj.id === instanceId);
    if (query === 'count') {
      return instanceData ? `${instanceData.mappingCount} Recording${(instanceData.mappingCount === 1) ? '' : 's'}` : '';
    } else {
      return instanceData ? instanceData.statusEnum : '';
    }
  }

  showSnackbar(message: string): void {
    this.snackbar.open(message, 'Close', {
      duration: 2500
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.subscription = null;
  }

}

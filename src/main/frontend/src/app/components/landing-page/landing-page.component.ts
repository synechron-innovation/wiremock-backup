import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';

import { Instance } from 'src/app/model/Instance';
import { InstanceDetailsDialogComponent } from './../instance-details-dialog/instance-details-dialog.component';
import { InstanceMappingService } from 'src/app/services/instance-mapping.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit, OnDestroy {
  instances: Instance[] = [];
  selectedInstance: Instance;
  subscription: Subscription;
  recordingStatusMap: WeakMap<Instance, string>;

  constructor(
    private dialog: MatDialog,
    private snackbar: MatSnackBar,
    private instanceMappingService: InstanceMappingService
  ) { }

  ngOnInit(): void {
    this.subscription = new Subscription();
    this.recordingStatusMap = new WeakMap();
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
      listOfInstances.forEach((instance) => {
        this.instanceMappingService.getRecordingStatus(instance.id)
          .subscribe((recordingState) => this.recordingStatusMap.set(instance, recordingState));
      });
    });
  }

  openInstanceDetailsDialog(instance?: Instance): void {
    const instanceDialog = this.dialog.open(InstanceDetailsDialogComponent, {
      disableClose: true,
      width: '30vw',
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
    const recordingStatus = this.recordingStatusMap.get(instance);

    if (recordingStatus !== 'RECORDING') {
      this.instanceMappingService.startRecording(instance.id)
        .subscribe((response) => {
          this.recordingStatusMap.set(instance, 'RECORDING');
          this.showSnackbar('Recording started successfully');
        }, (error) => {
          this.showSnackbar('Error occurred while starting the recording');
        });
    } else {
      this.showSnackbar('Recording is already started');
    }
  }

  stopRecording(instance: Instance): void {
    const recordingStatus = this.recordingStatusMap.get(instance);

    if (recordingStatus !== 'NEVERSTARTED' && recordingStatus !== 'STOPPED') {
      this.instanceMappingService.stopRecording(instance.id)
        .subscribe((response) => {
          this.recordingStatusMap.set(instance, 'STOPPED');
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

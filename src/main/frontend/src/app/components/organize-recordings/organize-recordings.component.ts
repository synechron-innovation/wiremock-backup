import { InstanceMappingService } from './../../services/instance-mapping.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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
  selectedRecording: Recording;
  editableRecording: string;

  constructor(
    private route: ActivatedRoute,
    private instanceMappingService: InstanceMappingService
  ) { }

  ngOnInit(): void {
    this.instanceId = +this.route.snapshot.paramMap.get('instanceId');

    this.instanceMappingService.getInstanceWithMappingsById(this.instanceId)
      .subscribe((instance: Instance) => this.instance = instance);
  }

  onRecordingSelection(recordingName: string): void {
    this.selectedRecording = this.instance.mappings.find((recording: Recording) => recording.name === recordingName);
    this.editableRecording = JSON.stringify(this.selectedRecording, undefined, 4);
    console.log('editableRecording: ', this.editableRecording);
  }

}

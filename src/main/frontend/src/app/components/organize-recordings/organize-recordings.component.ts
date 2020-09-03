import { InstanceMappingService } from './../../services/instance-mapping.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Instance } from './../../model/Instance';

@Component({
  selector: 'app-organize-recordings',
  templateUrl: './organize-recordings.component.html',
  styleUrls: ['./organize-recordings.component.scss']
})
export class OrganizeRecordingsComponent implements OnInit {
  instanceId: number;
  instance: Instance;

  constructor(
    private route: ActivatedRoute,
    private instanceMappingService: InstanceMappingService
  ) { }

  ngOnInit(): void {
    this.instanceId = +this.route.snapshot.paramMap.get('instanceId');

    this.instanceMappingService.getInstanceWithMappingsById(this.instanceId)
      .subscribe((instance: Instance) => this.instance = instance);
  }

}

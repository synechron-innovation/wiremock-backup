import { Component, OnInit } from '@angular/core';
import { Instance } from 'src/app/model/Instance';
import { InstanceMappingService } from 'src/app/services/instance-mapping.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit {
  instances: Instance[] = [];
  selectedInstance: Instance;

  constructor(
    private instanceMappingService: InstanceMappingService
  ) { }

  ngOnInit(): void {
    this.instanceMappingService.getAllInstances().subscribe((listOfInstances) => {
      this.instances = listOfInstances;
    });
  }



}

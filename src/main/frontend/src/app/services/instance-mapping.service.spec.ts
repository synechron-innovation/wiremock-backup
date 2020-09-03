import { TestBed } from '@angular/core/testing';

import { InstanceMappingService } from './instance-mapping.service';

describe('InstanceMappingService', () => {
  let service: InstanceMappingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InstanceMappingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

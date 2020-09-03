import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, EMPTY, of } from 'rxjs';
import { Instance } from './../model/Instance';

@Injectable({
  providedIn: 'root'
})
export class InstanceMappingService {
  private BASE_URL = 'http://localhost:8080';

  constructor(
    private http: HttpClient
  ) { }

  getInstanceWithMappingsById(instanceId: number): Observable<Instance> {
    const url = `${this.BASE_URL}/instance/id/${instanceId}/withMappings`;

    return this.http.get<Instance>(url);
  }
}

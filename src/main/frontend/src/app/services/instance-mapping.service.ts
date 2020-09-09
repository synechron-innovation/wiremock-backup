import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, EMPTY, of } from 'rxjs';
import { Instance } from './../model/Instance';
import { Recording } from '../model/Recording';

@Injectable({
  providedIn: 'root'
})
export class InstanceMappingService {
  private BASE_URL = 'http://localhost:8080';

  constructor(
    private http: HttpClient
  ) { }

  createInstance(instanceName: string, targetURL: string, wiremockURL: string): Observable<Instance> {
    return this.http.post<Instance>(
      `${this.BASE_URL}/instance?instanceName=${instanceName}&targetURL=${targetURL}&wiremockURL=${wiremockURL}`,
      null
    );
  }

  updateInstance(instance: Instance): Observable<Instance> {
    return this.http.put<Instance>(`${this.BASE_URL}/instance`, instance);
  }

  deleteInstance(id: number): Observable<any> {
    return this.http.delete<any>(`${this.BASE_URL}/instance?id=${id}`);
  }

  getAllInstances(): Observable<Instance[]> {
    return this.http.get<Instance[]>(`${this.BASE_URL}/instance`);
  }

  getInstanceByInstanceId(instanceId: number): Observable<Instance> {
    return this.http.get<Instance>(`${this.BASE_URL}/instance/id/${instanceId}`);
  }

  startRecording(instanceId: number): Observable<any> {
    return this.http.get<any>(`${this.BASE_URL}/recordings/start/${instanceId}`);
  }

  stopRecording(instanceId: number): Observable<any> {
    return this.http.get<any>(`${this.BASE_URL}/recordings/stop/${instanceId}`);
  }

  getRecordingStatus(instanceId: number): Observable<string> {
    return this.http.get<string>(`${this.BASE_URL}/recordings/status/${instanceId}`);
  }

  getMappingsByInstanceId(instanceId: number): Observable<Recording[]> {
    return this.http.get<Recording[]>(
      `${this.BASE_URL}/mappingOperationsLocalAndDB/importFromDB/instanceId/${instanceId}`
    );
  }

  importMappingsFromWireMock(instanceId: number, limit: number, offset: number): Observable<number> {
    return this.http.get<number>(
      `${this.BASE_URL}/mappingOperationsDBAndWM/importFromWiremock/instanceId/${instanceId}?limit=${limit}&offset=${offset}`
    );
  }

  exportMappingsToDatabase(recordings: Recording[], instanceId: number): Observable<Instance> {
    return this.http.post<Instance>(
      `${this.BASE_URL}/mappingOperationsLocalAndDB/exportToDB/instanceId/${instanceId}`,
      recordings
    );
  }

  exportMappingsToWireMock(instanceId: number): Observable<any> {
    return this.http.post<any>(
      `${this.BASE_URL}/mappingOperationsDBAndWM/exportToWiremock/instanceId/${instanceId}`,
      {}
    );
  }
}

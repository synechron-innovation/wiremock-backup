import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpinnerService {
  private showSpinner = new BehaviorSubject(false);

  constructor() { }

  startSpinner(): void {
    this.showSpinner.next(true);
  }

  stopSpinner(): void {
    this.showSpinner.next(false);
  }

  getSpinnerState(): Observable<boolean> {
    return this.showSpinner.asObservable();
  }
}

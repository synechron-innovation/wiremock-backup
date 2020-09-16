import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { SpinnerService } from './spinner.service';

@Injectable({
  providedIn: 'root'
})
export class SpinnerInterceptorService implements HttpInterceptor {
  private requests: HttpRequest<any>[] = [];

  constructor(private spinnerService: SpinnerService) { }

  removeRequest(req: HttpRequest<any>): void {
    const index = this.requests.indexOf(req);
    if (index >= 0) {
      this.requests.splice(index, 1);
    }
    if (!this.requests.length) {
      this.spinnerService.stopSpinner();
    }
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url.indexOf('status') === -1) {
      this.requests.push(req);
    }
    this.spinnerService.startSpinner();
    return next.handle(req).pipe(
      finalize(() => {
        this.removeRequest(req);
      })
    );
  }
}

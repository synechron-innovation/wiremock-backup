import { SpinnerInterceptorService } from './services/spinner-interceptor.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';

import { CdkTreeModule } from '@angular/cdk/tree';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { OrganizeRecordingsComponent } from './components/organize-recordings/organize-recordings.component';
import { InstanceMappingService } from './services/instance-mapping.service';
import { FolderTreeComponent } from './components/organize-recordings/folder-tree/folder-tree.component';
import { RecordingNameMappingPipe } from './pipes/recording-name-mapping.pipe';
import { SpinnerComponent } from './components/spinner/spinner.component';
import { SpinnerService } from './services/spinner.service';

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    OrganizeRecordingsComponent,
    FolderTreeComponent,
    RecordingNameMappingPipe,
    SpinnerComponent
  ],
  entryComponents: [SpinnerComponent],
  imports: [
    BrowserModule,
    MatCardModule,
    CdkTreeModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
  ],
  providers: [
    InstanceMappingService, SpinnerService,
    { provide: HTTP_INTERCEPTORS, useClass: SpinnerInterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

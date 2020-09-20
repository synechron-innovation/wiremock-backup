import { ClipboardModule } from '@angular/cdk/clipboard';
import { CdkTreeModule } from '@angular/cdk/tree';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule, MAT_TOOLTIP_DEFAULT_OPTIONS } from '@angular/material/tooltip';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ConfirmationDialogComponent } from './components/confirmation-dialog/confirmation-dialog.component';
import { EditMappingsDialogComponent } from './components/edit-mappings-dialog/edit-mappings-dialog.component';
import { InstanceDetailsDialogComponent } from './components/instance-details-dialog/instance-details-dialog.component';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { FolderTreeComponent } from './components/organize-recordings/folder-tree/folder-tree.component';
import { OrganizeRecordingsComponent } from './components/organize-recordings/organize-recordings.component';
import { SpinnerComponent } from './components/spinner/spinner.component';
import { RecordingNameMappingPipe } from './pipes/recording-name-mapping.pipe';
import { InstanceMappingService } from './services/instance-mapping.service';
import { SpinnerInterceptorService } from './services/spinner-interceptor.service';
import { SpinnerService } from './services/spinner.service';
import { HistoryDialogComponent } from './components/history-dialog/history-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    OrganizeRecordingsComponent,
    FolderTreeComponent,
    RecordingNameMappingPipe,
    SpinnerComponent,
    ConfirmationDialogComponent,
    InstanceDetailsDialogComponent,
    EditMappingsDialogComponent,
    HistoryDialogComponent
  ],
  entryComponents: [
    SpinnerComponent,
    ConfirmationDialogComponent,
    InstanceDetailsDialogComponent,
    HistoryDialogComponent
  ],
  imports: [
    BrowserModule,
    CdkTreeModule,
    ClipboardModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatTooltipModule,
    MatCheckboxModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
  ],
  providers: [
    InstanceMappingService, SpinnerService,
    { provide: HTTP_INTERCEPTORS, useClass: SpinnerInterceptorService, multi: true },
    {
      provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: {
        position: 'above'
      }
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

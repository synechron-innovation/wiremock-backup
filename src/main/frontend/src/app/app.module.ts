import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { CdkTreeModule } from '@angular/cdk/tree';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { OrganizeRecordingsComponent } from './components/organize-recordings/organize-recordings.component';
import { InstanceMappingService } from './services/instance-mapping.service';
import { FolderTreeComponent } from './components/organize-recordings/folder-tree/folder-tree.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    OrganizeRecordingsComponent,
    FolderTreeComponent
  ],
  imports: [
    BrowserModule,
    MatCardModule,
    CdkTreeModule,
    MatIconModule,
    MatButtonModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
  ],
  providers: [InstanceMappingService],
  bootstrap: [AppComponent]
})
export class AppModule { }

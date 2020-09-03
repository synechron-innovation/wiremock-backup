import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { OrganizeRecordingsComponent } from './components/organize-recordings/organize-recordings.component';

const routes: Routes = [
  {
    path: 'organize/:instanceId',
    component: OrganizeRecordingsComponent
  },
  {
    path: '',
    component: LandingPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

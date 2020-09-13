import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AdminComponentComponent } from './admin-component/admin-component.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import {RouterModule, Routes} from '@angular/router';
import {MatButtonModule} from '@angular/material';
import {SESSION_STORAGE_SERVICE, SessionService} from './vk-services/session.service';
import {LOCAL_STORAGE} from 'ngx-webstorage-service';
import {HttpClientModule} from '@angular/common/http';

const appRoutes: Routes = [
  { path: '', component: AdminComponentComponent},
  { path: '**', component: AdminComponentComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    AdminComponentComponent
  ],
  imports: [
    BrowserModule,
    NoopAnimationsModule,
    RouterModule,
    RouterModule.forRoot(appRoutes),
    MatButtonModule,
    HttpClientModule
  ],
  providers: [ { provide: SESSION_STORAGE_SERVICE, useExisting: LOCAL_STORAGE }, SessionService],
  bootstrap: [AppComponent]
})
export class AppModule { }

import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {AdminComponentComponent} from './admin-component/admin-component.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {RouterModule, Routes} from '@angular/router';
import {MatButtonModule, MatProgressBarModule} from '@angular/material';
import {LOCAL_STORAGE} from 'ngx-webstorage-service';
import {HttpClientModule} from '@angular/common/http';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatPaginatorModule} from '@angular/material/paginator';
import {ConfirmationComponent} from './admin-component/confiramtion/confirmation/confirmation.component';
import {MatDialogModule} from '@angular/material/dialog';
import {CrawlerClient} from './proto-gen/crawler_pb_service';
import {CrawlerClientFactory} from './crawler-client-factory';
import { ActuatorComponent } from './admin-component/actuator/actuator.component';

const appRoutes: Routes = [
  { path: '', component: AdminComponentComponent},
  { path: '**', component: AdminComponentComponent }
];
@NgModule({
  declarations: [
    AppComponent,
    ConfirmationComponent,
    AdminComponentComponent,
    ActuatorComponent
  ],
  imports: [
    FormsModule,
    RouterModule,
    BrowserModule,
    MatInputModule,
    MatButtonModule,
    MatDialogModule,
    HttpClientModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatPaginatorModule,
    ReactiveFormsModule,
    ReactiveFormsModule,
    NoopAnimationsModule,
    RouterModule.forRoot(appRoutes),
    MatProgressBarModule
  ],
  providers: [
    { provide: CrawlerClient, useFactory: CrawlerClientFactory }
  ],
  bootstrap: [AppComponent],
  entryComponents: [ConfirmationComponent, ActuatorComponent]
})
export class AppModule { }

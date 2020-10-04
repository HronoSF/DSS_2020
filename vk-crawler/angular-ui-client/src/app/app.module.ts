import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {SearchClient} from './proto-gen/search_pb_service';
import {SearchClientFactory} from './search-proto-service-factory';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {SearchService} from './app.service';
import {NgxGraphModule} from '@swimlane/ngx-graph';
import {MatCheckboxModule} from '@angular/material';
import {AuthService} from './auth.service';
import {VkSearchService} from './vk-search.service';
import {RouterModule, Routes} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import { ClientComponent } from './client/client.component';


const appRoutes: Routes = [
  { path: '', component: ClientComponent},
  { path: '**', component: ClientComponent }
];
@NgModule({
  declarations: [
    AppComponent,
    ClientComponent
  ],
  imports: [
    BrowserModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    NoopAnimationsModule,
    NgxGraphModule,
    MatCheckboxModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    { provide: SearchClient, useFactory: SearchClientFactory },
    SearchService,
    AuthService,
    VkSearchService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

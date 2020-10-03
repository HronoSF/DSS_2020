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

@NgModule({
  declarations: [
    AppComponent
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
    NgxGraphModule
  ],
  providers: [
    { provide: SearchClient, useFactory: SearchClientFactory },
    SearchService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

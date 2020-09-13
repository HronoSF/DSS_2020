import { Component, OnInit } from '@angular/core';
import {AuthService} from '../vk-services/auth.service';
import {vkOpenAuthDialogURL} from '../const';
import {ActivatedRoute} from '@angular/router';
import {throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';

@Component({
  selector: 'app-admin-component',
  templateUrl: './admin-component.component.html',
  styleUrls: ['./admin-component.component.css']
})
export class AdminComponentComponent implements OnInit {
  vkAuthPath = vkOpenAuthDialogURL;

  constructor(private authService: AuthService, private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    const vkCode = this.activatedRoute.snapshot.queryParams.code;
    if (vkCode && vkCode !== '') {
      this.authService.getAccessToken(vkCode).pipe(catchError(error => {
        return throwError(error);
      })).subscribe(vkAccessResponse => console.log(vkAccessResponse));
    } else {
      window.location.href = this.vkAuthPath;
    }
  }

}

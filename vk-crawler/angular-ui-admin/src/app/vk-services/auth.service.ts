import { Injectable, NgZone } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable, throwError} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {vkAccessTokenURLWithProxy, vkOpenAuthDialogURL} from '../const';
import {catchError, tap} from 'rxjs/operators';
import {VkAccessTokenResponse} from './insterfaces/vkAccessTokenResponse';

declare var VK;

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public vkAccessToken: VkAccessTokenResponse;

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private http: HttpClient
               ) {
  }

  public getAccessToken(code: string): Observable<any> {

    return this.http.get(`${vkAccessTokenURLWithProxy}&code=${code}`).pipe(
      catchError(error => {
        if ( error.code === 401 ) {
          window.location.href = vkOpenAuthDialogURL;
        }
        return throwError(error);
      }),
      tap( (response: VkAccessTokenResponse) => {
            this.vkAccessToken = response;
            if (this.activatedRoute.snapshot.queryParams.code) {
              this.router.navigate([], {queryParams: { code: '' }});
            }
      }
    ));
  }
}

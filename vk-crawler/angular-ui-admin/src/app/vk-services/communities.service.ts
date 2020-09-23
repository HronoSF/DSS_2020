import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {proxyForAPIRequest, vkAPIRequest} from '../const';
import {AuthService} from './auth.service';
import {VkCommonResponse} from "./insterfaces/VkCommonResponse";
import {VkGroupsResponse} from "./insterfaces/vkGroupsResponse";
import {map, switchMap} from "rxjs/operators";
import {VkUsersResponse} from "./insterfaces/vkUsersResponse";

@Injectable({
  providedIn: 'root'
})
export class CommunitiesService {

  constructor(private http: HttpClient, private authService: AuthService) { }


  public searchCommunities(request): Observable<VkGroupsResponse[]> {
    return this.http
      .get(`${proxyForAPIRequest}/groups.search?q=${request.query}&access_token=${this.authService.vkAccessToken.access_token}&v=5.54`)
      .pipe(map((res: VkCommonResponse<VkGroupsResponse>) => {
        return res.response.items;
      }));
  }

  public searchUsers(request): Observable<VkUsersResponse[]> {
    return this.http
      .get(`${proxyForAPIRequest}/users.search?q=${request.query}&access_token=${this.authService.vkAccessToken.access_token}&v=5.54`)
      .pipe(map((res: VkCommonResponse<VkUsersResponse>) => {
        return res.response.items;
      }));
  }

}

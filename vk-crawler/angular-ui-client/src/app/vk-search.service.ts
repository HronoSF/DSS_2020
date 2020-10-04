import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {proxyForAPIRequest} from './const';
import {AuthService} from './auth.service';
import {switchMap} from 'rxjs/operators';

export interface VkCommonResponse<T> {
  response: {
    count: number;
    items: T[];
  };
}


export interface VkUsersResponse {
  id: number;
  first_name: string;
  last_name: string;
  screen_name: string;
  photo: string;
}

export interface VkGroupsResponse {
  id: number;
  name: string;
  screen_name: string;
  is_closed: number;
  type: string;
  is_admin: number;
  is_member: number;
  is_advertiser: number;
  photo_50: string;
  photo_100: string;
  photo_200: string;
}

@Injectable()
export class VkSearchService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  public getUsers(request) {
    if (request === '') {
      return of({});
    }
    return this.http
      .get<{response: VkUsersResponse[]}>(
        `${proxyForAPIRequest}/users.get?user_ids=${request}&access_token=${this.authService.vkAccessToken.access_token}&fields=photo&v=5.54`)
      .pipe(
        switchMap(response => {
          const usersMap: {[key: string]: VkUsersResponse} = {};
          response.response.forEach(user => {
            if (!usersMap[user.id]) {
              usersMap[user.id] = user;
            }
          });
          return [usersMap];
        })
      );
  }

  public getGroups(request) {
    if (request === '') {
      return of({});
    }
    return this.http
      .get<{response: VkGroupsResponse[]}>(
        `${proxyForAPIRequest}/groups.getById?group_ids=${request}&access_token=${this.authService.vkAccessToken.access_token}&v=5.54`)
      .pipe(
        switchMap(response => {
          const groupsMap: {[key: string]: VkGroupsResponse} = {};
          response.response.forEach(group => {
            if (!groupsMap[group.id]) {
              groupsMap[group.id] = group;
            }
          });
          return [groupsMap];
        })
      );
  }
}

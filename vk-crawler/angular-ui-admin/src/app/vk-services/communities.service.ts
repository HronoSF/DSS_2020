import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, Observer} from 'rxjs';
import {proxyForAPIRequest} from '../const';
import {AuthService} from './auth.service';
import {VkCommonResponse} from './insterfaces/VkCommonResponse';
import {VkGroupsResponse} from './insterfaces/vkGroupsResponse';
import {VkUsersResponse} from './insterfaces/vkUsersResponse';
import {CrawlerClient} from '../proto-gen/crawler_pb_service';
import {CrawlerJobStatus, StartParsingRequest} from '../proto-gen/crawler_pb';
import {environment} from '../../environments/environment';
// import {Text} from '../proto-gen/datahash_pb';
// import {DataHashClient} from "../proto-gen/datahash_pb_service";

declare var VK;

@Injectable({
  providedIn: 'root'
})
export class CommunitiesService {

  public chosenGroups: VkGroupsResponse[] = [];
  public chosenUsers: VkUsersResponse[] = [];

  private crawlerClient: CrawlerClient = new CrawlerClient(environment.backendUrl);
  // private testGRPC: DataHashClient = new DataHashClient(environment.backendUrl);

  constructor(
    private http: HttpClient,
    private authService: AuthService) {}


  public searchCommunities(request, offset: number, count: number): Observable<VkCommonResponse<VkGroupsResponse>> {
    return this.http
      .get<VkCommonResponse<VkGroupsResponse>>(
        `${proxyForAPIRequest}/groups.search?q=${request.query}&access_token=${this.authService.vkAccessToken.access_token}&offset=${offset}&count=${count}&v=5.54`);
  }

  public searchUsers(request, offset: number, count: number): Observable<VkCommonResponse<VkUsersResponse>> {
    console.log(request.query);
    return this.http
      .get<VkCommonResponse<VkUsersResponse>>(`${proxyForAPIRequest}/users.search?q=${request.query}&access_token=${this.authService.vkAccessToken.access_token}&offset=${offset}&count=${count}&fields=photo&v=5.54`);
  }


  public chooseGroups(id: number, allGroups, event) {
    if (event) {
      this.chosenGroups.push(allGroups.find(item => item.id === +id));
    } else {
      this.chosenGroups = this.chosenGroups.filter(group => group.id !== id);
    }
    console.log(this.chosenGroups);
  }

  public chooseUsers(id: number, allUsers, event) {
    if (event) {
      this.chosenUsers.push(allUsers.find(item => item.id === +id));
    } else {
      this.chosenUsers.filter(user => user.id !== id);
    }
    console.log(this.chosenUsers);
  }

  public startCrawling(): Observable<any> {
    const idsList = this.chosenGroups.map(group => group.id.toString()).concat(this.chosenUsers.map(user => user.id.toString()));
    const request = new StartParsingRequest();
    request.setToparseList(idsList);

    return new Observable((observer: Observer<any>) => {
      this.crawlerClient.startCrawling(request, ((error, response: CrawlerJobStatus) => {
          if (error) {
            observer.error(new Error(error.message));
          } else {
            const result = response.getDomaintostatusMap();
            observer.next(result);
            observer.complete();
          }
      }));
    });
  }

  // public testGrpc(): Observable<any> {
  //   const text = 'I love Gleb <3';
  //   const request = new Text();
  //   request.setData(text);
  //
  //   return new Observable((observer: Observer<any>) => {
  //     this.testGRPC.hash_md5(request, ((error, response: Text) => {
  //       if (error) {
  //         observer.error(new Error(error.message));
  //       } else {
  //         const result = response.getData();
  //         observer.next(result);
  //         observer.complete();
  //       }
  //     }));
  //   });
  // }
}

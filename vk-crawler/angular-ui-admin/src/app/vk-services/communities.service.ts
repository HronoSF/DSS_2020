import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, Observer, Subject} from 'rxjs';
import {proxyForAPIRequest} from '../const';
import {AuthService} from './auth.service';
import {VkCommonResponse} from './insterfaces/VkCommonResponse';
import {VkGroupsResponse} from './insterfaces/vkGroupsResponse';
import {VkUsersResponse} from './insterfaces/vkUsersResponse';
import {CrawlerClient} from '../proto-gen/crawler_pb_service';
import {CrawlerJobStatusDTO, CrawlerProgressByDomainsResponseDTO, StartParsingAsServiceRequestDTO} from '../proto-gen/crawler_pb';
import {environment} from '../../environments/environment';
import {concatMap, delay, takeUntil} from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class CommunitiesService {

  public statuses: CrawlerProgressByDomainsResponseDTO.AsObject;

  public chosenGroups: VkGroupsResponse[] = [];
  public chosenUsers: VkUsersResponse[] = [];

  private crawlerClient: CrawlerClient = new CrawlerClient(environment.backendUrl);

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
      .get<VkCommonResponse<VkUsersResponse>>(`${proxyForAPIRequest}/users.search?q=${request.query}&access_token=${this.authService.vkAccessToken.access_token}&offset=${offset}&count=${count}&fields=photo,domain&v=5.54`);
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

  public startCrawling(): Observable<CrawlerJobStatusDTO.AsObject> {
    const idsList = this.chosenGroups.map(group => group.screen_name.toString()).concat(this.chosenUsers.map(user => user.domain.toString()));
    console.log(idsList);
    const request = new StartParsingAsServiceRequestDTO();
    request.setToparseList(idsList);

    return new Observable((observer: Observer<any>) => {
      this.crawlerClient.startCrawlingAsServiceActor(request, ((error, response: CrawlerJobStatusDTO) => {
          if (error) {
            observer.error(new Error(error.message));
          } else {
            const result = response.toObject();
            observer.next(result);
            observer.complete();
          }
      }));
    });
  }

  public getCrawlerStatus(): Observable<CrawlerProgressByDomainsResponseDTO.AsObject> {
    const request = new CrawlerProgressByDomainsResponseDTO();
    return new Observable((observer: Observer<any>) => {
      this.crawlerClient.getCrawlerProgress(request, ((error, response: CrawlerProgressByDomainsResponseDTO) => {
        if (error) {
          observer.error(new Error(error.message));
        } else {
          const result = response.toObject();
          observer.next(result);
          observer.complete();
        }
      }));
    });
  }

  public updateCrawlerStatus() {
   return this.getCrawlerStatus().pipe(
      delay(1000),
      concatMap((status) => {
          this.statuses = status;
          console.log(this.statuses);
          return this.updateCrawlerStatus();
        }
      ),
    );
  }
}

import {Injectable} from '@angular/core';
import {SearchClient} from './proto-gen/search_pb_service';
import {SearchRequest, SearchResponse, WallPost} from './proto-gen/search_pb';
import {Observable, Observer} from 'rxjs';

@Injectable()

export class SearchService {

  constructor(private searchClient: SearchClient) {}

  public search(query): Observable<SearchResponse.AsObject> {

    const request = new SearchRequest();
    request.setTexttosearch(query);

    return new Observable((observer: Observer<any>) => {
      this.searchClient.search(request, ((error, response: SearchResponse) => {
        console.log(error, response);
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
}

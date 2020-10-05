import {Injectable} from '@angular/core';
import {SearchClient} from './proto-gen/search_pb_service';
import {TestSearchRequestDTO, IdSearchRequestDTO, TextSearchResponseDTO, IdSearchResponseDTO} from './proto-gen/search_pb';
import {Observable, Observer} from 'rxjs';

@Injectable()

export class SearchService {

  constructor(private searchClient: SearchClient) {}

  public searchWithText(query, page, size): Observable<TextSearchResponseDTO.AsObject> {

    const request = new TestSearchRequestDTO();
    request.setTexttosearch(query);
    request.setPage(page);
    request.setSize(size);

    return new Observable((observer: Observer<any>) => {
      this.searchClient.searchWithText(request, ((error, response: TextSearchResponseDTO) => {
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

  public searchWithId(query): Observable<IdSearchResponseDTO.AsObject> {

    const request = new IdSearchRequestDTO();
    request.setIdtosearch(query);

    return new Observable((observer: Observer<any>) => {
      this.searchClient.searchWithId(request, ((error, response: IdSearchResponseDTO) => {
        console.log(error, response);
        if (error) {
          observer.error(new Error(error.message));
        } else {
          console.log(response.toObject());
          const result = response.toObject();
          observer.next(result);
          observer.complete();
        }
      }));
    });
  }
}

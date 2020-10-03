import {environment} from '../environments/environment';
import {SearchClient} from './proto-gen/search_pb_service';

export const SearchClientFactory = () => {
  return new SearchClient(`${environment.backendUrl}`);
};

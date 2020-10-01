import {CrawlerClient} from './proto-gen/crawler_pb_service';
import {environment} from '../environments/environment';
import {DataHashClient} from './proto-gen/datahash_pb_service';

export const TestClientFactory = () => {
  return new DataHashClient(environment.backendUrl);
};

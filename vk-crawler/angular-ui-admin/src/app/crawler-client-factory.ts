import {CrawlerClient} from './proto-gen/crawler_pb_service';
import {environment} from '../environments/environment';

export const CrawlerClientFactory = () => {
  return new CrawlerClient(environment.backendUrl);
};

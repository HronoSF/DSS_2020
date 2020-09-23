import {environment} from '../environments/environment';

export const appURL = environment.appURL;
export const proxyURL = 'http://localhost:8010/proxy';
export const proxyForAPIRequest = 'http://localhost:8020/proxy';

export const vkAppID = '7591763';
export const vkAppSecret = 'se0LqkKZnvh7hhvyziW1';

export const vkAccessTokenURL =
  `https://oauth.vk.com/access_token?client_id=${vkAppID}&client_secret=${vkAppSecret}&redirect_uri=${appURL}`;
export const vkAccessTokenURLWithProxy =
  `${proxyURL}/access_token?client_id=${vkAppID}&client_secret=${vkAppSecret}&redirect_uri=${appURL}`;

export const vkOpenAuthDialogURL =
  `https://oauth.vk.com/authorize?client_id=${vkAppID}&display=popup&redirect_uri=${appURL}&response_type=code&v=5.122`;

export const vkAPIRequest = 'https://api.vk.com/method';

// export function vkApiCallTemplateUrl(method: string, params: { [key: string]: string}[]): string {
//   for (const key in params) {
//   }
//   return `METHOD_NAME?PARAMETERS&access_token=ACCESS_TOKEN&v=V`
// }

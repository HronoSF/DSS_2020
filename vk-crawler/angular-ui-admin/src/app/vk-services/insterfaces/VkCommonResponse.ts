export interface VkCommonResponse<T> {
  response: {
    count: number;
    items: T[];
  };
}



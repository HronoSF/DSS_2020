package com.hronosf.crawler.configuration;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class VkClientConfiguration {

    @Bean
    public VkApiClient vkApiClient() {
        TransportClient transportClient = new HttpTransportClient();
        return new VkApiClient(transportClient);
    }

    @Bean
    public ServiceActor serviceActor(@Value("${vk.app.key}") String key, @Value("${vk.app.id}") String id) {
        return new ServiceActor(Integer.parseInt(id), key);
    }
}

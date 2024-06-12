package com.ppalma.movies.config;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Value("${rest.movies.url-base}")
  private String baseUrl;

  @Bean
  public RestClient restClient() {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
    return RestClient.builder()
        .messageConverters(converters -> converters.add(converter))
        .baseUrl(this.baseUrl)
        .build();
  }

}

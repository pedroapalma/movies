package com.ppalma.movies.rest;

import com.ppalma.movies.entity.MoviesPagedEntity;
import com.ppalma.movies.exception.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class MoviesRestClient {

  private final RestClient restClient;

  @Value("${rest.movies.uri}")
  private String moviesUri;

  public MoviesRestClient(RestClient restClient) {
    this.restClient = restClient;
  }

  public MoviesPagedEntity findMoviesByPage(int page) {
    return this.restClient.get()
        .uri(uriBuilder -> uriBuilder
            .path(this.moviesUri)
            .queryParam("page", page)
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatusCode::isError, (request, response) -> {
          throw new InternalServerException(
              String.format("Status: %s, Headers: %s", response.getStatusCode(),
                  response.getHeaders()));
        })
        .body(MoviesPagedEntity.class);
  }

}

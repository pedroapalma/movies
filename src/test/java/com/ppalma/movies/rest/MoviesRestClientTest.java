package com.ppalma.movies.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ppalma.movies.entity.MoviesPagedEntity;
import com.ppalma.movies.exception.InternalServerException;
import com.ppalma.movies.utils.DummyData;
import java.io.IOException;
import java.util.function.Consumer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
class MoviesRestClientTest {

  private static final String MOVIES_URI = "/api/movies/search";

  private MockWebServer server;

  private MoviesRestClient moviesRestClient;

  @BeforeEach
  void setUp() {
    this.server = new MockWebServer();
    RestClient restClient = RestClient
        .builder()
        .requestFactory(new org.springframework.http.client.SimpleClientHttpRequestFactory())
        .baseUrl(this.server.url("/").toString())
        .build();
    this.moviesRestClient = new MoviesRestClient(restClient);
  }

  @AfterEach
  void shutdown() throws IOException {
    if (this.server != null) {
      this.server.shutdown();
    }
  }

  @Test
  void findMoviesSuccessfully() {
    ReflectionTestUtils.setField(this.moviesRestClient, "moviesUri", MOVIES_URI);
    String responseBody = DummyData.toJson(DummyData.MOVIES_PAGE_ENTITY_1);
    this.prepareResponse(response -> response.setResponseCode(HttpStatus.OK.value())
        .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .setBody(responseBody));
    MoviesPagedEntity expectedPagedMovies = DummyData.deserialize(DummyData.MOVIES_PAGE_ENTITY_1,
        MoviesPagedEntity.class);

    MoviesPagedEntity currentPagedMovies = this.moviesRestClient.findMoviesByPage(1);

    this.expectRequestCount(1);
    this.expectRequest(request -> {
      assertThat(request.getPath()).isEqualTo(MOVIES_URI + "?page=1");
      assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo("application/json");
    });
    assertThat(currentPagedMovies).isEqualTo(expectedPagedMovies);
  }

  @Test
  void returnInternalServerExceptionWhenApiFails() {
    ReflectionTestUtils.setField(this.moviesRestClient, "moviesUri", MOVIES_URI);
    String responseBody = DummyData.toJson(DummyData.MOVIES_PAGE_ENTITY_1);
    this.prepareResponse(
        response -> response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .setBody(responseBody));

    assertThatThrownBy(() -> this.moviesRestClient.findMoviesByPage(1)).isInstanceOf(
        InternalServerException.class);

    this.expectRequestCount(1);
    this.expectRequest(request -> {
      assertThat(request.getPath()).isEqualTo(MOVIES_URI + "?page=1");
      assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo("application/json");
    });
  }

  private void prepareResponse(Consumer<MockResponse> consumer) {
    MockResponse response = new MockResponse();
    consumer.accept(response);
    this.server.enqueue(response);
  }

  private void expectRequest(Consumer<RecordedRequest> consumer) {
    try {
      consumer.accept(this.server.takeRequest());
    } catch (InterruptedException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private void expectRequestCount(int count) {
    assertThat(this.server.getRequestCount()).isEqualTo(count);
  }
}
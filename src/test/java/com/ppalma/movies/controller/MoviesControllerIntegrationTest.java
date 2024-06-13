package com.ppalma.movies.controller;

import static com.ppalma.movies.utils.DummyData.MOVIES_PAGE_ENTITY_1;
import static com.ppalma.movies.utils.DummyData.MOVIES_PAGE_ENTITY_2;
import static com.ppalma.movies.utils.DummyData.MOVIES_PAGE_ENTITY_3;
import static com.ppalma.movies.utils.DummyData.deserialize;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppalma.movies.entity.MoviesPagedEntity;
import com.ppalma.movies.exception.InternalServerException;
import com.ppalma.movies.repository.MoviesRepository;
import com.ppalma.movies.rest.MoviesRestClient;
import com.ppalma.movies.service.MoviesServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MoviesControllerIntegrationTest {

  public static final Integer THRESHOLD_4 = 4;

  public static final String DIRECTORS_URI = "/api/directors?threshold=%s";

  private static final int PAGE_1 = 1;

  private static final int PAGE_2 = 2;

  private static final int PAGE_3 = 3;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MoviesRestClient moviesRestClient;

  @SpyBean
  private MoviesRepository moviesRepository;

  @SpyBean
  private MoviesServices moviesServices;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void returnOkAndGetDirectorsSuccessfully() throws Exception {
    MoviesPagedEntity moviesPaged1 = deserialize(MOVIES_PAGE_ENTITY_1, MoviesPagedEntity.class);
    MoviesPagedEntity moviesPaged2 = deserialize(MOVIES_PAGE_ENTITY_2, MoviesPagedEntity.class);
    MoviesPagedEntity moviesPaged3 = deserialize(MOVIES_PAGE_ENTITY_3, MoviesPagedEntity.class);
    when(this.moviesRestClient.findMoviesByPage(PAGE_1)).thenReturn(moviesPaged1);
    when(this.moviesRestClient.findMoviesByPage(PAGE_2)).thenReturn(moviesPaged2);
    when(this.moviesRestClient.findMoviesByPage(PAGE_3)).thenReturn(moviesPaged3);

    this.mockMvc.perform(get(String.format(DIRECTORS_URI, THRESHOLD_4))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("[\"Martin Scorsese\",\"Woody Allen\"]")));

    verify(this.moviesServices, times(1)).getDirectors(THRESHOLD_4);
    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_1);
    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_2);
    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_3);
    verify(this.moviesRestClient, times(1)).findMoviesByPage(PAGE_1);
    verify(this.moviesRestClient, times(1)).findMoviesByPage(PAGE_2);
    verify(this.moviesRestClient, times(1)).findMoviesByPage(PAGE_3);
  }

  @Test
  void returnNotFoundWhenMoviesPaged1IsNull() throws Exception {
    when(this.moviesRestClient.findMoviesByPage(PAGE_1)).thenReturn(null);

    this.mockMvc.perform(get(String.format(DIRECTORS_URI, THRESHOLD_4))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("The movies does not exist")));

    verify(this.moviesServices, times(1)).getDirectors(THRESHOLD_4);
    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_1);
    verify(this.moviesRepository, times(0)).getMoviesByPage(PAGE_2);
    verify(this.moviesRepository, times(0)).getMoviesByPage(PAGE_3);
    verify(this.moviesRestClient, times(1)).findMoviesByPage(PAGE_1);
    verify(this.moviesRestClient, times(0)).findMoviesByPage(PAGE_2);
    verify(this.moviesRestClient, times(0)).findMoviesByPage(PAGE_3);
  }

  @Test
  void returnInternalServerErrorWhenApiFails() throws Exception {
    when(this.moviesRestClient.findMoviesByPage(PAGE_1)).thenThrow(InternalServerException.class);

    this.mockMvc.perform(get(String.format(DIRECTORS_URI, THRESHOLD_4))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Unexpected Error")));

    verify(this.moviesServices, times(1)).getDirectors(THRESHOLD_4);
    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_1);
    verify(this.moviesRepository, times(0)).getMoviesByPage(PAGE_2);
    verify(this.moviesRepository, times(0)).getMoviesByPage(PAGE_3);
    verify(this.moviesRestClient, times(1)).findMoviesByPage(PAGE_1);
    verify(this.moviesRestClient, times(0)).findMoviesByPage(PAGE_2);
    verify(this.moviesRestClient, times(0)).findMoviesByPage(PAGE_3);
  }

  @Test
  void returnBadRequestWhenInputIsNotValid() throws Exception {
    this.mockMvc.perform(get(String.format(DIRECTORS_URI, "WrongThreshold"))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

}
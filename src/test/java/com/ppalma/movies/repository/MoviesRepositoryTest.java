package com.ppalma.movies.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ppalma.movies.entity.MoviesPagedEntity;
import com.ppalma.movies.exception.InternalServerException;
import com.ppalma.movies.model.MoviesPaged;
import com.ppalma.movies.rest.MoviesRestClient;
import com.ppalma.movies.utils.DummyData;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoviesRepositoryTest {

  private static final int PAGE_1 = 1;

  @Mock
  private MoviesRestClient moviesRestClient;

  @InjectMocks
  private MoviesRepository moviesRepository;

  @Test
  void returnMoviesPagedSuccessfullyWhenPageIsOne() {
    MoviesPagedEntity moviesPagedEntity = DummyData.deserialize(DummyData.MOVIES_PAGE_ENTITY_1,
        MoviesPagedEntity.class);
    when(this.moviesRestClient.findMoviesByPage(PAGE_1)).thenReturn(moviesPagedEntity);
    MoviesPaged expectedMoviesPaged = DummyData.deserialize(DummyData.MOVIES_PAGE_1,
        MoviesPaged.class);

    Optional<MoviesPaged> optCurrentMoviesPaged = this.moviesRepository.getMoviesByPage(PAGE_1);

    verify(this.moviesRestClient, only()).findMoviesByPage(PAGE_1);
    assertThat(optCurrentMoviesPaged).isPresent();
    MoviesPaged currentMoviesPaged = optCurrentMoviesPaged.get();
    assertThat(currentMoviesPaged.getTotalPages()).isEqualTo(3);
    assertThat(currentMoviesPaged.getMovies()).isNotNull();
    assertThat(currentMoviesPaged).isEqualTo(expectedMoviesPaged);
  }

  @Test
  public void returnEmptyWhenPageIsOneAndApiResponseIsNull() {
    when(this.moviesRestClient.findMoviesByPage(PAGE_1)).thenReturn(null);

    Optional<MoviesPaged> optCurrentMoviesPaged = this.moviesRepository.getMoviesByPage(PAGE_1);

    verify(this.moviesRestClient, only()).findMoviesByPage(PAGE_1);
    assertThat(optCurrentMoviesPaged).isEmpty();
  }

  @Test
  void returnInternalServerExceptionWhenApiResponseIsError() {
    when(this.moviesRestClient.findMoviesByPage(PAGE_1)).thenThrow(InternalServerException.class);

    assertThatThrownBy(() -> this.moviesRepository.getMoviesByPage(PAGE_1)).isInstanceOf(
        InternalServerException.class);

    verify(this.moviesRestClient, only()).findMoviesByPage(PAGE_1);
  }
}
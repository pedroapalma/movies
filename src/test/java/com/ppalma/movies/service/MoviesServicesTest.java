package com.ppalma.movies.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ppalma.movies.exception.InternalServerException;
import com.ppalma.movies.exception.NotFoundException;
import com.ppalma.movies.model.MoviesPaged;
import com.ppalma.movies.repository.MoviesRepository;
import com.ppalma.movies.utils.DummyData;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoviesServicesTest {

  private static final int THRESHOLD_4 = 4;

  private static final int PAGE_1 = 1;

  private static final int PAGE_2 = 2;

  private static final int PAGE_3 = 3;

  @Mock
  private MoviesRepository moviesRepository;

  @InjectMocks
  private MoviesServices moviesServices;

  @Test
  void returnDirectorsSuccessfullyWhenThresholdIs4() {
    MoviesPaged moviesPaged1 = DummyData.deserialize(DummyData.MOVIES_PAGE_1, MoviesPaged.class);
    MoviesPaged moviesPaged2 = DummyData.deserialize(DummyData.MOVIES_PAGE_2, MoviesPaged.class);
    MoviesPaged moviesPaged3 = DummyData.deserialize(DummyData.MOVIES_PAGE_3, MoviesPaged.class);
    when(this.moviesRepository.getMoviesByPage(PAGE_1)).thenReturn(Optional.of(moviesPaged1));
    when(this.moviesRepository.getMoviesByPage(PAGE_2)).thenReturn(Optional.of(moviesPaged2));
    when(this.moviesRepository.getMoviesByPage(PAGE_3)).thenReturn(Optional.of(moviesPaged3));
    List<String> expectedDirectors = List.of("Martin Scorsese", "Woody Allen");

    List<String> currentDirectors = this.moviesServices.getDirectors(THRESHOLD_4);

    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_1);
    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_2);
    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_3);
    assertThat(currentDirectors).isEqualTo(expectedDirectors);
  }

  @Test
  void returnNotFoundExceptionWhenMoviesPaged1IsNull() {
    when(this.moviesRepository.getMoviesByPage(PAGE_1)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> this.moviesServices.getDirectors(THRESHOLD_4)).isInstanceOf(
        NotFoundException.class);

    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_1);
    verify(this.moviesRepository, times(0)).getMoviesByPage(PAGE_2);
    verify(this.moviesRepository, times(0)).getMoviesByPage(PAGE_3);
  }

  @Test
  void returnInternalServerExceptionWhenOneOfTheRemainingPagesIsEmpty() {
    MoviesPaged moviesPaged1 = DummyData.deserialize(DummyData.MOVIES_PAGE_1, MoviesPaged.class);
    when(this.moviesRepository.getMoviesByPage(PAGE_1)).thenReturn(Optional.of(moviesPaged1));
    when(this.moviesRepository.getMoviesByPage(PAGE_2)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> this.moviesServices.getDirectors(THRESHOLD_4)).isInstanceOf(
        InternalServerException.class);

    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_1);
    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_2);
    verify(this.moviesRepository, times(1)).getMoviesByPage(PAGE_3);
  }
}
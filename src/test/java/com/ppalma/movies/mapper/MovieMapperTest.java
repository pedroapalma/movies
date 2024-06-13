package com.ppalma.movies.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ppalma.movies.entity.MovieEntity;
import com.ppalma.movies.entity.MoviesPagedEntity;
import com.ppalma.movies.model.Movie;
import com.ppalma.movies.model.MoviesPaged;
import com.ppalma.movies.utils.DummyData;
import org.junit.jupiter.api.Test;

class MovieMapperTest {

  @Test
  public void returnMoviesPagedWhenMapMoviesPagedEntityToMoviesPaged() {
    MoviesPagedEntity moviesPagedEntity = DummyData.deserialize(DummyData.MOVIES_PAGE_ENTITY_1,
        MoviesPagedEntity.class);

    MoviesPaged moviesPaged = MovieMapper.toMoviesPaged(moviesPagedEntity);

    assertThat(moviesPaged).isNotNull();
    assertThat(moviesPaged.getTotalPages()).isEqualTo(3);
    assertThat(moviesPaged.getMovies()).isNotNull();
    assertThat(moviesPaged.getMovies()).isNotEmpty();
    assertThat(moviesPaged.getMovies()).hasSize(11);
  }

  @Test
  public void returnNullWhenMoviesPagedEntityIsNull() {
    MoviesPaged moviesPaged = MovieMapper.toMoviesPaged(null);

    assertThat(moviesPaged).isNull();
  }

  @Test
  public void returnMovieWhenMapMovieEntityToMovie() {
    MovieEntity movieEntity = DummyData.deserialize(DummyData.MOVIE, MovieEntity.class);

    Movie movie = MovieMapper.toMovie(movieEntity);

    assertThat(movie).isNotNull();
    assertThat(movie.getTitle()).isEqualTo("Midnight in Paris");
    assertThat(movie.getYear()).isEqualTo("2011");
    assertThat(movie.getRated()).isEqualTo("PG-13");
    assertThat(movie.getReleased()).isEqualTo("10 Jun 2011");
    assertThat(movie.getRuntime()).isEqualTo("94 min");
    assertThat(movie.getGenre()).isEqualTo("Comedy, Fantasy, Romance");
    assertThat(movie.getDirector()).isEqualTo("Woody Allen");
    assertThat(movie.getWriter()).isEqualTo("Woody Allen");
    assertThat(movie.getActors()).isEqualTo("Owen Wilson, Rachel McAdams, Kathy Bates");
  }

  @Test
  public void returnNullWhenMovieEntityIsNull() {
    Movie movie = MovieMapper.toMovie(null);

    assertThat(movie).isNull();
  }

}
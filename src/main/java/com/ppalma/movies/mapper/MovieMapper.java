package com.ppalma.movies.mapper;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import com.ppalma.movies.entity.MovieEntity;
import com.ppalma.movies.entity.MoviesPagedEntity;
import com.ppalma.movies.model.Movie;
import com.ppalma.movies.model.MoviesPaged;

public interface MovieMapper {

  static MoviesPaged toMoviesPaged(MoviesPagedEntity moviesPagedEntity) {
    if (moviesPagedEntity == null) {
      return null;
    }

    return MoviesPaged.builder()
        .totalPages(moviesPagedEntity.getTotalPages())
        .movies(
            emptyIfNull(moviesPagedEntity.getMovies()).stream().map(MovieMapper::toMovie).toList())
        .build();
  }

  static Movie toMovie(MovieEntity movieEntity) {
    if (movieEntity == null) {
      return null;
    }

    return Movie.builder()
        .title(movieEntity.getTitle())
        .year(movieEntity.getYear())
        .rated(movieEntity.getRated())
        .released(movieEntity.getReleased())
        .runtime(movieEntity.getRuntime())
        .genre(movieEntity.getGenre())
        .director(movieEntity.getDirector())
        .writer(movieEntity.getWriter())
        .actors(movieEntity.getActors())
        .build();
  }

}

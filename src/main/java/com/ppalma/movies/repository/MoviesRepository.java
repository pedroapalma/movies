package com.ppalma.movies.repository;

import com.ppalma.movies.mapper.MovieMapper;
import com.ppalma.movies.model.MoviesPaged;
import com.ppalma.movies.rest.MoviesRestClient;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MoviesRepository {

  private final MoviesRestClient moviesRestClient;

  public Optional<MoviesPaged> getPagedMovies(int page) {
    return Optional.ofNullable(this.moviesRestClient.findMovies(page))
        .map(MovieMapper::toMoviesPaged);
  }
}

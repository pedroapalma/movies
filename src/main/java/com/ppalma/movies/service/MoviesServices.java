package com.ppalma.movies.service;

import com.ppalma.movies.exception.InternalServerException;
import com.ppalma.movies.exception.NotFoundException;
import com.ppalma.movies.model.Movie;
import com.ppalma.movies.model.MoviesPaged;
import com.ppalma.movies.repository.MoviesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoviesServices {

  private final MoviesRepository moviesRepository;

  @Cacheable(value = "getDirectors", key = "#threshold")
  public List<String> getDirectors(int threshold) {
    List<Movie> movies = this.getAllMovies();

    Map<String, Long> directorMoviesCountMap = movies.stream()
        .collect(Collectors.groupingBy(Movie::getDirector, Collectors.counting()));

    return directorMoviesCountMap.entrySet().stream()
        .filter(entry -> entry.getValue() > threshold)
        .map(Map.Entry::getKey)
        .sorted()
        .collect(Collectors.toList());
  }

  private List<Movie> getAllMovies() {
    MoviesPaged firstPageMovies = this.moviesRepository.getMoviesByPage(1)
        .orElseThrow(() -> new NotFoundException("The movies does not exist"));

    List<Movie> remainingMovies = this.getMoviesFromRemainingPages(firstPageMovies.getTotalPages());

    return Stream.of(firstPageMovies.getMovies(), remainingMovies)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private List<Movie> getMoviesFromRemainingPages(int totalPages) {
    List<CompletableFuture<MoviesPaged>> futures = new ArrayList<>();
    for (int page = 2; page <= totalPages; page++) {
      CompletableFuture<MoviesPaged> future = this.getAsyncPagedMoviesFuture(page);
      futures.add(future);
    }

    CompletableFuture<Void> allOf = CompletableFuture.allOf(
        futures.toArray(new CompletableFuture[0]));

    List<Movie> movies;
    try {
      movies = this.joinMoviesFromPages(allOf, futures);
    } catch (InterruptedException | ExecutionException e) {
      throw new InternalServerException("Error getting movies");
    }

    return movies;
  }

  private CompletableFuture<MoviesPaged> getAsyncPagedMoviesFuture(int page) {
    return CompletableFuture.supplyAsync(
        () -> this.moviesRepository.getMoviesByPage(page)
            .orElseThrow(() -> new InternalServerException("Error getting remaining pages")));
  }

  private List<Movie> joinMoviesFromPages(CompletableFuture<Void> allOf,
      List<CompletableFuture<MoviesPaged>> futures)
      throws InterruptedException, ExecutionException {
    return allOf.thenApply(v -> futures.stream()
            .map(CompletableFuture::join)
            .map(MoviesPaged::getMovies)
            .flatMap(List::stream)
            .toList())
        .get();
  }

}

package com.ppalma.movies.controller;

import com.ppalma.movies.model.Movie;
import com.ppalma.movies.service.MoviesServices;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class MoviesController {

  private final MoviesServices moviesServices;

  @GetMapping("/directors")
  public List<String> getDirectors(
      @RequestParam(name = "threshold", required = false) Integer threshold) {
    return this.moviesServices.getDirectors(threshold);
  }

  @GetMapping("/movies")
  public List<Movie> getMovies()
      throws ExecutionException, InterruptedException {
    return this.moviesServices.getAllMovies();
  }
}

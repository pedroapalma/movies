package com.ppalma.movies.controller;

import com.ppalma.movies.service.MoviesServices;
import jakarta.validation.Valid;
import java.util.List;
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
  public List<String> getDirectors(@Valid @RequestParam(name = "threshold") Integer threshold) {
    return this.moviesServices.getDirectors(threshold);
  }

}

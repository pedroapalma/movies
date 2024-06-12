package com.ppalma.movies.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

  private String title;

  private String year;

  private String rated;

  private String released;

  private String runtime;

  private String genre;

  private String director;

  private String writer;

  private String actors;

}

package com.ppalma.movies.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoviesPaged {

  private Integer page;

  private Integer perPage;

  private Integer total;

  private Integer totalPages;

  private List<Movie> movies;

}

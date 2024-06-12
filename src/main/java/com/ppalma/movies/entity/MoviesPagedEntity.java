package com.ppalma.movies.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoviesPagedEntity {

  @JsonProperty("page")
  private Integer page;

  @JsonProperty("per_page")
  private Integer perPage;

  @JsonProperty("total")
  private Integer total;

  @JsonProperty("total_pages")
  private Integer totalPages;

  @JsonProperty("data")
  private List<MovieEntity> movies;

}

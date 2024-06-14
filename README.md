# Movies Api
![Coverage](.github/badges/jacoco.svg)

This project implements a RESTful API, given the threshold value, the goal is to use the API to get the list of the names of the directors with most movies directed. Specifically, the list of names of directors with movie count strictly greater than the given threshold.
The list of names must be returned in alphabetical order.

To access the collection of users perform HTTP GET request to: https://directa24-movies.wiremockapi.cloud/api/movies/search?page= where is an integer denoting the page of the results to return.

For example, GET request to: https://directa24-movies.wiremockapi.cloud/api/movies/search?page=2 will return the second page of the collection of movies. Pages are numbered from 1, so in order to access the first page, you need to ask for page number 1. The response to such request is a JSON with the following 5 fields:

* **page:** The current page of the results
* **per_page:** The maximum number of movies returned per page.
* **total:** The total number of movies on all pages of the result.
* **total_pages:** The total number of pages with results.
* **data:** An array of objects containing movies returned on the requested page

Each movie record has the following schema:

* **Title:** title of the movie
* **Year:** year the movie was released
* **Rated:** movie rating
* **Released:** movie release date
* **Runtime:** movie duration time in minutes
* **Genre:** move genre
* **Director:** movie director
* **Writer:** movie writers
* **Actors:** movie actors

# Technologies Used

- Java
- Spring Boot
- Redis
- Docker

# Getting Started

## Prerequisites

- Java
- Maven
- Docker

## Installing

- Deploy redis container using `docker-compose up -d` with de the file [docker-compose.yml](src/main/resources/docker/docker-compose.yml)
- Clone the repository.
- Build the project using Maven: `mvn clean package`
- Run the application: `java -jar target/movies-0.0.1-SNAPSHOT.jar`
- Access the API endpoints using your preferred HTTP client.

# API

## Endpoint

- `GET /api/directors?threshold={threshold}`: Retrieve the list of names of directors with movie count strictly greater than the given threshold

## Documentation

http://localhost:8080/swagger-ui/index.html

## Example

### Request

`GET` http://localhost:8080/api/directors?threshold=4

### Response

`OK`


```
[
    "Martin Scorsese",
    "Woody Allen"
]
```
package ru.example.movieservice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.example.movieservice.model.Movie;
import ru.example.movieservice.repository.MovieRepository;

@RestController
@RequestMapping("/")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @MessageMapping("request-response")
    Mono<Movie> getMovieByName(Movie movie) {
        return movieRepository.findById(movie.getId());
    }

    @MessageMapping("request-stream")
    Flux<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @MessageMapping("fire-forget")
    Mono<Void> addMovie(Movie movie) {
        return movieRepository.save(movie).then();
    }

    @MessageMapping("channel")
    Flux<Movie> channelMovies(Flux<String> ids) {
        return ids.doOnNext(id -> System.out.println("Received id: " + id))
                .flatMap(movieRepository::findById);
    }
}

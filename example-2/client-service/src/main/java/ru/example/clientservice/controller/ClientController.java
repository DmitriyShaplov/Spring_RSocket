package ru.example.clientservice.controller;

import io.rsocket.util.DefaultPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.example.clientservice.model.Movie;
import ru.example.clientservice.model.RequestMovie;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClientController {

    private final RSocketRequester requester;

    @GetMapping("/movie/{id}")
    public Mono<Movie> findMovieById(@PathVariable String id) {
        return this.requester
                .route("request-response")
//                .data(DefaultPayload.create(""))
                .data(new RequestMovie(id))
                .retrieveMono(Movie.class);
    }

    @GetMapping("/showAllMovies")
    public Flux<Movie> findAllMovies() {
        return this.requester
                .route("request-stream")
                .retrieveFlux(Movie.class);
    }

    @PostMapping("/addMovie/{id}/{name}/{price}")
    public Mono<Void> addMovie(@PathVariable String id,
                        @PathVariable String name,
                        @PathVariable String price) {
        return this.requester
                .route("fire-forget")
                .data(new Movie(id, name, price))
                .send();
    }

    @GetMapping(value = "/channel", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Movie> getMoviesIterated() {
        return requester
                .route("channel")
                .data(Flux.range(1, 5).map(String::valueOf).delayElements(Duration.ofSeconds(2)))
                .retrieveFlux(Movie.class);
    }
}

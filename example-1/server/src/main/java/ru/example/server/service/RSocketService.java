package ru.example.server.service;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class RSocketService extends AbstractRSocket {

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        System.out.println("fire-and-forget: server received");
        return Mono.empty();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        log.info(payload.getDataUtf8());
        return Mono.just(DefaultPayload.create("Connection Successful"));
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        log.info(payload.getDataUtf8());
        return Flux.range(1, 5)
                .map(i -> DefaultPayload.create("request-stream " + i));
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return Flux.from(payloads).map(Payload::getDataUtf8)
                .doOnNext(str -> log.info("Received: " + str))
                .map(DefaultPayload::create);
    }
}

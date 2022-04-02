package ru.example.server.config;

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import ru.example.server.service.RSocketService;

import javax.annotation.PostConstruct;

@Configuration
public class RSocketConfig {

    @PostConstruct
    public void startServer() {
        RSocketFactory.receive()
                .acceptor((setupPayload, reactiveSocket) ->
                                Mono.just(new RSocketService()))
                .transport(TcpServerTransport.create(8000))
                .start()
                .block()
                .onClose()
                .block();
    }
}

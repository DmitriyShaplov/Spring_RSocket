package ru.example.clientservice.config;

import io.rsocket.RSocket;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Slf4j
@Configuration
public class RSocketConfiguration {

    @Bean
    RSocketRequester rSocketRequester(RSocketStrategies strategies) {
        InetSocketAddress address = new InetSocketAddress("localhost", 7000);
        return RSocketRequester.builder()
                .rsocketFactory(factory -> factory
                        .dataMimeType(MimeTypeUtils.ALL_VALUE)
                        .frameDecoder(PayloadDecoder.ZERO_COPY))
                .rsocketStrategies(strategies)
                .connect(TcpClientTransport.create(address))
                .retry()
                .block();
    }

//    @Bean
//    public Mono<RSocketRequester> rSocketRequester(Mono<RSocket> rSocket, RSocketStrategies strategies) {
//        return rSocket
//                .map(socket -> RSocketRequester.wrap(socket, MimeTypeUtils.ALL, MimeTypeUtils.parseMimeType("application/cbor"), strategies))
//                .cache();
//    }
}

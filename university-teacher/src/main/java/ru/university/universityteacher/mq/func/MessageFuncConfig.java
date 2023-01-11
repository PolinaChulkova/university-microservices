package ru.university.universityteacher.mq.func;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;
import ru.university.universityteacher.dto.TaskNotificationDTO;

import java.util.function.Supplier;

@Configuration
@Getter
public class MessageFuncConfig {
    private Sinks.Many<Message<TaskNotificationDTO>> innerBus =
            Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    @Bean
    public Supplier<Flux<Message<TaskNotificationDTO>>> newTaskProduce() {
        return () -> innerBus.asFlux();
    }
}

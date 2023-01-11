package ru.university.universityteacher.mq.func;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;
import ru.university.universityteacher.dto.TaskNotificationDTO;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageFuncConfig messageFuncConfig;

    public void createNewTaskMessage(TaskNotificationDTO taskNotificationDTO) {
        messageFuncConfig.getInnerBus().emitNext(MessageBuilder.withPayload(taskNotificationDTO).build(),
                Sinks.EmitFailureHandler.FAIL_FAST);
    }
}

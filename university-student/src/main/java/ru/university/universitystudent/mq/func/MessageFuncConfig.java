package ru.university.universitystudent.mq.func;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import ru.university.universitystudent.dto.TaskNotificationDTO;
import ru.university.universitystudent.service.GroupService;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class MessageFuncConfig {
    private final GroupService groupService;

    @Bean
    public Consumer<Message<TaskNotificationDTO>> addNewTaskInGroup() {
        return message -> groupService.addTaskToGroup(
                message.getPayload().getGroupId(),
                message.getPayload().getTaskId());
    }
}

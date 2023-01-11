package ru.university.universityteacher.mq.legacy;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.university.universityteacher.dto.TaskNotificationDTO;

//@Component
//@EnableBinding(UniversityBinding.class)
//@RequiredArgsConstructor
//public class MessageProducer {
//    private final UniversityBinding universityBinding;
//
//    public void createNewTaskMessage(TaskNotificationDTO taskNotificationDTO) {
//        Message<TaskNotificationDTO> message = MessageBuilder.withPayload(taskNotificationDTO).build();
//        universityBinding.getOutputChannel().send(message);
//    }
//}

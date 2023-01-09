package ru.university.universitystudent.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import ru.university.universitystudent.dto.TaskNotificationDTO;
import ru.university.universitystudent.service.GroupService;

@Component
@EnableBinding(UniversityBinding.class)
@RequiredArgsConstructor
public class MessageConsumer {
    private final GroupService groupService;

    @StreamListener(target = UniversityBinding.INPUT_CHANNEL)
    public void addNewTaskInGroup(TaskNotificationDTO taskNotificationDTO) {
        groupService.addTaskToGroup(taskNotificationDTO.getGroupId(), taskNotificationDTO.getTaskId());
    }
}

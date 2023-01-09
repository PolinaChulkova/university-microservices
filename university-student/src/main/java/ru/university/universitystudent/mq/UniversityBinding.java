package ru.university.universitystudent.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface UniversityBinding {
    String INPUT_CHANNEL = "universityInputChannel";

    @Input(INPUT_CHANNEL)
    MessageChannel getInputChannel();
}

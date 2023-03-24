package hu.martin.chatter.application.time;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public interface TimeProvider {
    LocalDateTime now();
}

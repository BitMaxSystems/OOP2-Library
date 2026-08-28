package org.bitmaxsystems.oop2library.models.dto;

import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import java.time.LocalDateTime;

public class NotificationDTO {
    private final String title;
    private final String message;
    private final NotificationType type;
    private final LocalDateTime timestamp;

    public NotificationDTO(String title, String message, NotificationType type, LocalDateTime timestamp) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
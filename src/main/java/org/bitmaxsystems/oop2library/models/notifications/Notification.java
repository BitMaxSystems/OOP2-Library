package org.bitmaxsystems.oop2library.models.notifications;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationAudience audience;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    protected Notification() {
    }

    public Notification(String title, String message, NotificationType type, NotificationAudience audience) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.audience = audience;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
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

    public NotificationAudience getAudience() {
        return audience;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
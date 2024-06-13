package ru.adel.locationtracker.core.service.notification.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.adel.locationtracker.core.service.notification.db.entity.enums.InteractionStatus;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "incident_user_interaction")
public class IncidentUserInteraction {

    @EmbeddedId
    private IncidentUserInteractionId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InteractionStatus status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


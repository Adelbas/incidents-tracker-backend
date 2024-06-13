package ru.adel.locationtracker.core.service.notification.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class IncidentUserInteractionId implements Serializable {

    @Column(name = "incident_id")
    private Long incidentId;

    @Column(name = "incident_date")
    private LocalDate incidentDate;

    @Column(name = "user_id")
    private UUID userId;
}

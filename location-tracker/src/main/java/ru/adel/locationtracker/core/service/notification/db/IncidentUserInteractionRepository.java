package ru.adel.locationtracker.core.service.notification.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.adel.locationtracker.core.service.notification.db.entity.IncidentUserInteraction;
import ru.adel.locationtracker.core.service.notification.db.entity.IncidentUserInteractionId;

@Repository
public interface IncidentUserInteractionRepository extends JpaRepository<IncidentUserInteraction, IncidentUserInteractionId> {

}

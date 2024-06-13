package ru.adel.locationtracker.core.service.notification.db;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.notification.db.entity.enums.InteractionStatus;
import ru.adel.locationtracker.public_interface.event.dto.IncidentNotificationDto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {

    private static final String INSERT_STATEMENT = """
            INSERT INTO incident_user_interaction (incident_id, incident_date, user_id, status)
            VALUES (?, ?, ?, ?)
            ON CONFLICT (incident_id, incident_date, user_id) DO NOTHING""";

    private final JdbcTemplate jdbcTemplate;

    @Value("${jdbc.batch-size}")
    private Integer BATCH_SIZE;

    @Transactional
    public void batchInsert(List<UUID> users, Long incidentId, LocalDate incidentDate, InteractionStatus status) {
        try {
            jdbcTemplate.batchUpdate(INSERT_STATEMENT, users, BATCH_SIZE, (PreparedStatement ps, UUID id) -> {
                ps.setLong(1, incidentId);
                ps.setDate(2, Date.valueOf(incidentDate));
                ps.setObject(3, id);
                ps.setString(4, status.name());
            });
        } catch (Exception e) {
            log.error("Exception during batch inserting: {}", e.getMessage());
        }
    }

    @Transactional
    public void batchInsert(UUID userId, List<IncidentNotificationDto> incidents, InteractionStatus status) {
        try {
            jdbcTemplate.batchUpdate(
                    INSERT_STATEMENT, incidents, BATCH_SIZE,
                    (PreparedStatement ps, IncidentNotificationDto incident) -> {
                        ps.setLong(1, incident.id());
                        ps.setDate(2, Date.valueOf(incident.createdAt().toLocalDate()));
                        ps.setObject(3, userId);
                        ps.setString(4, status.name());
                    }
            );
        } catch (Exception e) {
            log.error("Exception during batch inserting: {}", e.getMessage());
        }
    }
}

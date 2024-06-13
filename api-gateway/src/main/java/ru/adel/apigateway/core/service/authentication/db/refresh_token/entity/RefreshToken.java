package ru.adel.apigateway.core.service.authentication.db.refresh_token.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.adel.apigateway.core.service.authentication.db.user.entity.User;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "issued_at", nullable = false)
    private Date issuedAt;

    @Column(name = "expired_at", nullable = false)
    private Date expiredAt;
}

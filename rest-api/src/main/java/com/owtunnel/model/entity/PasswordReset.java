package com.owtunnel.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "password_resets")
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_password_resets_user"))
    private User user;

    @Column(name = "reset_token", nullable = false, columnDefinition = "TEXT")
    private String resetToken;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    private void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.expiresAt == null) this.expiresAt = LocalDateTime.now().plusMinutes(30);
    }
}

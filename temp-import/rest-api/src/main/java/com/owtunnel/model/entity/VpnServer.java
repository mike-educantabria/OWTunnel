package com.owtunnel.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "vpn_servers")
public class VpnServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "country", nullable = false, length = 45)
    private String country;

    @Column(name = "city", nullable = false, length = 45)
    private String city;

    @Column(name = "hostname", nullable = false, length = 45)
    private String hostname;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(name = "config_file_url", nullable = false, columnDefinition = "TEXT")
    private String configFileUrl;

    @Column(name = "is_free", nullable = false)
    private Boolean isFree;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        if (this.isFree == null) this.isFree = false;
        if (this.isActive == null) this.isActive = true;
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.updatedAt == null) this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

package com.owtunnel.repository;

import com.owtunnel.model.entity.VpnServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VpnServerRepository extends JpaRepository<VpnServer, Long> {
}

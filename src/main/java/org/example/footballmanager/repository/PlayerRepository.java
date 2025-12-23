package org.example.footballmanager.repository;

import org.example.footballmanager.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Page<Player> findAllByTeam_Id(Long teamId, Pageable pageable);
}
package org.example.footballmanager.repository;

import org.example.footballmanager.entity.PlayerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    Page<PlayerEntity> findAllByTeam_Id(Long teamId, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update PlayerEntity p
        set p.team = null
        where p.team.id = :teamId
    """)
    int detachPlayersFromTeam(@Param("teamId") Long teamId);
}
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
    Page<PlayerEntity> findAll(Pageable pageable);
    Page<PlayerEntity> findAllByTeam_Id(Long teamId, Pageable pageable);

    @Query("""
        SELECT p
        FROM PlayerEntity p
        WHERE (:teamId IS NULL OR p.team.id = :teamId)
          AND LOWER(p.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
        ORDER BY p.fullName
    """)
    Page<PlayerEntity> searchPlayers(
            @Param("teamId") Long teamId,
            @Param("search") String search,
            Pageable pageable
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update PlayerEntity p
        set p.team = null
        where p.team.id = :teamId
    """)
    int detachPlayersFromTeam(@Param("teamId") Long teamId);
}
package org.example.footballmanager.repository;

import org.example.footballmanager.entity.MatchStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchStatsRepository extends JpaRepository<MatchStatsEntity, Long> {
    void deleteByMatchId(Long matchId);

    @Query("""
       SELECT DISTINCT ms.match.id
       FROM MatchStatsEntity ms
       WHERE ms.team.id = :teamId
       """)
    List<Long> findMatchIdsByTeamId(Long teamId);

    @Modifying
    @Query("DELETE FROM MatchStatsEntity ms WHERE ms.match.id IN :matchIds")
    void deleteStatsByMatchIds(List<Long> matchIds);
}

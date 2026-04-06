package org.example.footballmanager.repository;

import org.example.footballmanager.entity.MatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    @Query("SELECT DISTINCT m.season FROM MatchEntity m")
    Page<Integer> findDistinctSeasons(Pageable pageable);
    List<MatchEntity> findAllBySeasonAndIsFinishedTrue(Integer season);

    Page<MatchEntity> findByIsFinished(boolean isFinished, Pageable pageable);

    @Query(
            value = """
    SELECT DISTINCT m
    FROM MatchEntity m
    JOIN m.stats s
    JOIN s.team t
    WHERE (:isFinished IS NULL OR m.isFinished = :isFinished)
      AND (
          :search IS NULL OR :search = '' OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))
      )
    ORDER BY m.matchDateTime DESC
""",
            countQuery = """
    SELECT COUNT(DISTINCT m.id)
    FROM MatchEntity m
    JOIN m.stats s
    JOIN s.team t
    WHERE (:isFinished IS NULL OR m.isFinished = :isFinished)
      AND (
          :search IS NULL OR :search = '' OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))
      )
"""
    )
    Page<MatchEntity> searchMatches(
            @Param("search") String search,
            @Param("isFinished") Boolean isFinished,
            Pageable pageable
    );

    @Modifying
    @Query("DELETE FROM MatchEntity m WHERE m.id IN :matchIds")
    void deleteMatchesByIds(List<Long> matchIds);
}
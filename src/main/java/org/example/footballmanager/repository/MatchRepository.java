package org.example.footballmanager.repository;

import org.example.footballmanager.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    List<MatchEntity> findAllBySeason(Integer season);

    @Modifying
    @Query("DELETE FROM MatchEntity m WHERE m.id IN :matchIds")
    void deleteMatchesByIds(List<Long> matchIds);
}
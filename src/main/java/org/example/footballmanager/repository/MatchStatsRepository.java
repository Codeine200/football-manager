package org.example.footballmanager.repository;

import org.example.footballmanager.entity.MatchStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchStatsRepository extends JpaRepository<MatchStatsEntity, Long> {

}

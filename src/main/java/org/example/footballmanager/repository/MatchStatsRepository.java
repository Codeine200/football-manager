package org.example.footballmanager.repository;

import org.example.footballmanager.entity.MatchStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchStatsRepository extends JpaRepository<MatchStats, Long> {

}

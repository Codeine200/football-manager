package org.example.footballmanager.repository;

import org.example.footballmanager.entity.TeamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    @Query("""
        SELECT t
        FROM TeamEntity t
        WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<TeamEntity> searchTeams(
            @Param("search") String search,
            Pageable pageable
    );
}

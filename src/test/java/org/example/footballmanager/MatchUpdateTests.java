package org.example.footballmanager;

import jakarta.transaction.Transactional;
import org.example.footballmanager.domain.MatchFullInfo;
import org.example.footballmanager.domain.TeamFullInfo;
import org.example.footballmanager.domain.TeamId;
import org.example.footballmanager.entity.MatchEntity;
import org.example.footballmanager.repository.MatchRepository;
import org.example.footballmanager.service.MatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Testcontainers
@Sql(scripts = "/matches.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class MatchUpdateTests {

    @Container
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("football_test")
                    .withUsername("test")
                    .withPassword("test");

    static {
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchRepository matchRepository;

    @Test
    @Transactional
    void shouldUpdateMatchWhenMatchIsNotFinished() {
        MatchFullInfo update = MatchFullInfo.builder()
                .season(2026)
                .matchDateTime(LocalDateTime.of(2026,1,1,15,0))
                .isFinished(true)
                .team1(new TeamFullInfo(new TeamId(1L), 2, false))
                .team2(new TeamFullInfo(new TeamId(2L), 5, true))
                .build();

        long matchId = 1L;
        matchService.updateMatch(matchId, update);

        MatchEntity updated = matchRepository.findById(matchId).orElseThrow();

        assertEquals(2026, updated.getSeason());
        assertEquals(false, updated.getStats().get(0).getIsWinner());
        assertEquals(2, updated.getStats().get(0).getGoals());
        assertEquals(true, updated.getStats().get(1).getIsWinner());
        assertEquals(5, updated.getStats().get(1).getGoals());
    }

    @Test
    @Transactional
    void shouldUpdateMatchWhenMatchIsFinished() {
        MatchFullInfo update = MatchFullInfo.builder()
                .season(2026)
                .matchDateTime(LocalDateTime.of(2026,1,1,15,0))
                .team1(new TeamFullInfo(new TeamId(3L), 3, true))
                .team2(new TeamFullInfo(new TeamId(4L), 2, false))
                .isFinished(false)
                .build();

        long matchId = 1L;
        matchService.updateMatch(matchId, update);

        MatchEntity updated = matchRepository.findById(matchId).orElseThrow();

        assertEquals(2026, updated.getSeason());
        assertNull(updated.getStats().get(0).getScore());
        assertNull(updated.getStats().get(0).getGoals());
        assertNull(updated.getStats().get(0).getIsWinner());
        assertFalse(updated.isFinished());
    }
}
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

import java.time.LocalDate;

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
        registry.add("spring.datasource.embedded-database-connection", () -> "NONE");
    }

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchRepository matchRepository;


//    @Test
//    void shouldUpdateMatchWhenMatchIsNotFinished() {
//        MatchFullInfo update = MatchFullInfo.builder()
//                .season(2026L)
//                .matchDate(LocalDate.of(2026,1,1))
//                .isFinished(false)
//                .build();
//
//        long matchId = 1L;
//        matchService.updateMatch(matchId, update);
//    }

    @Test
    @Transactional
    void shouldUpdateMatchWhenMatchIsFinished() {
        MatchFullInfo update = MatchFullInfo.builder()
                .season(2026)
                .matchDate(LocalDate.of(2026,1,1))
                .team1(new TeamFullInfo(new TeamId(3L), 3, true))
                .team2(new TeamFullInfo(new TeamId(4L), 2, false))
                .isFinished(false)
                .build();

        long matchId = 1L;
        matchService.updateMatch(matchId, update);

        MatchEntity updated = matchRepository.findById(matchId).orElseThrow();

        assertEquals(2026, updated.getSeason());
        assertNull(updated.getStats().getFirst().getScore());
        assertNull(updated.getStats().getFirst().getGoals());
        assertNull(updated.getStats().getFirst().getIsWinner());
        assertFalse(updated.isFinished());
    }
}

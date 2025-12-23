package org.example.footballmanager;

import jakarta.transaction.Transactional;
import org.example.footballmanager.dto.request.MatchFinishRequestDto;
import org.example.footballmanager.entity.Match;
import org.example.footballmanager.entity.MatchStats;
import org.example.footballmanager.entity.Team;
import org.example.footballmanager.repository.MatchRepository;
import org.example.footballmanager.repository.TeamRepository;
import org.example.footballmanager.service.MatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@SpringBootTest
@Testcontainers
public class FinishMatchTests {

    @Container
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("football_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);
    }

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    @Transactional
    void shouldFinishMatchAndPersistStats() {
        Team team1 = teamRepository.save(Team.builder().name("Team A").build());
        Team team2 = teamRepository.save(Team.builder().name("Team B").build());

        Match match = Match.builder()
                .season("2025")
                .matchDate(LocalDate.now())
                .stats(new ArrayList<>())
                .build();

        MatchStats stats1 = new MatchStats();
        stats1.setTeam(team1);
        stats1.setGuest(false);

        MatchStats stats2 = new MatchStats();
        stats2.setTeam(team2);
        stats2.setGuest(true);

        match.addStats(stats1);
        match.addStats(stats2);

        match = matchRepository.save(match);

        MatchFinishRequestDto.TeamDto dto1 = new MatchFinishRequestDto.TeamDto();
        dto1.setTeamId(team1.getId());
        dto1.setGoals(3);

        MatchFinishRequestDto.TeamDto dto2 = new MatchFinishRequestDto.TeamDto();
        dto2.setTeamId(team2.getId());
        dto2.setGoals(1);

        MatchFinishRequestDto request = new MatchFinishRequestDto();
        request.setTeam1(dto1);
        request.setTeam2(dto2);

        Match finishedMatch = matchService.finishMatch(match.getId(), request);

        assertThat(finishedMatch.isFinished()).isTrue();
        for (MatchStats stats : finishedMatch.getStats()) {
            if (stats.getTeam().getId().equals(team1.getId())) {
                assertThat(stats.getGoals()).isEqualTo(3);
                assertThat(stats.getScore()).isEqualTo(3);
                assertThat(stats.getIsWinner()).isTrue();
                assertThat(stats.isGuest()).isFalse();
            } else if (stats.getTeam().getId().equals(team2.getId())) {
                assertThat(stats.getGoals()).isEqualTo(1);
                assertThat(stats.getScore()).isEqualTo(0);
                assertThat(stats.getIsWinner()).isFalse();
                assertThat(stats.isGuest()).isTrue();
            } else {
                fail("Unexpected team in match stats: " + stats.getTeam().getId());
            }
        }
    }

}

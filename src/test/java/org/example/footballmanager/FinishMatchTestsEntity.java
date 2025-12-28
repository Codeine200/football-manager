package org.example.footballmanager;

import jakarta.transaction.Transactional;
import org.example.footballmanager.domain.MatchFinish;
import org.example.footballmanager.dto.request.MatchFinishRequestDto;
import org.example.footballmanager.entity.MatchEntity;
import org.example.footballmanager.entity.MatchStatsEntity;
import org.example.footballmanager.entity.TeamEntity;
import org.example.footballmanager.mapper.MatchMapper;
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
public class FinishMatchTestsEntity {

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

    @Autowired
    private MatchMapper matchMapper;

    @Test
    @Transactional
    void shouldFinishMatchAndPersistStats() {
        TeamEntity teamEntity1 = teamRepository.save(TeamEntity.builder().name("Team A").build());
        TeamEntity teamEntity2 = teamRepository.save(TeamEntity.builder().name("Team B").build());

        MatchEntity matchEntity = MatchEntity.builder()
                .season("2025")
                .matchDate(LocalDate.now())
                .stats(new ArrayList<>())
                .build();

        MatchStatsEntity stats1 = new MatchStatsEntity();
        stats1.setTeam(teamEntity1);
        stats1.setGuest(false);

        MatchStatsEntity stats2 = new MatchStatsEntity();
        stats2.setTeam(teamEntity2);
        stats2.setGuest(true);

        matchEntity.addStats(stats1);
        matchEntity.addStats(stats2);

        matchEntity = matchRepository.save(matchEntity);

        MatchFinishRequestDto request =
                new MatchFinishRequestDto(
                        new MatchFinishRequestDto.TeamDto(teamEntity1.getId(), 3),
                        new MatchFinishRequestDto.TeamDto(teamEntity2.getId(), 1)
                );

        MatchFinish matchFinish = matchMapper.toDomain(request);
        MatchEntity finishedMatchEntity = matchService.finishMatch(matchEntity.getId(), matchFinish);

        assertThat(finishedMatchEntity.isFinished()).isTrue();
        for (MatchStatsEntity stats : finishedMatchEntity.getStats()) {
            if (stats.getTeam().getId().equals(teamEntity1.getId())) {
                assertThat(stats.getGoals()).isEqualTo(3);
                assertThat(stats.getScore()).isEqualTo(3);
                assertThat(stats.getIsWinner()).isTrue();
                assertThat(stats.isGuest()).isFalse();
            } else if (stats.getTeam().getId().equals(teamEntity2.getId())) {
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

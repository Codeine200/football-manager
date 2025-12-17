package org.example.footballmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.footballmanager.enums.TeamType;

@Entity
@Table(name = "match_stats", schema = "football")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchStats {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_stats_id_seq")
    @SequenceGenerator(
            name = "match_stats_id_seq",
            sequenceName = "football.match_stats_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_type", length = 10, nullable = false)
    private TeamType teamType;

    @Column(name = "score")
    private Integer score;

    @Column(name = "winner")
    private Boolean winner;

    @Column(name = "goals")
    private Integer goals;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
}

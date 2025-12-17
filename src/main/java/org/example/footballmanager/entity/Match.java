package org.example.footballmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.footballmanager.enums.MatchStatus;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "matches", schema = "football")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "matches_id_seq")
    @SequenceGenerator(
            name = "matches_id_seq",
            sequenceName = "football.matches_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = 20)
    private String season;

    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MatchStatus status;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<MatchStats> stats;
}

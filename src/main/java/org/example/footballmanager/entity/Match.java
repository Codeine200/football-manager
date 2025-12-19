package org.example.footballmanager.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches", schema = "football")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "is_finished")
    private boolean isFinished;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<MatchStats> stats = new ArrayList<>();

    public void addStats(MatchStats matchStats) {
        stats.add(matchStats);
        matchStats.setMatch(this);
    }
}

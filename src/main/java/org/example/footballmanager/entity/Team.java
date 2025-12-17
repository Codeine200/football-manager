package org.example.footballmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "team", schema = "football")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_seq_gen")
    @SequenceGenerator(
            name = "team_seq_gen",
            sequenceName = "football.team_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Player> players;
}

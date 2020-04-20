package org.example.springboot.domain.operators;

import lombok.Builder;
import lombok.Getter;
import org.example.springboot.domain.user.User;

import javax.persistence.*;

@Getter
@Entity
public class Operators {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String index;
    private String category;
    private String uniqueStatisticName;
    private int uniqueStatisticOasisId;
    private int kills;
    private int deaths;
    private int headShot;
    private int meleeKills;
    private int totalXp;
    private int timePlayed;
    private int roundWon;
    private int roundLost;

    public Operators() { }

    @Builder
    public Operators(String name, String index, String category, String uniqueStatisticName, int uniqueStatisticOasisId, int kills, int deaths, int headShot, int meleeKills, int totalXp, int timePlayed, int roundWon, int roundLost) {
        this.name = name;
        this.index = index;
        this.category = category;
        this.uniqueStatisticName = uniqueStatisticName;
        this.uniqueStatisticOasisId = uniqueStatisticOasisId;
        this.kills = kills;
        this.deaths = deaths;
        this.headShot = headShot;
        this.meleeKills = meleeKills;
        this.totalXp = totalXp;
        this.timePlayed = timePlayed;
        this.roundWon = roundWon;
        this.roundLost = roundLost;
    }
}

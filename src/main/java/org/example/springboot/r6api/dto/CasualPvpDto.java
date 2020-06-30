package org.example.springboot.r6api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.example.springboot.domain.casualpvp.CasualPvp;
import org.example.springboot.domain.rankpvp.RankPvp;

import java.time.LocalDateTime;

@ToString
@Getter
public class CasualPvpDto {
    int death;
    int kills;
    int matchLost;
    int matchWon;
    int matchPlayed;
    int timePlayed;
    LocalDateTime createdTime;

    @Builder
    public CasualPvpDto(int death, int kills, int matchLost, int matchWon, int matchPlayed, int timePlayed) {
        this.death = death;
        this.kills = kills;
        this.matchLost = matchLost;
        this.matchWon = matchWon;
        this.matchPlayed = matchPlayed;
        this.timePlayed = timePlayed;
        this.createdTime = LocalDateTime.now();
    }

    public CasualPvpDto(CasualPvp casualPvp) {
        this.death = casualPvp.getDeath();
        this.kills = casualPvp.getKills();
        this.matchLost = casualPvp.getMatchLost();
        this.matchPlayed = casualPvp.getMatchPlayed();
        this.matchWon = casualPvp.getMatchWon();
        this.createdTime = casualPvp.getCreatedTime();
    }
}

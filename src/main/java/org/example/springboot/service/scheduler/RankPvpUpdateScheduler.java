package org.example.springboot.service.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.springboot.domain.player.Player;
import org.example.springboot.domain.player.PlayerRepository;
import org.example.springboot.service.RankPvpService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RankPvpUpdateScheduler {
    private final PlayerRepository playerRepository;
    private final RankPvpService rankPvpService;

    @Scheduled(cron = "0 0 0 * * *")
    public void autoUpdateRankPvp() {
        List<Player> playerList = playerRepository.findAll();
        for(Player player : playerList) {
            rankPvpService.saveRankPvp(player.getPlatform(), player.getUserId());
        }
    }
}
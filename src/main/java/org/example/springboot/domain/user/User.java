package org.example.springboot.domain.user;

import lombok.Builder;
import lombok.Getter;
import org.example.springboot.domain.generalpvp.GeneralPvp;
import org.example.springboot.domain.operators.Operators;
import org.example.springboot.domain.rankstat.RankStat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String platform;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Operators> operatorsList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<RankStat> rankList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "generalpvp_id")
    private GeneralPvp generalPvp;

    public User() { }

    @Builder
    public User(String platform, String userId) {
        this.platform = platform;
        this.userId = userId;
    }

    public void setOperatorsList(List<Operators> operatorsList) {
        this.operatorsList = operatorsList;
    }
    public void setGeneralPvp(GeneralPvp generalPvp) {
        this.generalPvp = generalPvp;
    }
}
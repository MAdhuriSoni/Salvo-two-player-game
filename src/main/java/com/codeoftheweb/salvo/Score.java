package com.codeoftheweb.salvo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private Double Score;
    private Date finishDate;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Player_id")
    public Player player;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    public Game game;


    public Score(){}

    public Score(Game game, Player player ,Double Score) {
        this.game = game;
        this.player = player;
        this.Score = Score;
        this.finishDate = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
   @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Double getScore() {
        return Score;
    }

    public void setScore(Double score) {
        Score = score;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


}


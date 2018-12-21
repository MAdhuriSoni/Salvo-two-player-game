package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import javax.persistence.*;
import java.util.Date;

@Entity

public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private Date date;
    private String name;
    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scores;

    public Date getDate() {
        return date;
    }
    public long getId() {
        return this.id;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public Game() {
        this.date = new Date();
    }

    public boolean hasScore () {
        return (!getScores().isEmpty());
    }
    public boolean isFull() {
        return this.getGamePlayers().size() > 1;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Game(String name){
        this.date=new Date();
        this.name=name;
    }
 public String getGameName(){
        return this.name;
 }
    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }
    public Object gamePlayerID() {
        return id;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    @JsonIgnore
    public List<Player> getPlayers() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }

}

package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity

public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores;


    public long getId() {
        return id;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String userName;
    private String password;

    public Player() {}

    public Player(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    public  String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String toString() {
        return userName;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }
}
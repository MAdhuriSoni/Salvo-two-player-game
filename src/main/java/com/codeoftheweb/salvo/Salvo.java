package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private long GamePlayer;
    private Integer Turn_Number;
    private Long PlayerID ;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    public GamePlayer gamePlayer;


    @ElementCollection
    @Column(name = "locations")
    private List<String> locations;


    public Salvo(){}

    public Salvo(Integer Turn_Number, List<String>locations, GamePlayer gamePlayer, Long PlayerID) {
        this.locations = locations;
        this.gamePlayer = gamePlayer;
        this.Turn_Number = Turn_Number;
        this.PlayerID = PlayerID ;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
    public List<String> getLocations() {
        return locations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getTurn_Number() {
        return Turn_Number;
    }

    public void setTurn_Number(Integer turn_Number) {
        Turn_Number = turn_Number;
    }
    public void setGamePlayer(long gamePlayer) {
        GamePlayer = gamePlayer;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public Long getPlayerID() {
        return PlayerID;
    }

    public void setPlayerID(Long playerID) {
        PlayerID = playerID;
    }
}


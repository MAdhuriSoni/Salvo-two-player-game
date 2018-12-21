package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private String type;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    public GamePlayer gamePlayer;


    @ElementCollection
    @Column(name = "locations")
    private List<String> locations;

    @ElementCollection
    @Column(name="hit")
    private Set<String> hits = new HashSet<>();

    public Ship(){}

    public Ship(String type, List<String>locations, GamePlayer gamePlayer) {
        this.type = type;
        this.locations = locations;
        this.gamePlayer = gamePlayer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public long getId() {
        return id;
    }

    public Set<String> getHits() {
        return hits;
    }
    public void addHits(String hit) {
        this.getHits().add(hit);
    }

    public boolean isSink(){
        return (this.getLocations().size() == this.getHits().size());
    }
}
package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class GamePlayer {

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private GameStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships;
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvos;

//    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
//   private Set<Score> scores;

    public GamePlayer() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void setSalvos(Set<Salvo> salvoes) {
        this.salvos = salvos;
    }

    public Set<Salvo> getSalvos() {
        return salvos;
    }


    public enum GameStatus {
        WaitingForShips,
        WaitingForSecondPlayer,
        WaitingForSalvoes,
        WaitingForEnemySalvoes,
        MyTurn,
        GameOver
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

}

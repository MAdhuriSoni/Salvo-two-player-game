package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RequestMapping("/api")
    @RestController
    public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private SalvoRepository salvoRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    @RequestMapping(path = "/games", method = RequestMethod.GET)
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> map = new HashMap<>();

//        if (authentication != null){
          if(!isGuest(authentication)) {
            map.put("CurrentUser", makeCurrentUserDTO(authentication));
        } else {
            map.put("CurrentUser", "log in");
        }
        map.put("games", gameRepository
                .findAll()
                .stream()
                .map(game -> gameIDDTO(game))
                .collect(toSet()));

        return map;
    }

@RequestMapping(path = "/games", method = RequestMethod.POST )
public ResponseEntity <Map<String, Object>> createGame (Authentication authentication){
        Map<String, Object> map = new HashMap<>();
        if (authentication != null){
            Game game =  new Game();
            gameRepository.save(game);
            Player player = playerRepository.findByUserName(authentication.getName());
            GamePlayer gamePlayer = new GamePlayer(game, player);
            gamePlayer.setStatus(GamePlayer.GameStatus.WaitingForSecondPlayer);
            gamePlayerRepository.save(gamePlayer);
            return new ResponseEntity<>(makeMap("gamePlayerCreated", gamePlayer.getId()), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(makeMap("error", "No logged in player to create game"), HttpStatus.UNAUTHORIZED);
        }
}
@RequestMapping(path = "/games/{IDgame}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getGameJoin (@PathVariable Long IDgame, Authentication authentication) {
        Game game = gameRepository.findOne(IDgame);
    if(game.isFull()) {
        return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
    }
    Player player = CurrentUser(authentication);
        if (authentication == null) {
            return new ResponseEntity<>(makeMap("error", "Need to be logged in to join a game")
                    , HttpStatus.UNAUTHORIZED);
        } else if (game == null) {
            return new ResponseEntity<>(makeMap("error", "No existing game")
                    , HttpStatus.FORBIDDEN);
        } else {
            GamePlayer gamePlayer = new GamePlayer(game, player);
//            gamePlayer.setStatus(GamePlayer.GameStatus.WaitingForShips);
            gamePlayerRepository.save(gamePlayer);
           // GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, player));
            return new ResponseEntity<>(makeMap("gamePlayerID", gamePlayer.getId())
                    , HttpStatus.CREATED);
        }
    }

    public Map<String, Object> gameIDDTO(Game game) {
        Map<String, Object> id = new LinkedHashMap<>();
        id.put("id", game.getId());
        id.put("created", game.getDate());
        id.put("gamePlayer", game.gamePlayers
                .stream()
                .map(gamePlayer -> makeGameplayerDTO(gamePlayer))
                .collect(toList()));
        id.put("scores", game.getScores()
                .stream()
                .map(score -> ScoreDTO(score))
                .collect(toList()));
        return id;
    }

    private Map<String, Object> makeGameplayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("id", gamePlayer.getId());
        info.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        return info;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", player.getId());
        map.put("UserName", player.getUserName());
        return map;
    }

    private Map<String, Object> makeCurrentUserDTO(Authentication authentication) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", CurrentUser(authentication).getId());
        map.put("UserName", CurrentUser(authentication).getUserName());
        return map;
    }

    private Player CurrentUser(Authentication authentication){
        return playerRepository.findByUserName(authentication.getName());
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> ScoreDTO(Score score) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("score", score.getScore());
        map.put("player_id", score.getPlayer().getId());
        return map;
    }

    @RequestMapping("/leaderBoard")
    public List<Object> getScores() {
        return playerRepository
                .findAll()
                .stream()
                .map(player -> makeScoreDTO(player))
                .collect(toList());
    }

    public Map<String, Object> makeScoreDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player", player.getUserName());
        dto.put("totalscore", player.getScores().stream().map(s -> s.getScore()).mapToDouble(s -> s).sum());
        dto.put("wins", player.getScores().stream().filter(s -> s.getScore() == 1.0).count());
        dto.put("losses", player.getScores().stream().filter(s -> s.getScore() == 0.0).count());
        dto.put("ties", player.getScores().stream().filter(s -> s.getScore() == 0.5).count());
        return dto;
    }

    public GamePlayer getOtherPlayer(GamePlayer gamePlayer){
        List<GamePlayer> gamePlayersList = new ArrayList<>();
        Set<GamePlayer> gamePlayerSet = gamePlayer.getGame().getGamePlayers();
        for (GamePlayer gp : gamePlayerSet) {
            if (gp != gamePlayer) {
                gamePlayersList.add(gp);
            }
        }
        return gamePlayersList.get(0);
    }


    private Map<String, Object> shipDTO(Ship ship) {
        Map<String, Object> map = new LinkedHashMap<>();
//        map.put("id", ship.getId());
        map.put("location", ship.getLocations());
        map.put("type", ship.getType());
        return map;
    }


    private List<Object> SalvoGameDTO(GamePlayer gamePlayer) {
        List<Object> salvoList = new ArrayList<>();
        gamePlayer.getSalvos().forEach(salvo -> {
            Map<String, Object> salvoDTO = new LinkedHashMap<>();
            salvoDTO.put("Turn_Number", salvo.getTurn_Number());
            salvoDTO.put("locations", salvo.getLocations());
            salvoList.add(salvoDTO);
        });
        return salvoList;
    }


    @RequestMapping("/game_view/{id}")
    private Map<String, Object> gameView(@PathVariable Long id) {
        Map<String, Object> map = new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepository.getOne(id);
        whenNeededChangeEnemysStatus(gamePlayer);
        GamePlayer opponent = getOpponent(gamePlayer);
        map.put("game", gameIDDTO(gamePlayer.getGame()));
        map.put("gamePlayerId", gamePlayer.getId());
        map.put("UserName", gamePlayer.getPlayer());
        map.put("lastTurnNo",whichTurnIsIt(gamePlayer)-1 );

        map.put("hitsInfo", setHittedsAndSinked(gamePlayer));
        map.put("user_ships", gamePlayer.getShips()
                .stream()
                .map(ship -> shipDTO(ship))
                .collect(toList()));
        map.put("userSalvos", SalvoGameDTO(gamePlayer));      // userSalvos
        if (opponent != null) {
            map.put("opponentSalvos", SalvoGameDTO(opponent));         //opponentsalvos
            map.put("opponent_sunkShips", getSunkShips(opponent));
            map.put("user_sunkShips", getSunkShips(gamePlayer));
            map.put("gameStatus", MakeGameStatusDTO(gamePlayer));
        }

        return map;
    }

    private Set<Object> setHittedsAndSinked (GamePlayer gamePlayer){
        GamePlayer opponent = getOpponent(gamePlayer);
        Integer lastTurnNo = whichTurnIsIt(gamePlayer) - 1;
        if (lastTurnNo == 0){ return null; }

        Set<Object> hitsAndSinksSet = new HashSet<Object>();
        for (int i = 1; i <= lastTurnNo; i++){
            hitsAndSinksSet.add(MakeHitsAndSinks(i, gamePlayer));
        }
        return hitsAndSinksSet;
    }
    private Map<String, Object> MakeHitsAndSinks(int currentTurn, GamePlayer gamePlayer){     //hits and sink for gameview

        Map<String, Object> hitsAndSinks = new LinkedHashMap<String, Object>();
        hitsAndSinks.put("Turn_Number", currentTurn);
        hitsAndSinks.put("hitsOnPlayer", MakeHitsOnGivenPlayer(gamePlayer, currentTurn));
        hitsAndSinks.put("hitsOnEnemy", MakeHitsOnGivenPlayer(getOpponent(gamePlayer), currentTurn));
        return hitsAndSinks;
    }
    private Map<Object, Object> MakeHitsOnGivenPlayer(GamePlayer givenGP, int currentTurn){   //hits and sink data for gameview

         Set<Ship> givenPlayerShips = givenGP.getShips();
        Set<Salvo> enemySalvosFromCurrentTurn = getOpponent(givenGP).getSalvos()
                .stream()
                .filter(salvo -> salvo.getTurn_Number() == currentTurn)
                .collect(Collectors.toSet());
        Map<Object, Object> hitsOnGivenPlayer = new LinkedHashMap<Object, Object>();

        givenPlayerShips.stream().forEach((ship) -> {
            hitsOnGivenPlayer.put(ship.getType(), MakeShipInfoForHits(ship, enemySalvosFromCurrentTurn));
        });
        return hitsOnGivenPlayer;
    }
    private Map<String, Object> MakeShipInfoForHits(Ship currentShip, Set<Salvo> currentSalvosFromCurrentTurn){
        List<String> currentShipLocations = currentShip.getLocations();
        ArrayList<String> hits = new ArrayList<>();
        currentSalvosFromCurrentTurn.forEach(salvo -> {
            salvo.getLocations().forEach(singleShot -> {
                if (currentShipLocations.contains(singleShot)){
                    hits.add(singleShot);
                    currentShip.addHits(singleShot);
                }
            });
        });

        Map<String, Object> currentShipInfo = new LinkedHashMap<String, Object>();
        currentShipInfo.put("size", currentShipLocations.size());
        currentShipInfo.put("hits", hits);
        currentShipInfo.put("hitsTillNow", currentShip.getHits().size());
        currentShipInfo.put("isSink", currentShip.isSink());
        return currentShipInfo;
    }

    private Integer whichTurnIsIt(GamePlayer gamePlayer) {
        Integer Turn_Number = 0;
        for (Salvo salvo : gamePlayer.getSalvos()) {
            Integer turn = salvo.getTurn_Number();
            if (Turn_Number < turn) {
                Turn_Number = turn;
            }
        }
        return Turn_Number + 1;
    }

    private Boolean isMyTurn (GamePlayer gamePlayer) {
        Boolean myTurn = false;
        if (gamePlayer.getId() < getOpponent(gamePlayer).getId()){
            if (whichTurnIsIt(gamePlayer) <= whichTurnIsIt(getOpponent(gamePlayer))) {
                myTurn = true;
            }
        } else {
            if (whichTurnIsIt(gamePlayer) < whichTurnIsIt(getOpponent(gamePlayer))) {
                myTurn = true;
            }
        }
        return myTurn;
    }

    public List<Object> getSunkShips (GamePlayer gamePlayer) {
        List<Object> sunkShips = new ArrayList<>();
        for (Ship ship : gamePlayer.getShips()) {
            if (ship.isSink()){
                sunkShips.add(shipDTO(ship));
            }
        }
        return sunkShips;
    }


    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable Long gamePlayerId,
                                                          @RequestBody Set<Ship> ships,
                                                          Authentication authentication) {
        GamePlayer gamePlayer  = gamePlayerRepository.findOne(gamePlayerId);
        Game game = gamePlayer .getGame();
        if (authentication == null) {
            return new ResponseEntity<>(makeMap("error", "log in to place ships"), HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer  == null) {
            return new ResponseEntity<>(makeMap("error", "gamePlayer does not exist"), HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer .getPlayer() != CurrentUser(authentication)) {
            return new ResponseEntity<>(makeMap("error", "This is Not your game"), HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer .getShips().size() > 5) {
            return new ResponseEntity<>(makeMap("error", "Ships are already placed")
                    , HttpStatus.FORBIDDEN);
        }
        else {
            for (Ship ship : ships) {
                ship.setGamePlayer(gamePlayer );
                if (!game.isFull()){

                } else if (getOpponent(gamePlayer).getStatus() == GamePlayer.GameStatus.WaitingForShips){
                    //gamePlayer .setStatus(GamePlayer.GameStatus.WaitingForSecondPlayer);
                    whenNeededChangeEnemysStatus(gamePlayer );
                } else {

                    whenNeededChangeEnemysStatus(gamePlayer);
                }
                shipRepository.save(ship);
            }
            return new ResponseEntity<>(makeMap("succes", "Ships are created")
                    , HttpStatus.CREATED);
        }
    }

    public GamePlayer getOpponent (GamePlayer gamePlayer){
        Optional<GamePlayer> enemy = gamePlayer.getGame().getGamePlayers()
                .stream()
                .filter(gp -> gp.getId() != gamePlayer.getId())
                .findFirst();
        return (enemy.isPresent()) ? enemy.get() : gamePlayer;
    }


    public void whenNeededChangeEnemysStatus(GamePlayer CurrentUser){
        GamePlayer opponent = getOpponent(CurrentUser);
        Game currentGame = CurrentUser.getGame();
      if(!currentGame.isFull())
      {
          if(CurrentUser.getShips().size()==0)
          {
              CurrentUser.setStatus(GamePlayer.GameStatus.WaitingForShips);
          }
          else{
              CurrentUser.setStatus(GamePlayer.GameStatus.WaitingForSecondPlayer);
          }
      } else{
          if (CurrentUser.getShips().size() == 0) {
              CurrentUser.setStatus(GamePlayer.GameStatus.WaitingForShips);
          } else if (opponent.getShips().size() == 0) {
              CurrentUser.setStatus(GamePlayer.GameStatus.WaitingForSalvoes);
           } else if (!isMyTurn(CurrentUser)) {
              CurrentUser.setStatus(GamePlayer.GameStatus.WaitingForEnemySalvoes);
            } else {
              CurrentUser.setStatus(GamePlayer.GameStatus.MyTurn);
          }

        }
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvos/{turnNumber}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placingSalvos(@PathVariable long gamePlayerId,
                                                             @PathVariable Integer turnNumber,
                                                             @RequestBody List<String> SalvosLocation,
                                                             Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Integer currentTurnNo = whichTurnIsIt(gamePlayer);
        if (authentication == null) {
            return new ResponseEntity<>(makeMap("error", "need to logged in to fire salvos")
                    , HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer == null) {
            return new ResponseEntity<>(makeMap("error", "The gamePlayer does not exist")
                    , HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer.getPlayer() != CurrentUser(authentication)) {
            return new ResponseEntity<>(makeMap("error", "This is not your game (the current User is not in this gamePlayer)")
                    , HttpStatus.UNAUTHORIZED);
        } else if (SalvosLocation.size()>5) {
            return new ResponseEntity<>(makeMap("error", "Salvos already has been placed")
                    , HttpStatus.FORBIDDEN);
           // whenNeededChangeEnemysStatus(CurrentUser);
        }
        else {
            Map<String, Object> salvoMap = new LinkedHashMap<>();
            Salvo salvo = new Salvo(turnNumber,SalvosLocation,gamePlayer,gamePlayer.getPlayer().getId());
            salvoRepository.save(salvo);
            salvoMap.put("locations",salvo.getLocations());
            salvoMap.put("Turn_Number",salvo.getTurn_Number());
            return new ResponseEntity<>(salvoMap
                    , HttpStatus.CREATED);

        }
      }

    @RequestMapping("/players")
    public ResponseEntity<String> createPlayer(
                                               String userName,
                                               String password) {
        if (userName.isEmpty()) {
            return new ResponseEntity<>("No name given", HttpStatus.FORBIDDEN);
        }
        final Player existingPlayer = playerRepository.findByUserName(userName);
        if (existingPlayer != null) {
            return new ResponseEntity<>("Name is already taken", HttpStatus.CONFLICT);
        }
        playerRepository.save(new Player(userName, password));
        return new ResponseEntity<>("Player created", HttpStatus.CREATED);
    }


    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    // add salvoes
    private boolean isTurnCorrect(Salvo salvo, Long currentTurnNo){
        Integer turnNoInReceivedData = salvo.getTurn_Number();
        return turnNoInReceivedData.equals(currentTurnNo);
    }

    private Map<String, Object> MakeGameStatusDTO(GamePlayer gamePlayer)
    {
        Map<String, Object> gameStatusDTO = new LinkedHashMap<String, Object>();
        if(isGameOver(gamePlayer)){
            gamePlayer.setStatus(GamePlayer.GameStatus.GameOver);
            getOpponent(gamePlayer).setStatus(GamePlayer.GameStatus.GameOver);
            gamePlayerRepository.save(gamePlayer);
            gamePlayerRepository.save(getOpponent(gamePlayer));
        }
        gameStatusDTO.put("status", gamePlayer.getStatus());
        gameStatusDTO.put("isGameOver", isGameOver(gamePlayer));
        if(isGameOver(gamePlayer)){
            gameStatusDTO.put("whoWon", whoWon(gamePlayer));
        }
        return gameStatusDTO;
    }

    private boolean isGameOver(GamePlayer gp){
        return ((noPlayersSinkedShips(gp) == 5) || noPlayersSinkedShips(getOpponent(gp)) == 5);
    }

    private String whoWon(GamePlayer gp){
        if (noPlayersSinkedShips(gp) < noPlayersSinkedShips(getOpponent(gp))){
            changeScores(gp, "userWon");
            return gp.getPlayer().getUserName();
        } else if (noPlayersSinkedShips(gp) > noPlayersSinkedShips(getOpponent(gp))){
            changeScores(getOpponent(gp), "opponentWon");
            return getOpponent(gp).getPlayer().getUserName();
        } else {
            changeScores(gp, "tie");
            return "tie";
        }
    }



    private long noPlayersSinkedShips(GamePlayer gp){
        return gp.getShips().stream()
                .filter(ship -> ship.isSink())
                .count();
    }

    private void changeScores(GamePlayer gamePlayer, String tie){
        if(!gamePlayer.getGame().hasScore()){
            if(tie == "tie"){
                 Score newScore1 = new Score(gamePlayer.getGame(),gamePlayer.getPlayer(), 0.5);
                Score newScore2 = new Score(gamePlayer.getGame(),getOpponent(gamePlayer).getPlayer(), 0.5);
                scoreRepository.save(newScore1);
                scoreRepository.save(newScore2);
            } else {
                Score newScore1 = new Score(gamePlayer.getGame(),gamePlayer.getPlayer(),1.0);
                Score newScore2 = new Score(gamePlayer.getGame(),getOpponent(gamePlayer).getPlayer() , 0.0);
                scoreRepository.save(newScore1);
                scoreRepository.save(newScore2);
            }
        }
    }
}
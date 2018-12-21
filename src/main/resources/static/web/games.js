var main = new Vue({
    el: '#main',
    data: {

leader:[],
gameInfo:[],
//gamesList:null,
gamesList:[],
gameData:[],
LoggedIn: false,
LoggedOut: true,
CurrentUser: "",
currentPlayer: "",
tableHide: false,

    },
    created() {
        this.getLeaderBoard();
        this.getGames();

    },
    methods: {

getLeaderBoard: function()
{
      $.get("/api/leaderBoard")
         .done(function (data) {
         main.leader = data;
            console.log(data);
        })
        .fail(function (jqXHR, textStatus) {
            alert("Failed: " + textStatus);
        });
},

getGames: function() {
    $.get("/api/games")
        .done(function (data) {
           console.log(data);
             main.gamesList=data.games.sort((a,b) => a.id - b.id);
             console.log(data)
            main.games=data;
            console.log(data.CurrentUser)
            if (data.CurrentUser != "log in") {
                main.LoggedOut=false;
                main.LoggedIn=true;
                 main.tableHide= true;
                main.currentPlayer = data.CurrentUser;
            } else {
                  main.LoggedOut=true;
                  main.LoggedIn=false;
                  main.tableHide=false;
            }
        })
        .fail(function (jqXHR, textStatus) {
            alert("Failed: " + textStatus);
        });
},

returnToGame: function (id) {
    location.replace("/web/game.html?game_view="+ id)
},

 login: function(userName, password){
var userName = document.getElementById("username").value
var password = document.getElementById("password").value

  fetch("/api/login", {
                     credentials: 'include',
                     method: 'POST',
                     headers: {
                         'Accept': 'application/json',
                         'Content-Type': 'application/x-www-form-urlencoded'
                     },
                     body: 'userName=' + userName + '&password=' + password,
                 })
                 .then(r =>{
                     if (r.status == 200){
                     main.LoggedOut = false;
                     main.LoggedIn = true;
                     main.tableHide = true;
 		console.log(r)
 		}
 		else if (r.status == 401) {
         				alert("wrong password");
         				}
      })
                 .catch(e => console.log(e))
 },


  logout: function ()
 {
          fetch("/api/logout", {
                     credentials: 'include',
                     method: 'POST',
                     headers: {
                         'Accept': 'application/json',
                         'Content-Type': 'application/x-www-form-urlencoded'
                     },
                 })
                 .then(r =>{
       		if (r.status == 200) {
       		    main.LoggedOut = true;
                main.LoggedIn = false;
                main.tableHide = false;
 				console.log(r)
 				}
                 })
                 .catch(e => console.log(e))
 },


 signUp: function ()
  {
  var userName = document.getElementById("username").value
           var password = document.getElementById("password").value
        fetch("/api/players", {
                      credentials: 'include',
                      method: 'POST',
                      headers: {
                          'Accept': 'application/json',
                          'Content-Type': 'application/x-www-form-urlencoded'
                      },
                     body: 'userName=' + userName + '&password=' + password,
                  })
                  .then(r =>
        		{if (r.status == 200)
        		    main.LoggedOut = true;
                 main.LoggedIn = false;
  				console.log(r)
                  })
                  .catch(e => console.log(e))
  },


  createGame: function () {
              fetch("/api/games", {
                      credentials: 'include',
                      method: 'POST',
                  })
                  .then(response => response.json())
                  .then(res => {
                   if (res.gamePlayerCreated != null) {
                           var gpID = res.gamePlayerCreated;
                                     console.log(gpID);
                                     window.location.href = "/web/game.html?game_view=" + gpID;
                                      } else {
                               alert("error")
                        console.log(res);
                        }
                      })
                  .catch(function (res) {
                  })
          },



          joinGame: function (gameID) {
          console.log(gameID);
                      fetch("/api/games/" + gameID + "/players", {
                              credentials: 'include',
                              method: 'POST',
                          })
                           .then(response => response.json())
                               .then(res => {

                                   if (res.gamePlayerID != null) {
                                       window.location.href = "/web/game.html?game_view=" + res.gamePlayerID
                                          } else {
                                               alert("error")
                                                 }
                                         })
                                         .catch(function (res) {
                                            })
                                    },
      back: function () {
        window.history.back();
     },

    }
})

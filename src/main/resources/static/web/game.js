var url = window.location.href;
var id = retrieveGameplayId(url);

fetchGameData();

function retrieveGameplayId(search) {   // search dynamic id
    console.log(url)
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;
    search.replace(reg, function (match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });
    console.log(obj)
    return obj;
}

var main = new Vue({
   el: '#main',
   data: {
   salvo:false,
       data: [] ,
       LoggedOut: true,
       shipLocations:[],  //this is the location of all ships
       numbers: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
       alphabet: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'],
       rows: [],
       columns: [],
       gameData: {},
       placing: true,
       salvoPlaced: false,
       opponent: "",
       gameOver: false,
       Rotate: true,
       currentShip:{},  //this is the location of current placing ship
       shipName:"",
       salvoLocation:[],
       hitsInfo:[],
       salvos: [],
       allShots:[],
       turnNumber : 0,
       allShips:
       [{
       type:'Destroyer',
       length:5,
       locations:[],
       isPlaced: false,
       },
       {
       type:'Submarine',
       length:4,
       locations:[],
       isPlaced: false,
       },
       {
       type:'PatrolBoat',
       length:3,
       locations:[],
       isPlaced: false,
        },
         {
       type:'Fishingboat',
        length:3,
        locations:[],
        isPlaced: false,
         },
         {
         type:'Speedboat',
         length:2,
         locations:[],
         isPlaced: false,
       }]
       },


   created() {

   },
   methods: {


 shipColorForGame: function(data) {  //Location of ship and change color after saving ship
        for (i = 0; i < data.user_ships.length; i++) {
            for (j = 0; j < data.user_ships[i].location.length; j++) {
                var findCell = document.getElementById("player" + data.user_ships[i].location[j]);
                findCell.setAttribute("class", "change_cell_color");
            }
        }
        if( data.isSink == true )
         {
           findCell.setAttribute("class", "change_sink_color");
         }
    },


       shipPlacer: function(cellId) {  //Location of ship and find the cell

              cellId.slice()
               var char = cellId[0];
               var number;
               if(cellId.length == 2) {
                    number = cellId[1] ;
               } else {
                    number = cellId[1]+ cellId[2];
               }
                  if(document.getElementById("slect_tag").value == "Horizontal")    // this row will check the condition for horizontal select option
                  {
                  var select_tag = document.getElementById("slect_tag");
          try{
                var indexToCheck = number;
                var charToCheck = char;
                if(parseInt(number) + this.currentShip.length <= this.numbers.length + 1)
                 {
                  for (i=0; i<this.currentShip.length; i++)
                   {
                    for(j=0; j<this.shipLocations.length; j++)
                    {
                     if(this.shipLocations[j] == charToCheck + indexToCheck)
                   {
                   throw "Selected ship overlaps an other ship, please try again"
                   }
                 }
                 indexToCheck++;
                }
              for (k=0; k<this.currentShip.length; k++)  //horizontal placing ships
               {
               this.shipLocations.push(char + number);
               this.currentShip.locations.push(char + number);
               console.log(char)
               console.log(number)
               this.shipGridColor(char + number);
               number++
               }
          this.currentShip.isPlaced = true;
               console.log("Current ship")
               console.log(this.currentShip.locations)
               console.log("All ship locations")
               console.log(this.shipLocations)
           }

           else{
           alert("ship is placed outside of the grid");

           }
    }
    catch(error){
             alert(error);
          }
      }
else                            //checking the condition for verticale placing ship
   {
                var index ;
                var indexToCheck = number;
                var charToCheck = char;
                for(m=0; m<this.alphabet.length; m++)
                {
                  if(this.alphabet[m] == char)
                  {
                    index = m;
                  }
                }
                if(char + this.currentShip.length <= this.numbers.length + 1)     //this will select vertical
                 {
                  for (i=0; i<this.currentShip.length; i++)
                   {
                    for(j=0; j<this.shipLocations.length; j++)
                    {
                     if(this.shipLocations[j] == charToCheck + indexToCheck)
                   {
                   throw "Selected ship overlaps an other ship, please try again"
                   }
                 }
                 charToCheck = this.alphabet[index++];
                }
               }
                      for (k=0; k<this.currentShip.length+1; k++)    //vertical placing ships
                       {
                       this.shipLocations.push(char + number);
                       this.currentShip.locations.push(char + number);
                       console.log(char)
                       console.log(number)
                       this.shipGridColor(char + number);
                       console.log("Index: " + index)
                       char = this.alphabet[index++]
                      console.log("Index: " + index)
                       }
                  this.currentShip.isPlaced = true;
                       console.log("Current ship")
                       console.log(this.currentShip.location)
                       console.log("All ship locations")
                       console.log(this.shipLocations)
   }
},
           shipGridColor:function(cellId)   //color for horizontal and vertical placing ships
           {
              console.log(cellId)
              var cell = document.getElementById("player" + cellId);
              cell.setAttribute("class","placingShipColor");
           },

          shipRemoveFromGrid: function()
          {
                   for (j = 0; j < this.currentShip.locations.length; j++)
                   {
                        var index = this.shipLocations.indexOf(this.currentShip.locations[j]);
                        this.shipLocations.splice(index, 1);
                        var findCell = document.getElementById("player" + this.currentShip.locations[j]);
                        findCell.removeAttribute("class");
                   }
                      this.currentShip.locations = [];
               this.currentShip.isPlaced = false;
          },


          shipCheckIsPlaced: function(event)   //placing a ships
          {
             if(this.currentShip.isPlaced == true)
             {
             this.shipRemoveFromGrid()
              //alert("ship already placed");
             }
             else
             {
                this.shipPlacer(event.target.id.replace("player", ""));    // call the shipPlacer function here
             }
          },


           shipSelectingData:function(shipName)  // selecting the ships for the checkboxes
           {
               for(i=0;i<this.allShips.length; i++)
                   if(this.allShips[i].type == shipName)
                   {
                        this.currentShip = this.allShips[i];
                       console.log(this.currentShip)
                       this.checked = true
                   }
           },

  afterRefreshPageShipSave: function ()
{
            if(this.allShips.length === 5)
            {
            console.log(id.game_view)
                fetch("/api/games/players/" +id.game_view + "/ships", {
                        credentials: 'include',
                        method: 'POST',
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(this.allShips),
                    })
                   .then(function (response) {
                                         response.status
                                         if (response.status == 201) {
                                             return response.json().then((data) => { console.log(data.Destroyer.location)
                                                                                   console.log(data.Speedboat.location)
                                                                                    console.log(data.Submarine.location)
                                                                                    console.log(data.PatrolBoat.location)
                                                                                    console.log(data.Fishingboat.location)
                                              for (i=0; i<data.Destroyer.location.length; i++){
                                              main.shipGridColor(data.Destroyer.location[i])
                                              }
                                              for(j=0; j<data.Speedboat.location.length; j++){
                                              main.shipGridColor(data.Speedboat.location[j])
                                              }
                                             for(k=0; k<data.Submarine.location.length; k++){
                                              main.shipGridColor(data.Submarine.location[k])
                                             }
                                             for(l=0; l<data.PatrolBoat.location.length; l++){
                                              main.shipGridColor(data.PatrolBoat.location[l])
                                             }
                                             for(m=0; m<data.Fishingboat.location.length; m++){
                                             main.shipGridColor(data.Fishingboat.location[m])
                                             }
                                          })
                                        } else  {
                                            return response.json().then((data) => { console.log(data) })
                                         }
                                     }, function (error) {
                                     error.message //=> String
                                 })
               }
                                  else if (this.allShips.length >= 5){
                                        alert("You can't send more than 5 ships");
                                        return false
                                    } else {
                                        alert("You must place all the ships on the board");
                                    }
           },


salvoInformation: function (data)  //Location of salvos
    {
       console.log('this is salvo information')
        for (var i = 0; i < data.userSalvos.length; i++) {
                    console.log("logged in user is the same as salvo");
                    for (var k = 0; k < data.userSalvos[i].locations.length; k++) {
                        var findCell = document.getElementById("opponent" + data.userSalvos[i].locations[k]);
                        console.log(findCell.className)
                        if (findCell.className == "change_cell_color") {
                            findCell.setAttribute("class", "salvo_Shoot_Hit_player");
                        }
                        else {
                            findCell.setAttribute("class", "Salvo_Shoot_miss_player");
                        }
                    }
                }
                    console.log("opponents salvos here");
              for(i=0; i<data.opponentSalvos.length; i++)
              {
                   for (var k = 0; k < data.opponentSalvos[i].locations.length; k++) {
                    console.log(data.opponentSalvos[i].locations[k])
                        var findCell = document.getElementById("player" + data.opponentSalvos[i].locations[k]);
                        if (findCell.className == "change_cell_color") {
                            findCell.setAttribute("class", "salvo_Shoot_Hit");
                            }
                             else {
                            findCell.setAttribute("class", "Salvo_Shoot_miss");
                         }
                    }
                 }
             if(data.hitsInfo != null)
             {
                 for(L=0; L<data.hitsInfo.length; L++)
               {
                for ( key in data.hitsInfo[L].hitsOnEnemy )
                  {
                  for(t=0; t<(data.hitsInfo[L].hitsOnEnemy[key].hits.length);t++)
                   {
                console.log(data.hitsInfo[L].hitsOnEnemy[key].hits[t])
                var hitCell =document.getElementById("opponent"+data.hitsInfo[L].hitsOnEnemy[key].hits[t])
                hitCell.setAttribute("class", "Salvo_Hit_enemy")
                    }
                 }
              }
          }
      },

     userSunkShip: function(data)
    {
      for(i=0; i<data.user_sunkShips.length; i++)
       {
        console.log(data.user_sunkShips[i])
         for(j=0; j<data.user_sunkShips[i].location.length; j++)
          {
            console.log(data.user_sunkShips[i].location[j])
            var userSunkCell = document.getElementById("player" +data.user_sunkShips[i].location[j])
            console.log(userSunkCell);
            userSunkCell.setAttribute("class", "User_Sunk_Ship")
          }
       }
    },

    opponentSunkShip: function(data)
    {
      for(i=0; i<data.opponent_sunkShips.length; i++)
       {
       console.log(data.opponent_sunkShips[i]);
         for(j=0; j<data.opponent_sunkShips[i].location.length; j++)
         {
          console.log(data.opponent_sunkShips[i].location[j]);
          var oppoSunkCell = document.getElementById("opponent" +data.opponent_sunkShips[i].location[j])
          console.log(oppoSunkCell);
          oppoSunkCell.setAttribute("class", "opponent_sunk_ship")
         }
       }
    },

   sendUserNameForYou: function(player)
     {
         console.log(player)
         for(i=0;i<player.length;i++)
       {
         console.log(this.data.UserName)
         if(player[i].player.UserName==this.data.UserName.userName)
          {
         return player[i].player.UserName;
          }
       }
   },

   sendUserNameForOpponent: function(opponent)
    {
      console.log(opponent)
      for(j=0; j<opponent.length; j++)
       {
           console.log(this.data.UserName)
           if(opponent[j].player.UserName != this.data.UserName.userName)
           {
             return opponent[j].player.UserName;
           }
       }
    },

   salvosCheckIsPlaced: function(shotCell)
        {
             console.log(shotCell)
             if(this.salvoLocation.length<5)
         {
             if(this.salvoLocation.indexOf(shotCell)<0)
             {
             var salvoLoc = document.getElementById("opponent" + shotCell)
              salvoLoc.setAttribute("class" , "change_salvo_color");

             this.salvoLocation.push(shotCell)
             }
             else
             {
               var index = this.salvoLocation.indexOf(shotCell);
               console.log(index)
               this.salvoLocation.splice(index, 1);
               var findCell = document.getElementById("opponent" + shotCell)
               findCell.removeAttribute("class", "change_salvo_color");
                this.salvoLocation.salvoPlaced = false;
             }
         }
             else
          {
              alert("only five cells per shots")
          }
             console.log(this.salvoLocation)
        },


afterRefreshPageSalvoSave: function()
  { console.log(this.salvoLocation)
                if (this.salvoLocation.length === 5) {
                this.turnNumber=this.turnNumber+1
                    fetch("/api/games/players/" +id.game_view + "/salvos/"+this.turnNumber, {
                              credentials: 'include',
                               method: 'POST',
                               headers: {
                               'Accept': 'application/json',
                               'Content-Type': 'application/json'
                                 },
                                 body: JSON.stringify(this.salvoLocation),
                                 })
                                .then(function (response) {
                                 console.log(response)
                                 response.status
                                if (response.status == 201) {
                                 return response.json().then((data) => {
                                 this.salvoLocation =[];
                                 console.log(data)
                          //    main.salvoRemoveFromGrid()
                                })
                               } else  {
                               return response.json().then((data) => { console.log(data)
                             main.Turn_Number = data.turnNumber})
                                   }
                             }, function (error) {
                          error.message //=> String
                           })
                           }
                           else {
                          alert("please fire 5 shots");
                     }
              },

            logout: function()
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
                				console.log(r)
                				}
                                })
                                .catch(e => console.log(e))
                }

      }
});

 function fetchGameData() {
 $.getJSON("/api/game_view/" + id.game_view)
      .done(function (data) {
          console.log(data);
          //if(data.game.gamePlayer.length === 2) main.opponent = data.game.gamePlayer[1].player.UserName;    // hide a player
            main.data = data;
            main.hitsInfo = data;
            main.turnNumber = data.lastTurnNo;
            main.shipColorForGame(data)
            main.salvoInformation(data);
            main.userSunkShip(data);
            main.opponentSunkShip(data);
            main.salvo=true;
             // }
      })
      .fail(function (error) {
          console.log("Error: " + error);
      });
  }





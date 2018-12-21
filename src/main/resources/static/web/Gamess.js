var main = new Vue({
    el: '#main',
    data: {
        games: [],
        currentPlayer:false,
        currentGameplayer:false

    },
    created: function () {
        fetchData();
    },
    method: {

    }
});

function fetchData() {
    $.get("/api/games")
        .done(function (data) {
             console.log(data);
            main.games=data;
             console.log(main.games)
            main.currentPlayer=true;
        })
        .fail(function (jqXHR, textStatus) {
            alert("Failed: " + textStatus);
        });
}

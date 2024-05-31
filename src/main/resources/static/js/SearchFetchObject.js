function searchTracks() {
    let tracks = document.querySelectorAll(".playlist-tracks li");
    let searchInput = document.getElementById("searchInput").value.toLowerCase();

    tracks.forEach(function (track) {
        let trackTitle = track.querySelector(".checkbox-info-track span:nth-child(3)").textContent.toLowerCase();
        let trackArtist = track.querySelector(".checkbox-info-track span:first-child").textContent.toLowerCase();

        if (trackTitle.includes(searchInput) || trackArtist.includes(searchInput)) {
            track.style.display = "block";
        } else {
            track.style.display = "none";
        }
    });
}

function searchArtistToAdmin() {
    let artists = document.querySelectorAll(".admin__panel-artists ul");
    let searchInput = document.getElementById("searchInput").value.toLowerCase();

    artists.forEach(function (artist) {
        let artistTitle = artist.querySelector(".admin__panel-artists-title").textContent.toLowerCase();

        if (artistTitle.includes(searchInput)) {
            artist.style.display = "block";
        } else {
            artist.style.display = "none";
        }
    });
}
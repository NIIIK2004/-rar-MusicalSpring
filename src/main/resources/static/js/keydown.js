document.addEventListener("keydown", function (event) {
    if (event.code === "Space") {
        event.preventDefault();
        togglePlayPause();
    }
});

function togglePlayPause() {
    document.addEventListener("DOMContentLoaded", () => {
        const audioPlayers = document.querySelectorAll(".audioPlayer");
        const audioPlayer = audioPlayers[currentIndex];

        if (audioPlayer) {
            if (audioPlayer.paused) {
                audioPlayer.play();
            } else {
                audioPlayer.pause();
            }
        }
    })
}

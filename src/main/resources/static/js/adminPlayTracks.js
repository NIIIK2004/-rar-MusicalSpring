document.addEventListener('DOMContentLoaded', function() {
    const audioPlayers = document.querySelectorAll('.audioPlayer');
    const playPauseButtons = document.querySelectorAll('.tracks-main__btn');

    let currentPlaying = null;

    playPauseButtons.forEach(function(button, index) {
        button.addEventListener('click', function() {
            const audioPlayer = audioPlayers[index];

            if (currentPlaying !== audioPlayer) {
                playTrack(audioPlayer, button);
            } else {
                if (audioPlayer.paused || audioPlayer.ended) {
                    playTrack(audioPlayer, button);
                } else {
                    pauseTrack(audioPlayer, button);
                }
            }
        });
    });

    function playTrack(player, button) {
        pauseAll(player);

        player.currentTime = 0;
        player.play();
        button.querySelector('.playPauseIconSlider').src = '../images/pause-track-small.svg';
        currentPlaying = player;
    }

    function pauseTrack(player, button) {
        player.pause();
        button.querySelector('.playPauseIconSlider').src = '../images/play-track-small.svg';
        currentPlaying = null;
    }

    function pauseAll(excludePlayer) {
        audioPlayers.forEach(function(player, index) {
            if (player !== excludePlayer) {
                const button = playPauseButtons[index];
                button.querySelector('.playPauseIconSlider').src = '../images/play-track-small.svg';
                player.pause();
            }
        });
    }

    audioPlayers.forEach(function(player, index) {
        player.addEventListener('ended', function() {
            playPauseButtons[index].querySelector('.playPauseIconSlider').src = '../images/play-track-small.svg';
            currentPlaying = null;
        });
    });
});

let progressBar = document.querySelector(".progress-bar");
let progress = document.querySelector(".progress");
let circle = document.querySelector(".circle");
let currentTime = document.querySelector(".current-time");
let totalTime = document.querySelector(".total-time");
let volumeControl = document.getElementById("volumeControl");

let currentAudioPlayer = null;
let currentPlayPauseIcon = null;
document.addEventListener("DOMContentLoaded", () => {
    const playPauseButtons = document.querySelectorAll(".tracks-main__btn");
    const audioPlayers = document.querySelectorAll(".audioPlayer");
    const playPauseIcons = document.querySelectorAll(".playPauseIconSlider");

    const prevButton = document.querySelector("#prevButton");
    const playPauseButton = document.querySelector("#playPauseButton");
    const nextButton = document.querySelector("#nextButton");

    let currentIndex = 0;

    playPauseButtons.forEach((button, index) => {
        const audioPlayer = audioPlayers[currentIndex];
        audioPlayer.currentTime = 0;
        button.addEventListener("click", () => {
            currentIndex = index;
            playTrack(index);
        });
    });

    prevButton.addEventListener("click", () => {
        const audioPlayer = audioPlayers[currentIndex];
        audioPlayer.currentTime = 0;
        currentIndex = (currentIndex - 1 + playPauseButtons.length) % playPauseButtons.length;
        playTrack(currentIndex);
    });

    nextButton.addEventListener("click", () => {
        const audioPlayer = audioPlayers[currentIndex];
        audioPlayer.currentTime = 0;
        currentIndex = (currentIndex + 1) % playPauseButtons.length;
        playTrack(currentIndex);
    });

    playPauseButton.addEventListener("click", () => {
        const audioPlayer = audioPlayers[currentIndex];
        const playPauseIcon = playPauseIcons[currentIndex];

        if (audioPlayer.paused) {
            audioPlayer.play();
            playPauseIcon.src = "../images/pause-track-small.svg";
            playPauseButton.querySelector(".playPauseIcon").src = "../images/pause-icon.svg";
        } else {
            audioPlayer.pause();
            playPauseIcon.src = "../images/play-track-small.svg";
            playPauseButton.querySelector(".playPauseIcon").src = "../images/play-icon.svg";
        }
    });

    audioPlayers.forEach((audioPlayer, index) => {
        audioPlayer.addEventListener("ended", () => {
            const playPauseIcon = playPauseIcons[index];
            playPauseIcon.src = "../images/play-track-small.svg";
            playPauseIcon.parentElement.style.opacity = '0';
            audioPlayer.currentTime = 0;

            const nextIndex = (currentIndex + 1) % playPauseButtons.length;
            currentIndex = nextIndex;
            playTrack(nextIndex);
            updateBottomPanel(playPauseButtons[nextIndex].getAttribute("data-track-id"));
        });
    });


    function playTrack(index) {
        audioPlayers.forEach((audioPlayer, i) => {
            const playPauseIcon = playPauseIcons[i];
            const button = playPauseButtons[i];
            if (i === index) {
                audioPlayer.play();
                playPauseIcon.src = "../images/pause-track-small.svg";
                button.style.opacity = '1';
                playPauseButton.querySelector(".playPauseIcon").src = "../images/pause-icon.svg";
            } else {
                audioPlayer.pause();
                playPauseIcon.src = "../images/play-track-small.svg";
                button.style.opacity = '0';
            }

            const trackId = playPauseButtons[index].getAttribute("data-track-id");
            localStorage.setItem("lastPlayedTrack", trackId);
        });

        progressBar.addEventListener("click", function (event) {
            let rect = progressBar.getBoundingClientRect();
            let offsetX = event.clientX - rect.left;
            let newProgress = (offsetX / progressBar.offsetWidth);
            audioPlayers[currentIndex].currentTime = newProgress * audioPlayers[currentIndex].duration;
        });
        updateBottomPanel(playPauseButtons[index].getAttribute("data-track-id"));
    }


    playPauseButtons.forEach((button, index) => {
        button.addEventListener("click", () => {
            const trackId = button.getAttribute("data-track-id");
            const audioPlayer = audioPlayers[index];
            const playPauseIcon = playPauseIcons[index];

            updateBottomPanel(trackId);

            if (currentAudioPlayer && currentAudioPlayer !== audioPlayer) {
                if (!currentAudioPlayer.paused) {
                    currentAudioPlayer.pause();
                    currentPlayPauseIcon.src = "../images/play-track-small.svg";
                    currentPlayPauseIcon.parentElement.style.opacity = '0';
                }
                currentAudioPlayer.currentTime = 0;
            }

            if (audioPlayer.paused || currentAudioPlayer !== audioPlayer) {
                audioPlayer.play();
                playPauseIcon.src = "../images/pause-track-small.svg";
                button.style.opacity = '1';
                currentAudioPlayer = audioPlayer;
                currentAudioPlayer.setAttribute("data-track-id", trackId);
                currentPlayPauseIcon = playPauseIcon;
            } else {
                audioPlayer.pause();
                playPauseIcon.src = "../images/play-track-small.svg";
                playPauseButton.querySelector(".playPauseIcon").src = "../images/play-icon.svg";
                button.style.opacity = '0';
                currentAudioPlayer = null;
                currentPlayPauseIcon = null;
            }
        });
    });

    audioPlayers.forEach((audioPlayer) => {
        audioPlayer.addEventListener("timeupdate", function () {
            if (!isNaN(audioPlayer.duration)) {
                let currentProgress = (audioPlayer.currentTime / audioPlayer.duration) * 100;
                progress.style.width = currentProgress + "%";

                circle.style.opacity = '0';

                let currentMinutes = Math.floor(audioPlayer.currentTime / 60);
                let currentSeconds = Math.floor(audioPlayer.currentTime % 60);
                let totalMinutes = Math.floor(audioPlayer.duration / 60);
                let totalSeconds = Math.floor(audioPlayer.duration % 60);

                currentTime.textContent = padDigits(currentMinutes) + ":" + padDigits(currentSeconds);
                totalTime.textContent = padDigits(totalMinutes) + ":" + padDigits(totalSeconds);
            }
        });

    });

    function updateProgressBarAndCircle(event) {
        let rect = progressBar.getBoundingClientRect();
        let offsetX = event.clientX - rect.left;
        circle.style.left = offsetX - circle.offsetWidth / 2 + "px";
        if (event.type === 'mousemove') {
            circle.style.opacity = '1';
            progressBar.style.backgroundColor = 'var(--white)';
        } else {
            circle.style.opacity = '0';
            progressBar.style.backgroundColor = '#cdcdcd36';
        }
    }

    progressBar.addEventListener("mousemove", updateProgressBarAndCircle);
    progressBar.addEventListener("mouseleave", updateProgressBarAndCircle);

    function padDigits(number) {
        return (number < 10) ? "0" + number : number;
    }

    volumeControl.addEventListener('input', function () {
        Array.from(audioPlayers).forEach(function (audio) {
            audio.volume = volumeControl.value;
        });

        localStorage.setItem("userVolume", volumeControl.value);
    });

    volumeControl.addEventListener('wheel', function (event) {
        event.preventDefault();
        let delta = event.deltaY || event.detail || -event.wheelDelta;
        let step = delta > 0 ? -0.1 : 0.1;
        volumeControl.value = Math.max(0, Math.min(1, parseFloat(volumeControl.value) + step));
        Array.from(audioPlayers).forEach(function (audio) {
            audio.volume = volumeControl.value;
        });
    });

    const storedVolume = localStorage.getItem("userVolume");
    if (storedVolume !== null) {
        volumeControl.value = storedVolume;
        Array.from(audioPlayers).forEach(function (audio) {
            audio.volume = volumeControl.value;
        });
    }
});

function updateBottomPanel(trackId) {
    fetch(`/${trackId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка хттп запроса! Статус: ${response.status}`);
            }
            return response.json();
        })
        .then(track => {
            displayTrackInfo(track);
            console.log(track)
        })
        .catch(reportError => {
            console.error('Ошибка извлекания данных трека бро: ', reportError);
        })
}

function displayTrackInfo(track) {
    document.querySelectorAll('.trackTitle').forEach(trackTitle => trackTitle.innerText = track.title);
    document.querySelectorAll('.artistsName').forEach(artistName => {
        artistName.innerText = track.artistsName;
        artistName.href = `/artist/${track.artistId}/details`;
    });
    document.querySelectorAll('.coverImage').forEach(image => image.src = `/uploads/${track.coverFilename}`);
    document.querySelector('.details-track').style.background = `url('/uploads/${track.coverFilename}')`;
    document.querySelectorAll('.details-track__genre').forEach(trackGenre => trackGenre.innerText = track.genre);
    document.querySelectorAll('.details-track__lyrics').forEach(trackLyrics => trackLyrics.innerText = track.lyric);
}

function listenToTrack(button) {
    let trackId = button.dataset.trackId;

    fetch(`/listen/${trackId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка HTTP запроса! Статус: ${response.status}`);
            }
            console.log("Track listened successfully");
        })
        .catch(error => {
            console.error('Ошибка при прослушивании трека: ', error);

        });
}

function updatePlayPauseButton(isPaused) {
    const playPauseButton = document.querySelector(".control-block-randBtn");
    const playPauseIcon = playPauseButton.querySelector(".control-block-randBtn-img");

    if (isPaused) {
        playPauseIcon.src = "../images/play-track-small.svg";
    } else {
        playPauseIcon.src = "../images/pause-track-small.svg";
    }
}

function playRandomTrack() {
    fetch("/random-track")
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(track => {
            displayTrackInfo(track);

            if (currentAudioPlayer) {
                if (currentAudioPlayer.paused || currentAudioPlayer.getAttribute("data-track-id") !== track.id) {
                    playTrackById(track.id);
                    updatePlayPauseButton(false);
                } else {
                    currentAudioPlayer.paused ? currentAudioPlayer.play() : currentAudioPlayer.pause();
                    updatePlayPauseButton(!currentAudioPlayer.paused);
                }
            } else {
                playTrackById(track.id);
                updatePlayPauseButton(false);
            }
        })
        .catch(error => {
            console.error('Error fetching random track:', error);
        });
}

function playTrackById(trackId) {
    const playButton = document.querySelector(`.tracks-main__btn[data-track-id="${trackId}"]`);
    playButton.click();
}

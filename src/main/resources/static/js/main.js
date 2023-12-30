let audio = document.getElementById("myAudio");
let progressBar = document.querySelector(".progress-bar");
let progress = document.querySelector(".progress");
let circle = document.querySelector(".circle");
let currentTime = document.querySelector(".current-time");
let totalTime = document.querySelector(".total-time");
let playButton = document.querySelector("button img#playPauseIcon");
let playButtonSlider = document.querySelector("button img#playPauseIconSlider");
let muteButton = document.querySelector("button img#muteIcon");
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
        button.addEventListener("click", () => {
            currentIndex = index;
            playTrack(index);
        });
    });

    prevButton.addEventListener("click", () => {
        currentIndex = (currentIndex - 1 + playPauseButtons.length) % playPauseButtons.length;
        playTrack(currentIndex);
    });

    nextButton.addEventListener("click", () => {
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
                button.style.opacity = '0';
                currentAudioPlayer = null;
                currentPlayPauseIcon = null;
            }
        });
    });

    audioPlayers.forEach((audioPlayer, index) => {
        audioPlayer.addEventListener("ended", () => {
            const playPauseIcon = playPauseIcons[index];
            playPauseIcon.src = "../images/play-track-small.svg";
            playPauseIcon.parentElement.style.opacity = '0';
            currentAudioPlayer = null;
            updateBottomPanel(audioPlayer.getAttribute("data-track-id"));
        });
    });

    audioPlayers.forEach((audioPlayer, index) => {
        audioPlayer.addEventListener("timeupdate", function () {
            if (!isNaN(audioPlayer.duration)) {
                let currentProgress = (audioPlayer.currentTime / audioPlayer.duration) * 100;
                progress.style.width = currentProgress + "%";

                circle.style.left = (currentProgress * progressBar.offsetWidth) / 103 + "px";

                let currentMinutes = Math.floor(audioPlayer.currentTime / 60);
                let currentSeconds = Math.floor(audioPlayer.currentTime % 60);
                let totalMinutes = Math.floor(audioPlayer.duration / 60);
                let totalSeconds = Math.floor(audioPlayer.duration % 60);

                currentTime.textContent = padDigits(currentMinutes) + ":" + padDigits(currentSeconds);
                totalTime.textContent = padDigits(totalMinutes) + ":" + padDigits(totalSeconds);
            }
        });

    });

    progressBar.addEventListener("mousemove", function (event) {
        let rect = progressBar.getBoundingClientRect();
        let offsetX = event.clientX - rect.left;
        circle.style.left = offsetX - circle.offsetWidth / 2 + "px";
    });

    function padDigits(number) {
        return (number < 10) ? "0" + number : number;
    }

    volumeControl.addEventListener('input', function () {
        Array.from(audioPlayers).forEach(function (audio) {
           audio.volume = volumeControl.value;
        });
    });
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
        })
        .catch(reportError => {
            console.error('Ошибка извлекания данных трека бро: ', reportError);
        })
}

function displayTrackInfo(track) {
    document.querySelectorAll('.trackTitle').forEach(trackTitle => trackTitle.innerText = track.title);
    document.querySelectorAll('.artistsName').forEach(artistName => artistName.innerText = track.artistsName);
    document.querySelectorAll('.coverImage').forEach(image => image.src = `/uploads/${track.coverFilename}`);
    document.querySelector('.details-track').style.background = `url('/uploads/${track.coverFilename}')`;
    document.querySelectorAll('.details-track__genre').forEach(trackGenre => trackGenre.innerText = track.genre);
    document.querySelectorAll('.details-track__lyrics').forEach(trackLyrics => trackLyrics.innerText = track.lyric);
}



//
// function toggleMute() {
//     if (currentAudioPlayer) {
//         currentAudioPlayer.muted = !currentAudioPlayer.muted;
//         document.getElementById('muteIcon').src = currentAudioPlayer.muted
//             ? "../images/sound-muted-icon.svg"
//             : "../images/sound-active-icon.svg";
//     }
// }


// ////////Slider/////////
// var swiper = new Swiper('.swiper', {
//     // Настройки Swiper
//     slidesPerView: 4,
//     spaceBetween: 20,
//     navigation: {
//         nextEl: '.main__AllTracks-slider-arrow-right',
//         prevEl: '.main__AllTracks-slider-arrow-left'
//     },
//     mousewheel: false,
//     loop: false
// });
//
//
// //////Размытие/////
//
// window.addEventListener("DOMContentLoaded", function (event) {
//     var content = document.getElementById("content");
//     setTimeout(function () {
//         content.classList.remove("hidden");
//         content.style.opacity = 1;
//     }, 3000);
// });
//

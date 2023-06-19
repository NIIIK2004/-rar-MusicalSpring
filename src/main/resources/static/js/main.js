let audio = document.getElementById("myAudio");
let progressBar = document.querySelector(".progress-bar");
let progress = document.querySelector(".progress");
let circle = document.querySelector(".circle");
let currentTime = document.querySelector(".current-time");
let totalTime = document.querySelector(".total-time");
let playButton = document.querySelector("button img#playPauseIcon");
let playButtonSlider = document.querySelector("button img#playPauseIconSlider");
let muteButton = document.querySelector("button img#muteIcon");

function updatePlayButtonState() {
    if (audio.paused) {
        playButton.src = "../images/play-icon.svg";
        playButton.alt = "Воспроизвести";
    } else {
        playButton.src = "../images/stop-icon.svg";
        playButton.alt = "Остановить";
    }
}

function updatePlayButtonStateSlider() {
    if (audio.paused) {
        playButtonSlider.src = "../images/play-icon-for-slider.svg";
        playButtonSlider.alt = "Воспроизвести";
    } else {
        playButtonSlider.src = "../images/stop-icon-for-slider.svg";
        playButtonSlider.alt = "Остановить";
    }
}

function toggleAudioSlider(event) {
    event.stopPropagation();
    if (audio.paused) {
        audio.play();
    } else {
        audio.pause();
    }
    updatePlayButtonStateSlider();
    updatePlayButtonState();
}

function toggleAudio(event) {
    event.stopPropagation();
    if (audio.paused) {
        audio.play();
    } else {
        audio.pause();
    }
    updatePlayButtonState();
    updatePlayButtonStateSlider();
}

audio.addEventListener("play", updatePlayButtonState, updatePlayButtonStateSlider);
audio.addEventListener("pause", updatePlayButtonState, updatePlayButtonStateSlider);
audio.addEventListener("ended", nextTrack);
updatePlayButtonState();
updatePlayButtonStateSlider();

function toggleMute() {
    if (audio.muted) {
        audio.muted = false;
        muteButton.src = "../images/sound-active-icon.svg";
        muteButton.alt = "Звук включен";
    } else {
        audio.muted = true;
        muteButton.src = "../images/sound-mute-icon.svg";
        muteButton.alt = "Звук выключен";
    }
}

let trackIndex = 0;
let tracks = [
    "../audio/dzhizus-manyak-mp3.mp3",
    "../audio/muzvolna_net__dzhizus_vo_imya_lyubvi.mp3",
    "../audio/Nirvana_-_Smells_Like_Teen_Spirit_(musmore.com).mp3"
];

function playTrack() {
    audio.src = tracks[trackIndex];
    audio.play();
    updatePlayButtonState();
    updatePlayButtonStateSlider();
}

function nextTrack() {
    trackIndex++;
    if (trackIndex >= tracks.length) {
        trackIndex = 0;
    }
    playTrack();
}

function previousTrack() {
    trackIndex--;
    if (trackIndex < 0) {
        trackIndex = tracks.length - 1;
    }
    playTrack();
}

progressBar.addEventListener("click", function (event) {
    let rect = progressBar.getBoundingClientRect();
    let offsetX = event.clientX - rect.left;
    let newProgress = (offsetX / progressBar.offsetWidth) * 100;
    audio.currentTime = (newProgress / 100) * audio.duration;
});

progressBar.addEventListener("mousemove", function (event) {
    let rect = progressBar.getBoundingClientRect();
    let offsetX = event.clientX - rect.left;
    let newProgress = (offsetX / progressBar.offsetWidth) * 100;
    circle.style.left = offsetX - circle.offsetWidth / 2 + "px";
});


audio.addEventListener("timeupdate", function () {
    if (!isNaN(audio.duration)) { // Проверяем, доступно ли значение duration
        let currentProgress = (audio.currentTime / audio.duration) * 100;
        progress.style.width = currentProgress + "%";

        let currentMinutes = Math.floor(audio.currentTime / 60);
        let currentSeconds = Math.floor(audio.currentTime % 60);
        let totalMinutes = Math.floor(audio.duration / 60);
        let totalSeconds = Math.floor(audio.duration % 60);

        currentTime.textContent = padDigits(currentMinutes) + ":" + padDigits(currentSeconds);
        totalTime.textContent = padDigits(totalMinutes) + ":" + padDigits(totalSeconds);
    }
});

function padDigits(number) {
    return (number < 10) ? "0" + number : number;
}

// Обнуление значения прогресса воспроизведения при загрузке страницы
window.addEventListener('load', function () {
    audio.currentTime = 0;
});

// Функция для сохранения состояния прогресса воспроизведения
function savePlaybackProgress() {
    localStorage.setItem('playbackProgress', audio.currentTime);
}

function restorePlaybackProgress() {
    let progress = localStorage.getItem('playbackProgress');
    if (progress) {
        audio.currentTime = parseFloat(progress);
    }
}

audio.addEventListener('timeupdate', savePlaybackProgress);
window.addEventListener('load', restorePlaybackProgress);

////////Slider/////////
var swiper = new Swiper('.swiper', {
    // Настройки Swiper
    slidesPerView: 4,
    spaceBetween: 20,
    navigation: {
        nextEl: '.main__AllTracks-slider-arrow-right',
        prevEl: '.main__AllTracks-slider-arrow-left'
    },
    mousewheel: false,
    loop: false
});


//////Размытие/////

window.addEventListener("DOMContentLoaded", function (event) {
    var content = document.getElementById("content");
    setTimeout(function () {
        content.classList.remove("hidden");
        content.style.opacity = 1;
    }, 3000);
});

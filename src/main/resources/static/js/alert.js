const repeatBtns = document.querySelectorAll('.action_btn-repeat');
const shuffleBtns = document.querySelectorAll('.action_btn-shuffle');
const moreBtns = document.querySelectorAll('.action_btn-more');
const controlBlockGenre = document.querySelectorAll('.control-block-genre');

const subsWarn = document.querySelector('.subs__warn');
const noWorkWarn = document.querySelector('.no-work__warn');

const closebtn = document.querySelectorAll('.subs__warn-close');
const noWorkClose = document.querySelectorAll('.noWork-close');

//Алерт нижней панели
repeatBtns.forEach((btn) => {
    btn.addEventListener('click', addSubsWarn);
});

shuffleBtns.forEach((btn) => {
    btn.addEventListener('click', addSubsWarn);
});

moreBtns.forEach((btn) => {
    btn.addEventListener('click', addSubsWarn);
});

closebtn.forEach((btn) => {
    btn.addEventListener('click', removeSubsWarn);
})

function addSubsWarn() {
    subsWarn.classList.add('visible');
}

function removeSubsWarn() {
    subsWarn.classList.remove('visible');
}

//Алерт функции жанров
controlBlockGenre.forEach((btn) => {
    btn.addEventListener('click', addNoWorkWarn);
});

noWorkClose.forEach((btn) => {
    btn.addEventListener('click', removeNoWorkWarn);
})

function addNoWorkWarn() {
    noWorkWarn.classList.add('visible');
}
function removeNoWorkWarn() {
    noWorkWarn.classList.remove('visible');
}

document.addEventListener("DOMContentLoaded", function () {
    let messageElement = document.getElementById('user-data-message');
    if (messageElement) {
        setTimeout(function () {
            messageElement.classList.add('hide');
        }, 5000);

        let closeLink = document.getElementById('close-link');
        if (closeLink) {
            closeLink.addEventListener('click', function (e) {
                e.preventDefault();
                messageElement.classList.add('hide');
            });
        }
    }
});

document.addEventListener("DOMContentLoaded", function() {
    const banner = document.querySelector(".ads-banner");
    const closeButton = document.querySelector(".close-button");

    closeButton.addEventListener("click", function(event) {
        event.preventDefault();
        banner.classList.add('fade-out');
        // banner.style.display = "none";

        setTimeout(function() {
            banner.style.display = "none";
            banner.classList.remove('fade-out');
        }, 500);
    });
});


const showMore = document.querySelector('.tracks-main__more');
const itemsLength = document.querySelectorAll('.tracks-main__item').length;
const warnNotification  = document.querySelector(".warn__notification");
const body = document.querySelector('.body');

const fullscreenButton = document.getElementById("fullscreen-container");
const maximizeIcon = document.querySelector("#fullscreen-container img");
const detailsTrack = document.querySelector(".details-track");
const bottomPanel = document.querySelector(".bottom_panel");

let items = 7;

//Показать еще треки
showMore.addEventListener('click', () => {
    items += 7;
    const array = Array.from(document.querySelector('.tracks-main__list').children);
    const visibleItems = array.slice(0, items);

    visibleItems.forEach(el => el.classList.add('is-visible'));
    if (visibleItems.length === itemsLength) {
        showMore.style.display = 'none';
    }
});

//Изменение темы на сервисе
function animate() {
    const theme = localStorage.getItem('theme');

    if (maximizeIcon.src.includes("maximize-icon.svg")) {
        bottomPanel.style.backgroundColor = 'var(--black-layer)';
        bottomPanel.style.borderRadius = 'unset';
        bottomPanel.style.padding = '20px 0px';
        bottomPanel.style.scale = 'unset';
        bottomPanel.style.marginBottom = 'unset';
    } else {
        if (theme === 'light') {
            bottomPanel.style.backgroundColor = 'var(--black-layer)';
            bottomPanel.style.borderRadius = '20px';
            bottomPanel.style.padding = '20px 40px';
            bottomPanel.style.scale = '.95';
            bottomPanel.style.marginBottom = '20px';
        } else {
            bottomPanel.style.backgroundColor = 'transparent';
        }
    }

    requestAnimationFrame(animate);
}

//Фуллскрин треков
fullscreenButton.addEventListener("click", function (event) {
    event.preventDefault();
    if (maximizeIcon.src.includes("maximize-icon.svg")) {
        maximizeIcon.src = "../images/minimize-icon.svg";
        detailsTrack.classList.add("open");
        document.body.classList.add('body--active');
        warnNotification.style.opacity = "1";
        setTimeout(function () {
            warnNotification.style.opacity = "0";
        }, 2000);

    } else {
        maximizeIcon.src = "../images/maximize-icon.svg";
        detailsTrack.classList.remove("open");
        document.body.classList.remove('body--active');
    }
});

requestAnimationFrame(animate);


//Переключатель в фуллскрине между "Текстом" и "Подробным"
document.addEventListener("DOMContentLoaded", function () {
    const tabButtons = document.querySelectorAll(".details-track__btn");
    const tabContents = document.querySelectorAll(".details-track__tab");

    tabButtons[0].classList.add("active");
    tabContents[0].classList.add("active");

    tabButtons.forEach(function (button, index) {
        button.addEventListener("click", function () {
            tabButtons.forEach(function (btn) {
                btn.classList.remove("active");
            });
            tabContents.forEach(function (content) {
                content.classList.remove("active");
            });
            button.classList.add("active");
            tabContents[index].classList.add("active");
        });
    });
});

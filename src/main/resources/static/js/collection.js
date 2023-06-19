window.addEventListener("DOMContentLoaded", function (event) {
    let content = document.getElementById("content");
    setTimeout(function () {
        content.classList.add("show");
    });
});

setTimeout(function () {
    let offer = document.querySelector(".offer");
    offer.classList.add("showOffer");
}, 1000);
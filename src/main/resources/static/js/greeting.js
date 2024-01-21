document.addEventListener("DOMContentLoaded", function () {
    updateGreeting();
    setInterval(updateGreeting, 60000);

    function updateGreeting() {
        let currentTime = new Date();
        let hours = currentTime.getHours();
        let greetingElement = document.getElementById("greeting");

        if (hours >= 5 && hours < 12) {
            greetingElement.textContent = "Доброе утро, ";
        } else if (hours >= 12 && hours < 18) {
            greetingElement.textContent = "Добрый день, ";
        } else if (hours >= 18 && hours < 23) {
            greetingElement.textContent = "Добрый вечер, ";
        } else {
            greetingElement.textContent = "Доброй ночи, ";
        }
    }


})
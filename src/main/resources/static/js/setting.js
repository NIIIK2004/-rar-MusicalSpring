document.addEventListener("DOMContentLoaded", () => {
    const nameInput = document.getElementById("name");
    nameInput.addEventListener("input", () => {
        const formData = new FormData(document.querySelector('.setting-form'));
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/setting/validate");
        xhr.onload = () => {
            if (xhr.status === 200) {
                const errors = JSON.parse(xhr.responseText);
                const errorContainer = document.getElementById("error-container");
                errorContainer.innerHTML = ""; // Очищаем контейнер ошибок
                errors.forEach(error => {
                    const errorElement = document.createElement("div");
                    errorElement.textContent = error;
                    errorContainer.appendChild(errorElement); // Добавляем каждую ошибку в контейнер
                });
            }
        };
        xhr.send(formData);
    });
});

/*

const searchInput = document.querySelector('.search_tracks');
searchInput.addEventListener('input', function() {
    const searchTerm = searchInput.value;
    // Отправка запроса на сервер
    fetch(`/artist/search?name=${searchTerm}`)
        .then(response => response.json())
        .then(data => {
            // Обновление результатов поиска на основе полученных данных
            // Пример: обновление списка результатов на странице
            const searchResultsContainer = document.querySelector('.search__track-wrapper');
            searchResultsContainer.innerHTML = '';

            data.forEach(artist => {
                const artistElement = document.createElement('div');
                artistElement.classList.add('search__track-item');
                artistElement.textContent = artist.name;
                searchResultsContainer.appendChild(artistElement);
            });
        })
        .catch(error => {
            console.error('Произошла ошибка при отправке запроса: ', error);
        });
});


*/

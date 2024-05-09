const deleteTrackLinks = document.querySelectorAll('.playlist-track-delete');

const deletePhotoLinks = document.querySelectorAll('.album__photo a');

const subscribeForms = document.querySelectorAll('.Artist__btns form');

deleteTrackLinks.forEach(link => {
    link.addEventListener('click', function(event) {
        event.preventDefault();

        const url = this.getAttribute('href');
        const trackContainer = this.closest('li');

        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                trackContainer.style.display = 'none';
            })
            .catch(error => {
                console.error('There was a problem with your fetch operation:', error);
                console.error('Server response was not in JSON format');
            });

    });
});
deletePhotoLinks.forEach(link => {
    link.addEventListener('click', function(event) {
        event.preventDefault();

        const url = this.getAttribute('href');
        const photoContainer = this.closest('.album__photo');

        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                photoContainer.style.display = 'none';
            })
            .catch(error => {
                console.error('There was a problem with your fetch operation:', error);
            });
    });
});
subscribeForms.forEach(form => {
    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const url = this.getAttribute('action');
        const formData = new FormData(this);

        fetch(url, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const button = this.querySelector('button');
                if (button.classList.contains('Artist__btns-subs')) {
                    button.classList.remove('Artist__btns-subs');
                    button.classList.add('Artist__btns-unsubs');
                    button.textContent = 'Отписаться';
                } else {
                    button.classList.remove('Artist__btns-unsubs');
                    button.classList.add('Artist__btns-subs');
                    button.textContent = 'Подписаться';
                }
            })
            .catch(error => {
                console.error('There was a problem with your fetch operation:', error);
            });
    });
});
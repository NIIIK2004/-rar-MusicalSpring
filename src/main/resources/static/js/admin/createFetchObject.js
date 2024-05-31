document.getElementById('submitButton').addEventListener('click', function(event) {
    event.preventDefault();
    let form = document.getElementById('trackForm');
    let formData = new FormData(form);

    fetch('/alltrack/saveOrUpdate', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            let errorDiv = document.getElementById('user-data-message');
            errorDiv.innerHTML = '';
            let h1 = document.createElement('h1');
            h1.textContent = 'Найдены ошибки при добавлении:';
            errorDiv.appendChild(h1);
            if (data.errors) {
                data.errors.forEach(error => {
                    let p = document.createElement('p');
                    p.textContent = error;
                    errorDiv.appendChild(p);
                    errorDiv.style.opacity = '1';

                    setTimeout(function() {
                        errorDiv.style.opacity = '0';
                    }, 3500);
                });
            } else {
                window.location.href = data.redirectUrl;
            }
        })
        .catch(error => console.error('Error:', error));
});


document.getElementById('submitButton').addEventListener('click', function(event) {
    event.preventDefault();
    let form = document.getElementById('artistForm');
    let formData = new FormData(form);

    fetch('/artist/createOrEditArtist', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            let errorDiv = document.getElementById('user-data-message');
            errorDiv.innerHTML = '';
            let h1 = document.createElement('h1');
            h1.textContent = 'Найдены ошибки при добавлении:';
            errorDiv.appendChild(h1);

            if (data.errors) {
                data.errors.forEach(error => {
                    let p = document.createElement('p');
                    p.textContent = error;
                    errorDiv.appendChild(p);
                });
                errorDiv.style.opacity = '1';

                setTimeout(function() {
                    errorDiv.style.opacity = '0';
                }, 3500);
            } else {
                window.location.href = data.redirectUrl;
            }
        })
        .catch(error => console.error('Error:', error));
});
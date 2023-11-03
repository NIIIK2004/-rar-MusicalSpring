window.addEventListener('DOMContentLoaded', function () {
    var video = document.getElementById('welcome-video');
    var registrationForm = document.getElementById('registration-form');

    document.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            showRegistrationForm();
        }
    });

    video.addEventListener('ended', function () {
        showRegistrationForm();
    });

    function showRegistrationForm() {
        video.style.display = 'none';
        registrationForm.style.opacity = '1';
        registrationForm.style.pointerEvents = 'auto';
    }
});


const forms = document.querySelectorAll('.autorizated__wrapper');
let currentIndex = 0;

function showNextForm() {
    forms[currentIndex].classList.add('hidden');
    currentIndex = (currentIndex + 1) % forms.length;
    forms[currentIndex].classList.remove('hidden');
}

document.querySelectorAll('.autorizated__wrapper-next').forEach((button) => {
    button.addEventListener('click', (event) => {
        event.preventDefault()
        showNextForm();
    });
})

forms[currentIndex].classList.remove('hidden');



function previewFile() {
    const preview = document.getElementById('preview');
    const file = document.querySelector('input[type=file]').files[0];
    const reader = new FileReader();

    reader.onloadend = function () {
        if (file) {
            preview.src = reader.result;
        }
    }

    if (file) {
        reader.readAsDataURL(file);
    }
}
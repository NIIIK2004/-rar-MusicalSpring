const user = {};
window.addEventListener('DOMContentLoaded', function () {
    let video = document.getElementById('welcome-video');
    let registrationForm = document.getElementById('registration-form');

    document.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            showRegistrationForm();
        }
    });

    video.addEventListener('ended', function () {
        showRegistrationForm();
    });

    const hasVisited = localStorage.getItem('hasVisited');

    if (!hasVisited) {
        localStorage.setItem('hasVisited', 'true');
    } else {
        showRegistrationForm();
    }

    function showRegistrationForm() {
        video.style.display = 'none';
        registrationForm.style.opacity = '1';
        registrationForm.style.pointerEvents = 'auto';

        const inputs = registrationForm.querySelectorAll('input, textarea');
        inputs.forEach((input) => {
            user[input.name] = input.value;
        });

        currentIndex = 0;
        forms[currentIndex].classList.remove('hidden');
    }
});

const forms = document.querySelectorAll('.autorizated__wrapper');
let currentIndex = 0;

function showNextForm() {
    const currentForm = forms[currentIndex];
    const inputs = currentForm.querySelectorAll('input, textarea');
    inputs.forEach((input) => {
        user[input.name] = input.value;
    });

    currentIndex = (currentIndex + 1) % forms.length;
    const nextForm = forms[currentIndex];
    nextForm.classList.remove('hidden');
    currentForm.classList.add('hidden');
}

document.querySelectorAll('.autorizated__wrapper-next').forEach((button) => {
    button.addEventListener('click', (event) => {
        event.preventDefault();
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
document.querySelectorAll('.autorizated__wrapper-back').forEach((button) => {
    button.addEventListener('click', (event) => {
        event.preventDefault()
        showPreviousForm();
    });
})

function showPreviousForm() {
    const currentForm = forms[currentIndex];
    const inputs = currentForm.querySelectorAll('input, textarea');
    inputs.forEach((input) => {
        user[input.name] = input.value;
    });

    currentIndex = (currentIndex - 1 + forms.length) % forms.length;
    const previousForm = forms[currentIndex];
    previousForm.classList.remove('hidden');
    currentForm.classList.add('hidden');
}

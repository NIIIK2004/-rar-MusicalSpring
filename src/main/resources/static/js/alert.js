// Получаем ссылки на все кнопки
const repeatBtns = document.querySelectorAll('.action_btn-repeat');
const shuffleBtns = document.querySelectorAll('.action_btn-shuffle');
const moreBtns = document.querySelectorAll('.action_btn-more');
const subsWarn = document.querySelector('.subs__warn');
const closebtn = document.querySelectorAll('.subs__warn-close');


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
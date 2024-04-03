function goBack() {
    window.history.back();
}

function updateFileName() {
    let input = document.getElementById('audioFile');
    let fileNameSpan = document.getElementById('audioFileName');

    if (input.files.length > 0) {
        fileNameSpan.textContent = input.files[0].name;
    } else {
        fileNameSpan.textContent = 'Загрузите файл';
    }
}

let inputElement = document.getElementById("photosFile");
let fileCountElement = document.getElementById("fileNameOrCount");

inputElement.addEventListener("change", function (event) {
    let fileList = event.target.files;

    if (fileList.length > 0) {
        if (fileList.length > 1) {
            fileCountElement.textContent = fileList.length + " файлов выбрано";
        } else {
            fileCountElement.textContent = "Выбран файл: " + fileList[0].name;
        }
    } else {
        fileCountElement.textContent = "";
    }
});

let fileInput = document.getElementById("files");
let uploadForm = document.getElementById("uploadForm");

fileInput.addEventListener("change", function () {
    uploadForm.submit();
});

function previewFile() {
    const preview = document.getElementById('preview');
    const file = document.querySelector('input[type=file]').files[0];
    const reader = new FileReader();

    reader.onloadend = function () {
        if (file) {
            preview.src = reader.result;
        }
    }

    reader.onloadend = function () {
        if (file) {
            preview.src = reader.result;
        }
    };

    if (file) {
        reader.readAsDataURL(file);
    }
}



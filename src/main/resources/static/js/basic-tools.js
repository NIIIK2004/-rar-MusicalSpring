function goBack() {
    window.history.back();
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
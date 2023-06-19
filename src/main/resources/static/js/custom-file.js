updateFileAudio();
updateFilePhoto();
updateFileClip();
updateFileName();

function updateFileName(event) {
    let inputphoto = document.getElementById('photo');
    let fileNamePhoto = inputphoto.files[0].name;
    let fileLabelPhoto = document.getElementById('file-upload-label');

    fileLabelPhoto.innerHTML = fileNamePhoto;
}

function updateFileAudio() {
    let inputaudio = document.getElementById('audio');
    let fileNameaudio = inputaudio.files[0].name;
    let fileLabelaudio = document.getElementById('file-upload-audio');

    fileLabelaudio.innerHTML = fileNameaudio;
}

function updateFilePhoto() {
    let inputphoto = document.getElementById('cover');
    let fileNamephoto = inputphoto.files[0].name;
    let fileLabelphoto = document.getElementById('file-upload-photo');

    fileLabelphoto.innerHTML = fileNamephoto;

}

function updateFileClip() {
    let inputclip = document.getElementById('clip');
    let fileNameclip = inputclip.files[0].name;
    let fileLabelclip = document.getElementById('file-upload-clip');

    fileLabelclip.innerHTML = fileNameclip;
}
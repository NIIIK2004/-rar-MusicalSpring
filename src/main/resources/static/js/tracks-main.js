const showMore = document.querySelector('.tracks-main__more');
const itemsLength = document.querySelectorAll('.tracks-main__item').length;
const modalEditRelease = document.getElementById('modal-edit_release');
const modalDeleteRelease = document.getElementById('modal-delete_release');
const body = document.querySelector('.body');

const fullscreenButton = document.getElementById("fullscreen-container");
const maximizeIcon = document.querySelector("#fullscreen-container img");
const detailsTrack = document.querySelector(".details-track");

let curr_track_id = -1;
let items = 6;

showMore.addEventListener('click', () => {
    items += 6;
    const array = Array.from(document.querySelector('.tracks-main__list').children);
    const visibleItems = array.slice(0, items);

    visibleItems.forEach(el => el.classList.add('is-visible'));
    if (visibleItems.length === itemsLength) {
        showMore.style.display = 'none';
    }
});

fullscreenButton.addEventListener("click", function (event) {
    event.preventDefault();
    if (maximizeIcon.src.includes("maximize-icon.svg")) {
        maximizeIcon.src = "../images/minimize-icon.svg";
        detailsTrack.classList.add("open");
        document.body.classList.add('body--active');
    } else {
        maximizeIcon.src = "../images/maximize-icon.svg";
        detailsTrack.classList.remove("open");
        document.body.classList.remove('body--active');
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const tabButtons = document.querySelectorAll(".details-track__btn");
    const tabContents = document.querySelectorAll(".details-track__tab");

    tabButtons[0].classList.add("active");
    tabContents[0].classList.add("active");

    tabButtons.forEach(function (button, index) {
        button.addEventListener("click", function () {
            tabButtons.forEach(function (btn) {
                btn.classList.remove("active");
            });
            tabContents.forEach(function (content) {
                content.classList.remove("active");
            });
            button.classList.add("active");
            tabContents[index].classList.add("active");
        });
    });
});

function modal_addRelease() {
    modal_RealiseOpen("Добавление релиза");
    $("#title").val("")
    $("#description").val("")
    $("#lyrics").val("")
    $("#photo").val("")
    $("#audio").val("")
    $("#genre").val("")
    $("#releaseyear").val("")
    $("#clip").val("")
    // $("#artist_id").val("")
    curr_track_id = -1;
}

function modal_saveRelease() {
    let formData = new FormData();
    formData.append("id", curr_track_id);
    formData.append('title', $("#title").val());
    formData.append('description', $("#description").val());
    formData.append('lyrics', $("#lyrics").val());
    formData.append('genre', $("#genre").val());
    formData.append('releaseyear', $("#releaseyear").val())

    formData.append('audiofilename', $("#audio").val());
    formData.append('audio', $('#audio')[0].files[0]);

    formData.append('coverfilename', $('#cover').val());
    formData.append('cover', $('#cover')[0].files[0]);

    formData.append('clipfilename', $('#clip').val());
    formData.append('clip', $('#clip')[0].files[0]);

    formData.append('artist_id', $("#artistId").val());

    if (curr_track_id === -1) {
        formData.delete('id');
    }

    $.ajax({
        method: "POST",
        url: curr_track_id === -1 ? "/alltrack/save" : `/alltrack/edit?id=${curr_track_id}`,
        data: formData,
        processData: false,
        contentType: false,
    }).done(function () {
        modal_releaseClose();
        window.location.reload();
    });
}

function modal_EditRelease(event) {
    modal_RealiseOpen("Редактирование релиза");

    let track_id = $(event.target).closest("button").attr('data-track-id');
    let track_title = $(event.target).closest("button").attr('data-track-title');
    let track_description = $(event.target).closest("button").attr('data-track-description');
    let track_lyrics = $(event.target).closest("button").attr('data-track-lyrics');
    let track_filenamecover = $(event.target).closest("button").attr('data-track-filenamecover');
    let track_filenameaudio = $(event.target).closest("button").attr('data-track-filenameaudio');
    let track_genre = $(event.target).closest("button").attr('data-track-genre');
    let track_releaseyear = $(event.target).closest("button").attr('data-track-releaseyear');
    let track_filenameclip = $(event.target).closest("button").attr('data-track-filenameclip');
    let track_artists = $(event.target).closest("button").attr('data-track-artists');
    let artistId = $("#artistId").val();

    curr_track_id = track_id;
    $("#title").val(track_title);
    $("#description").val(track_description);
    $("#lyrics").val(track_lyrics);
    $("#cover").val(track_filenamecover);
    $("#audio").val(track_filenameaudio);
    $("#genre").val(track_genre);
    $("#releaseyear").val(track_releaseyear);
    $("#clip").val(track_filenameclip);
    $("#artistId").val(artistId);

    let formData = new FormData();
    formData.append("id", track_id);
    formData.append("title", track_title);
    formData.append("description", track_description);
    formData.append("lyrics", track_lyrics);
    formData.append("genre", track_genre);
    formData.append("releaseyear", track_releaseyear);
    formData.append("artist_id", artistId);

    let fileInputAudio = $("#audio")[0];
    if (fileInputAudio.files.length > 0) {
        formData.append('audio', fileInputAudio.files[0]);
    } else {
        formData.append('audiofilename', track_filenameaudio);
    }

    let fileInputCover = $("#cover")[0];
    if (fileInputCover.files.length > 0) {
        formData.append('cover', fileInputCover.files[0]);
    } else {
        formData.append('coverfilename', track_filenamecover);
    }

    let fileInputClip = $("#clip")[0];
    if (fileInputClip.files.length > 0) {
        formData.append('clip', fileInputClip.files[0]);
    } else {
        formData.append('clipfilename', track_filenameclip);
    }
}

function modal_RealiseOpen(titleText) {
    $("#edit_modal__title").html(titleText);
    body.classList.toggle('body--active');
    modalEditRelease.classList.toggle('modal--active');
}

function modal_releaseClose() {
    body.classList.remove('body--active');
    modalEditRelease.classList.remove('modal--active');
    curr_track_id = -1;
    $("#name-release").val("");
    $("#price-release").val("");
    $("#description-release").val("");
    $(".modal").scrollTop(0);
}

function modal_DeleteRelease() {
    console.log("open")
    let track_id = $(event.target).closest("button").attr("data-track-id");
    let track_title = $(event.target).closest("button").attr("data-track-title");

    body.classList.toggle('body--active');
    modalDeleteRelease.classList.toggle('modal--active');

    curr_track_id = track_id;
    $("#track_name-delete").html(track_title);
}

function modal_DeleteReleaseClose() {
    console.log("close")
    body.classList.remove('body--active');
    modalDeleteRelease.classList.remove('modal--active');
    curr_track_id = -1;
}

function deleteRelease() {
    window.location.href = `/delete/${curr_track_id}`;
}

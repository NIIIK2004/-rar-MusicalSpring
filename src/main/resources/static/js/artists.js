const modalEditArtist = document.getElementById('modal-edit_artist');
const modalDeleteArtist = document.getElementById('modal-delete_Artist');
const body = document.body;
let curr_artist_id = -1;

function modal_addArtist() {
    modal_ArtistOpen("Добавление нового артиста");
    $("#description").val("");
    curr_artist_id = -1;
}

function modal_saveArtist() {
    let formData = new FormData();
    formData.append('id', curr_artist_id);
    formData.append('name', $("#name").val());
    formData.append('description', $("#description").val());
    formData.append('genre', $("#genre").val());
    formData.append('listeners', $("#listeners").val());
    formData.append('filename', $("#photo").val());
    formData.append('photo', $("#photo")[0].files[0]);

    if (curr_artist_id === -1) {
        formData.delete('id');
    }

    $.ajax({
        method: "POST",
        url: curr_artist_id === -1 ? "/allartist/save" : `/allartist/edit?id=${curr_artist_id}`,
        data: formData,
        processData: false,
        contentType: false
    }).done(function () {
        modal_ArtistClose();
        window.location.reload();
    });
}

    function modal_EditArtist(event) {
        modal_ArtistOpen("Редактирование Артиста");
        let artist_id = $(event.target).closest("button").attr('data-artist-id');
        let artist_name = $(event.target).closest("button").attr('data-artist-name');
        let artist_description = $(event.target).closest("button").attr('data-artist-description');
        let artist_genre = $(event.target).closest("button").attr('data-artist-genre');
        let artist_listeners = $(event.target).closest("button").attr('data-artist-listeners');
        let artist_filename = $(event.target).closest("button").attr('data-artist-filename');

        $("#name").val(artist_name);
        $("#description").val(artist_description);
        $("#genre").val(artist_genre);
        $("#listeners").val(artist_listeners);
        $("#file-upload-label").text(artist_filename);

        let formData = new FormData();
        formData.append('id', artist_id);
        formData.append('name', $("#name").val());
        formData.append('description', $("#description").val());
        formData.append('genre', $("#genre").val());
        formData.append('listeners', $("#listeners").val());

        let fileInput = $("#photo")[0];
        if (fileInput.files.length > 0) {
            formData.append('photo', fileInput.files[0]);
        } else {
            formData.append('filename', artist_filename);
        }
        curr_artist_id = artist_id;
    }

function modal_ArtistOpen(titleText) {
    $("#edit_modal__title").html(titleText);
    body.classList.toggle('body--active');
    modalEditArtist.classList.toggle('modal--active');
}

function modal_DeleteArtist() {
    console.log("open")
    let artist_id = $(event.target).closest("button").attr("data-artist-id");
    let artist_name = $(event.target).closest("button").attr("data-artist-name");
    body.classList.toggle('body--active');
    modalDeleteArtist.classList.toggle('modal--active');
    curr_artist_id = artist_id;
    $("#artist_name-delete").html(artist_name);
}

function modal_ArtistClose() {
    body.classList.remove('body--active');
    modalEditArtist.classList.remove('modal--active');
    curr_artist_id = -1;
    $("#name").val("")
    $("#description").val("")
    $("#genre").val("")
    $("#listeners").val("")
    $("#file-upload-label").text("Загрузить файл")
    $(".modal").scrollTop(0);
}

function modal_DeleteArtistClose() {
    console.log("close")
    body.classList.remove('body--active');
    modalDeleteArtist.classList.remove('modal--active');
    curr_artist_id = -1;
}

function deleteArtist() {
    window.location.href = `allartist/delete/${curr_artist_id}`;
}
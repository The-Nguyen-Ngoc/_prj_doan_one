function buttonCancelAndFileImage(redirect){
    $("#buttonCancel").on("click", function (){
        window.location = redirect;
    })

    $('#fileImage').change(function (){
        showImageThumbnail(this);
    })};

function showImageThumbnail(fileInput){
    var file = fileInput.files[0];
    var reader = new FileReader();

    reader.onload = function (e){
        $('#thumbnail').attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
}

function showModalDialog(title,message){
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();

}
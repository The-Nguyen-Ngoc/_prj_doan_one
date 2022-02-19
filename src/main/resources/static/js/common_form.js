

function buttonCancelAndFileImage(redirect){
    $("#buttonCancel").on("click", function (){
        window.location = redirect;
    })

    $('#fileImage').change(function (){
        showImageThumbnail(this);
    })

};


function addNextExtraImage(index) {
    html =` <div class="col m-3 p-2" id="divExtraImage${index}">
            <div id="extraImageHeader${index}"><label>Ảnh ${index+1}: </label></div>
            <div>
                <img id="extraThumbnail${index}" alt="Extra image ${index} preview" class="img-fluid"
                     src="${defaultImageThumbnail}"/>
            </div>
            <br>
            <div>
                <input type="file" name="extraImage" 
                accept="image/png,image/jpg, image/jpeg" 
                onchange="showExtraImageThumbnail(this,${index})"/>
            </div>
        </div>`

    htmlRemove = `
                    <a  type="button" class="btn btn-danger justify-content-center float-right mb-2"
                     onclick="removeExtraImage(${index-1})">
                        <i class="fas fa-trash-alt"></i>
                    </a>
                `
    $('#divProductImages').append(html);
    $('#extraImageHeader'+(index-1)).append(htmlRemove);
    extraImagesCount++;
}

function showExtraImageThumbnail(fileInput, index){
    var file = fileInput.files[0];
    fileName = file.name;
    imageNameHiddenField  = $("#imageName"+ index);
    if(imageNameHiddenField.length){
        imageNameHiddenField.val(fileName);
    }


    var reader = new FileReader();

    reader.onload = function (e){
        $("#extraThumbnail"+ index).attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
    if(index >= extraImagesCount-1){
    addNextExtraImage(index+1);
    }
}

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
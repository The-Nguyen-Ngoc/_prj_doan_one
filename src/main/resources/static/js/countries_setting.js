var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;



$(document).ready(function () {
    buttonLoad = $("#buttonLoadCountries");
    dropDownCountry = $("#dropDownCountries");
    buttonAddCountry = $("#btnAddCountry");
    buttonUpdateCountry = $("#btnUpdateCountry");
    buttonDeleteCountry = $("#btnDeleteCountry");
    labelCountryName = $("#labelCountryName");
    fieldCountryName = $("#fieldCountryName");
    fieldCountryCode= $("#fieldCountryCode");

    buttonLoad.click(function () {
        loadCountries();
    });

    dropDownCountry.on('change', function () {
        changeFormStateToSelectedCountry();
    });

    buttonAddCountry.click(function () {
            addCountry();
    });
    buttonUpdateCountry.click(function () {
        if(buttonUpdateCountry.val()=="Sửa") {
            update();
        }
    });
    buttonDeleteCountry.click(function () {
            deleteCountry();
    });

});

function deleteCountry(){
    optionValue = dropDownCountry.val();
    countryId = optionValue.split("-")[0];
    url = contextPath +"countries/delete/" + countryId;

    $.get(url,function (){
        $("#dropDownCountries option[value='"+optionValue+"']").remove();
        changeFormStateToNew();
    }).done(function (){
        $("#toastMessage").text("Xóa quốc gia thành công");
        $(".toast").toast("show");
    }).fail(function (){
        $("#toastMessage").text("Có lỗi khi xóa quốc gia!");
        $(".toast").toast("show");
    })
}
function update() {
    url = contextPath +"countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    jsonData = {name: countryName, code: countryCode};

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType:"application/json"
    }).done(function (countryId){
        selectNewlyAddedCountry(countryId, countryCode, countryName);
        $("#dropDownCountries option:selected").val(countryCode);
        $("#dropDownCountries option:selected").text(countryName);
        $("#toastMessage").text("Cập nhật quốc gia thành công!");
        $(".toast").toast("show");
    }).fail(function (){
        $("#toastMessage").text("Có lỗi khi cập nhật quốc gia!");
        $(".toast").toast("show");
    })
}

function changeFormStateToNew() {
    buttonAddCountry.val("Add");
    labelCountryName.text("Thêm quốc gia");
    buttonUpdateCountry.prop('disabled', true);
    buttonDeleteCountry.prop('disabled', true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}
function changeFormStateToUpdate() {
    buttonUpdateCountry.val("Update");
    labelCountryName.text("Sửa quốc gia");
    buttonUpdateCountry.prop('disabled', false);
    buttonDeleteCountry.prop('disabled', true);
    buttonAddCountry.prop('disabled', true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}
function changeFormStateToSelectedCountry() {
    buttonAddCountry.prop('value', 'Thêm');
    buttonUpdateCountry.prop('disabled', false);
    buttonDeleteCountry.prop('disabled', false);
    selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);

    var optionValue = dropDownCountry.val();
    fieldCountryCode.val(optionValue);

}



function addCountry() {
    url = contextPath +"countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    jsonData = {name: countryName, code: countryCode};

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType:"application/json"
    }).done(function (countryId){
        selectNewlyAddedCountry(countryId, countryCode, countryName);
        $("#toastMessage").text("Thêm quốc gia thành công!");
        $(".toast").toast("show");
    }).fail(function (){
        $("#toastMessage").text("Có lỗi khi thêm quốc gia!");
        $(".toast").toast("show");
    })
}

function selectNewlyAddedCountry(countryId, countryCode, countryName) {

    optionValue = countryId+ "-" + countryCode;
    dropDownCountry.append("<option value='"+countryCode+"'>"+countryName+"</option>");
    $("#dropDownCountries option[value='"+countryCode+"']").prop('selected', true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();

}


function loadCountries(){
    url =contextPath+"countries/list";
    $.get(url,function (responseJSON){
        dropDownCountry.empty();
        $.each(responseJSON,function (index,country) {

            dropDownCountry.append("<option value='"+country.code+"'>"+country.name+"</option>");
        });

    }).done(function (){
        $("#toastMessage").text("Danh sách quốc gia đã được tải xong");
        $(".toast").toast("show");
    }).fail(function (){
        $("#toastMessage").text("Có lỗi khi tải danh sách quốc gia!");
        $(".toast").toast("show");
    })
}
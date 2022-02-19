function removeDetailByIndex( index) {
    $('#divDetail' + index).remove();
}

$(document).ready(function () {
    $("a[name='linkRemoveDetail']").each(function (index) {

        $(this).click(function () {
            removeDetailByIndex(index)
        });
    });

});

function addNextDetailSection() {
    allDivDetails = $("[id^='divDetail']");
    divDetailsCount = allDivDetails.length;

    html = `
     <div class="form-inline" id="divDetail${divDetailsCount}">
        <input type="hidden" name="detailIDs" value="0"/>
        <label class="m-3">Thuộc tính:</label>
        <input type="text" class="form-control m-3 w-25" name="detailNames" maxlength="255"/>
        <label class="m-3">Giá trị:</label>
        <input type="text" class="form-control m-3 w-25" name="detailValues" maxlength="255"/>
    </div>
    `;
    $('#divProductDetails').append(html);
    previouDivDetailSection = allDivDetails.last();
    previouDivDetailId = previouDivDetailSection.attr('id');

    htmlRemove = `
                    <a class="form-control border-danger" onclick="removeDetailSectionById('${previouDivDetailId}')" >
                        <i class="fas fa-trash-alt"></i>
                    </a>
                `


    previouDivDetailSection.append(htmlRemove);
}


function removeDetailSectionById(id) {
    $('#' + id).remove();

}
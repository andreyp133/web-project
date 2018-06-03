function pickAll() {
    var mainCheckbox = document.getElementsByName("mainPicker");
    var allCheckboxes = document.getElementsByName("picker");
    var checkboxesCount = allCheckboxes.length;
    if(mainCheckbox[0].checked){
        for(var i = 0; i < checkboxesCount; i++){
            allCheckboxes[i].checked = true;
        }
    } else {
        for(var i = 0; i < checkboxesCount; i++){
            allCheckboxes[i].checked = false;
        }
    }
}

function unpickMainCheckbox() {
    var mainCheckbox = document.getElementsByName("mainPicker");
    mainCheckbox[0].checked = false;
}
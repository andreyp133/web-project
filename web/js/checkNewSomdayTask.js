function checkTextField() {
    var task = document.getElementById("task").value;
    var errorMessage = document.getElementById("errorMessage");
    if(task.length == 0){
        errorMessage.style.display = "";
        errorMessage.innerHTML = "Task's text is empty!";
        window.scrollTo(0,0);
        return false;
    } else {
        errorMessage.style.display = "none";
        return true;
    }
}
function checkDate() {
    var date = document.getElementById("date").value;
    var errorMessage = document.getElementById("errorMessage");
    if(date.length == 0){
        errorMessage.style.display = "";
        errorMessage.innerHTML = "Task's date is empty!";
        window.scrollTo(0,0);
        return false;
    } else {
        errorMessage.style.display = "none";
        return true;
    }
}
function checkNewSomdayTask() {
    var task = document.getElementById("somedayTask").value;
    var errorMessage = document.getElementById("errorMessage");
    if(task.length == 0){
        errorMessage.style.display = "";
        errorMessage.innerHTML = "Task's text is empty!";
        window.scrollTo(0,0);
        return false;
    } else {
        return checkDate();
    }
}
$(function () {
    $('#datetimepicker').datetimepicker({
        format: 'YYYY-MM-DD'
    });
});
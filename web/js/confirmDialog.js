$(document).ready(function () {
    $.ajaxSetup({cache: false});
    $(".viewDialog").on("click", function (e) {
        e.preventDefault();
        $("<div></div>")
            .addClass("dialog")
            .appendTo("body")
            .dialog({
                title: $(this).attr("data-dialog-title"),
                close: function () {
                    $(this).remove()
                },
                modal: true,
                height: 450,
                width: 800
            }).load(this.href);
    });
});
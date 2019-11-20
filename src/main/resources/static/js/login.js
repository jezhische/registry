$(function () {
    let freeAccess = $('#free-access');

// ------------------------------------------------------------------------------------------------
    function accessFree() {
        freeAccess.click(function (event) {
            event.preventDefault();
            $.ajax({
                type: 'get',
                url: 'free-access',
//                dataType: 'text/html;charset=UTF-8',
                success: function (data, status, jqXHR) {
                    console.log("accessFree() success");
                    window.location.href = 'free-access';
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("accessFree() error");
                    console.log(textStatus);
                    console.log(errorThrown);
                    console.log(jqXHR.status);
                }
            });
        });
    }


// ------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------

// ================================================================ P E R F O R M A N C E

    accessFree();
});
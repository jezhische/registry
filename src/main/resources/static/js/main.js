

$(function () {

// ======================================================================================= page index.html

    function redirectRegistrationPage() {
        $('#signup-button').click(function (event) {
            event.preventDefault();
            console.log('signup-button clicked');
            $.ajax({
                type: 'get',
                url: 'registration',
                dataType: 'text',
                success: function (data) {
                    console.log('redirectRegistrationPage() success');
                    if (data) window.location.href = 'registration'
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("redirectRegistrationPage() error: status = " + jqXHR.status +
                    ", errorThrown = " + errorThrown);
                }
            });
        });
    }

    function hackButtonBehavior() {
         let hackButton = $("#hack-button");
        let getPassword = $("#hack-password");
        hackButton.click(function (event) {
            event.preventDefault();
            console.log('hackButton was pushed!');
            hackRequest(getPassword.val());
            getPassword.val('');
        });
    }
// ------------------------------------------------

    // send "#hack-password" input content (see index.html) to check, and if the password matched return true
    function hackRequest(password) {
        $.ajax({
            type: 'get',
            url: 'index?pswd=' + password,
            dataType: 'json',
            success: function (data, status, jqXHR) {
                console.log('hackRequest completed, data = ' + data);
//                if (data) hackerRedirect();
                if (data) window.location.href = 'terriblemistakeofyourlife.html';
                else hackMessage(password);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("hackRequest() error");
            }
        });
    }

// ------------------------------------------------

    function hackMessage(password) {
        $("#hack-message").html('No dear, <span style="color: blue; font-weight: bold">' + password + '</span> does not fit. Come again!');
        $('#promo-picture').css('visibility', 'visible');
    }

// ------------------------------------------------

    function unlockButtonBehavior() {
        let unlockBtn = $('#unlock-btn');
        unlockBtn.click(function (event) {
            event.preventDefault();
            console.log('unlock-btn pushed');
            unlockRequest();
        })

    }

// ------------------------------------------------

// ========================================================================= page terriblemistakeofyourlife.html

    function unlockRequest() {
        $.ajax({
            type: 'post',
            url: 'unlock',
//            dataType: 'json',
            success: function (data, status, jqXHR) {
                console.log('unlockRequest completed');
                let image = $('#hack-img');
                image.attr("src","images/unlocked.png");
                setTimeout(function () {
                    window.location.href = '/store';
                }, 2000)
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("unlockRequest() error");
            }
        })
    }

// ------------------------------------------------

    function removeHackerBlockButtonBehavior() {
        let rHBButton = $('#remove-hacker-block');
        rHBButton.click(function (event) {
            event.preventDefault();
            // console.log($('#hacker-block').html());
            $('#hacker-block').html('');
            rHBButton.css('visibility', 'hidden');
            let promoPicture = $('#promo-picture');
            if(promoPicture.css('visibility') === 'visible') promoPicture.css('visibility', 'hidden');
            createLogInput();
        })
    }

// ------------------------------------------------

    function createLogInput() {

    }

// ------------------------------------------------
// ------------------------------------------------
// ------------------------------------------------

// ================================================================ P E R F O R M A N C E
    redirectRegistrationPage();
    hackButtonBehavior();
    unlockButtonBehavior();
    removeHackerBlockButtonBehavior();

});
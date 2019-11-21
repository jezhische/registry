window.onload = function () {

    const VIEW_PROFILE = document.querySelector('#view-profile');
    const VIEW_PROFILE_ERROR_TEXT = document.querySelector('#view-profile-error-text');

    function getProfileOnButtonClick() {
        VIEW_PROFILE.onclick = async function (event) {
            event.preventDefault();
            console.log('VIEW_PROFILE clicked');
            let response = await fetch('index/customer-data', {method: 'GET'});
        //    as I get Promise instance:
            if (response.ok) {
                let respJson = await response.json();
                console.log(respJson);
                return respJson;
            }
            else {
                VIEW_PROFILE_ERROR_TEXT.innerHTML = `Some error occured: ${response.status}: ${response.statusText}`;
                VIEW_PROFILE_ERROR_TEXT.style.display = 'block';
                return null;
            }
        }
    }

//    ================================================================================================ performance
    getProfileOnButtonClick();
};
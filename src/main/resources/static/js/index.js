window.onload = function () {

    const VIEW_PROFILE = document.querySelector('#view-profile');
    const VIEW_PROFILE_ERROR_TEXT = document.querySelector('#view-profile-error-text');
    const NAME = document.querySelector('#name');
    const LAST_NAME = document.querySelector('#last-name');
    const EMAIL = document.querySelector('#email');
    const COUNTRY = document.querySelector('#country');
    const PROVINCE = document.querySelector('#province');
    const CITY = document.querySelector('#city');
    const STATES = document.querySelector('#states');

    function getProfileOnButtonClick() {
        VIEW_PROFILE.onclick = async function (event) {
            event.preventDefault();
            VIEW_PROFILE_ERROR_TEXT.style.display = 'none';
            console.log('VIEW_PROFILE clicked');
            let response = await fetch('index/customer-data', {method: 'GET'});
        //    as I get Promise instance:
            if (response.ok) {
                let respJson = await response.json();
                console.log(respJson);
                viewProfile(respJson);
                // return respJson;
            }
            else {
                VIEW_PROFILE_ERROR_TEXT.innerHTML = `Some error occured: ${response.status}: ${response.statusText}`;
                VIEW_PROFILE_ERROR_TEXT.style.display = 'block';
                return null;
            }
        }
    }

    function viewProfile(respJson) {
        NAME.style.display = 'block';
        if (respJson.name) NAME.innerHTML = 'Your name: <span style="color: #d58512">' +  respJson.name + '</span>';
        else NAME.innerHTML = 'You haven\'t any name';

        LAST_NAME.style.display = 'block';
        if (respJson.lastName) LAST_NAME.innerHTML = 'Your last name: <span style="color: #d58512">' +  respJson.lastName + '</span>';
        else LAST_NAME.innerHTML = 'You haven\'t any last name';

        EMAIL.style.display = 'block';
        if (respJson.email) EMAIL.innerHTML = 'Your email: <span style="color: #d58512">' +  respJson.email + '</span>';
        else EMAIL.innerHTML = 'You haven\'t any email';

        COUNTRY.style.display = 'block';
        if (respJson.country) COUNTRY.innerHTML = 'Your country: <span style="color: #d58512">' +  respJson.country + '</span>';
        else COUNTRY.innerHTML = 'You haven\'t any country';

        if (respJson.country && respJson.country.toLowerCase() === 'USA'.toLowerCase()) {
            STATES.style.display = 'block';
            STATES.innerHTML = 'The states that border with yours one: ';
            if (respJson.states && respJson.states.length !== 0) {
                respJson.states.forEach(state => {
                    STATES.innerHTML = STATES.innerHTML + '<span style="color: #d58512">, ' + state + '</span>';
                })
            } else STATES.innerHTML = 'You don \'t have any states next door';
        } else if (respJson.country && respJson.country.toLowerCase() === 'Canada'.toLowerCase()) {
            PROVINCE.style.display = 'block';
            if (respJson.province) PROVINCE.innerHTML = 'Your province: <span style="color: #d58512">' +  respJson.province + '</span>';
            else EMAIL.innerHTML = 'You haven\'t any province';

            CITY.style.display = 'block';
            if (respJson.city) CITY.innerHTML = 'Your city: <span style="color: #d58512">' +  respJson.city + '</span>';
            else EMAIL.innerHTML = 'You haven\'t any city';
        }
    }

//    ================================================================================================ performance
    getProfileOnButtonClick();
};
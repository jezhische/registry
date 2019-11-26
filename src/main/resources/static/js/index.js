window.onload = function () {

    let customer = {};
    let name = '';
    let surname = '';
    let email = '';
    let country = '';
    let province = '';
    let city = '';
    let states = [];

    const VIEW_PROFILE_BTN = document.querySelector('#view-profile');
    const VIEW_PROFILE_ERROR_TEXT = document.querySelector('#view-profile-error-text');
    const NAME = document.querySelector('#name');
    const LAST_NAME = document.querySelector('#last-name');
    const EMAIL = document.querySelector('#email');
    const COUNTRY = document.querySelector('#country');
    const PROVINCE = document.querySelector('#province');
    const CITY = document.querySelector('#city');
    const STATES = document.querySelector('#states');
    const EDIT_PROFILE_BTN = document.querySelector('#edit-profile');
    const EDIT = document.querySelector('#edit');
    const CANCEL = document.querySelector('#cancel');
    const DELETE = document.querySelector('#delete');
    const USA_STATE_SELECTION = document.querySelector('#usa-state-selection');
    const SUCCESS_MESSAGE = document.querySelector('#success-message');
    const MODAL = document.querySelector('.modal');
    let CANADA_PROVINCE_SELECT;

    window.onclick = (event) => MODAL.style.display = 'none';

    // function getProfileOnButtonClick() {
        VIEW_PROFILE_BTN.onclick = async function (event) {
            event.preventDefault();
            VIEW_PROFILE_ERROR_TEXT.style.display = 'none';
            USA_STATE_SELECTION.style.display = 'none';
            console.log('VIEW_PROFILE clicked');
            let response = await fetch('index/customer-data', {method: 'GET'});
        //    as I get Promise instance:
            if (response.ok) {
                customer = await response.json();
                // let controlText = await response.text();
                console.log(customer);
                viewProfile(customer);
            }
            else {
                VIEW_PROFILE_ERROR_TEXT.innerHTML = `Some error occured: ${response.status}: ${response.statusText}`;
                VIEW_PROFILE_ERROR_TEXT.style.display = 'block';
                return null;
            }
        };
    // }

    function viewProfile(customer) {
        NAME.style.display = 'block';
        if (customer.name) NAME.innerHTML = 'Your name: <span style="color: #d58512">' +  customer.name + '</span>';
        else NAME.innerHTML = 'You haven\'t any name';

        LAST_NAME.style.display = 'block';
        if (customer.surname) LAST_NAME.innerHTML = 'Your last name: <span style="color: #d58512">' +  customer.surname + '</span>';
        else LAST_NAME.innerHTML = 'You haven\'t any last name';

        EMAIL.style.display = 'block';
        if (customer.email) EMAIL.innerHTML = 'Your email: <span style="color: #d58512">' +  customer.email + '</span>';
        else EMAIL.innerHTML = 'You haven\'t any email';

        COUNTRY.style.display = 'block';
        if (customer.country) COUNTRY.innerHTML = 'Your country: <span style="color: #d58512">' +  customer.country + '</span>';
        else COUNTRY.innerHTML = 'You haven\'t any country';

        if (customer.country && customer.country.toLowerCase() === 'USA'.toLowerCase()) {
            STATES.style.display = 'block';
            STATES.innerHTML = 'The states that border with yours one: ';
            if (customer.states && customer.states.length !== 0) {
                customer.states.forEach(state => {
                    STATES.innerHTML = STATES.innerHTML + '<span style="color: #d58512">, ' + state + '</span>';
                })
            } else STATES.innerHTML = 'You don \'t have any states next door';
        } else if (customer.country && customer.country.toLowerCase() === 'Canada'.toLowerCase()) {
            PROVINCE.style.display = 'block';
            if (customer.province) PROVINCE.innerHTML = 'Your province: <span style="color: #d58512">' +  customer.province + '</span>';
            else PROVINCE.innerHTML = 'You haven\'t any province';

            CITY.style.display = 'block';
            if (customer.city) CITY.innerHTML = 'Your city: <span style="color: #d58512">' +  customer.city + '</span>';
            else CITY.innerHTML = 'You haven\'t any city';
        }

        EDIT_PROFILE_BTN.style.display = 'block';
        EDIT.style.display = 'none';
        CANCEL.style.display = 'none';
        DELETE.style.display = 'none';
    }

// ==============================================================================================================
        EDIT_PROFILE_BTN.onclick = function (event) {
            event.preventDefault();
            states = [];
            for (let i = 1; i < USA_STATE_SELECTION.children.length; i++) {
                USA_STATE_SELECTION.children[i].style.display = 'none';
            }
            name = customer.name;
            NAME.innerHTML = '<input type="text" value="' + name + '" class="col-sm-6 col-sm-offset-1"/>';
            surname = customer.surname;
            LAST_NAME.innerHTML = '<input type="text" value="' + surname + '" class="col-sm-6 col-sm-offset-1"/>';
            email = customer.email;
            EMAIL.innerHTML = '<input type="text" value="' + email + '" class="col-sm-6 col-sm-offset-1"/>';
            country = customer.country;
            // let another = country.toLowerCase() === 'usa' ? 'Canada' : 'USA';
            COUNTRY.innerHTML = '<select id="country-select" class="col-sm-6 col-sm-offset-1">' +
                '<option value="" disabled selected>'+ country + '</option>' +
                // '<option value="">'+ another + '</option>' +
                '</select>';
            // const COUNTRY_SELECT = document.querySelector('#country-select');

            if (customer.country && customer.country.toLowerCase() === 'USA'.toLowerCase()) {
                STATES.innerHTML = '<select id="usa-states-select" class="col-sm-6 col-sm-offset-1">' +
                    '<option value="" disabled selected>Select your state</option></select>';
                STATES.style.display = 'block';
                const USA_STATES_SELECT = document.querySelector('#usa-states-select');
                // the variable from us-states-array.js
                statesArray.forEach(state => {
                    let option = document.createElement("OPTION");
                    option.value = state;
                    option.innerText = state;
                    USA_STATES_SELECT.append(option);
                });
                USA_STATE_SELECTION.style.display = 'block';
                states = customer.states;
                // console.log('STATES before forEach: ' + states);
                states.forEach(state => {
                    let element = document.createElement('DIV');
                    element.innerHTML = '<div class="state col-md-8 col-md-offset-2" align="center">'
                        + state + '<span class="round-border">X</span></div>';
                    element.onclick = function(event) {
                        this.style.display = 'none';
                        states.splice(states.indexOf(state), 1);
                        console.log('states: ' + states);
                    };
                    USA_STATE_SELECTION.append(element);
                    console.log('states: ' + states);
                });

                USA_STATES_SELECT.onchange = function (event) {
                    let choosenState = USA_STATES_SELECT.value;
                    // console.log('state: ' + choosenState);
                    let stateIndex = states.indexOf(choosenState);
                    // console.log('state index: ' + stateIndex);
                    if (stateIndex < 0) {
                        states.push(choosenState);
                        let element = document.createElement('DIV');
                        element.innerHTML = '<div class="state col-md-8 col-md-offset-2" align="center">'
                            + choosenState + '<span class="round-border">X</span></div>';
                        element.onclick = function(event) {
                            this.style.display = 'none';
                            states.splice(states.indexOf(choosenState), 1);
                            console.log('states: ' + states);
                        };
                        USA_STATE_SELECTION.append(element);
                        console.log('states: ' + states);
                    }
                };
            } else if (customer.country && customer.country.toLowerCase() === 'canada'.toLowerCase()) {
                STATES.style.display = 'none';
                province = customer.province;
                PROVINCE.innerHTML = '<select id="canada-province-select" class="col-sm-6 col-sm-offset-1"></select>';
                    // + '<option value="" selected>' + province + '</option></select>';
                PROVINCE.style.display = 'block';
                CANADA_PROVINCE_SELECT = document.querySelector('#canada-province-select');
                // the variable from canada-province-array.js
                provincesArray.forEach(province => {
                    let option = document.createElement("OPTION");
                    option.value = province;
                    option.innerText = province;
                    CANADA_PROVINCE_SELECT.append(option);
                });
                CANADA_PROVINCE_SELECT.onchange = function(event) {
                    province = CANADA_PROVINCE_SELECT.options[CANADA_PROVINCE_SELECT.selectedIndex].text;
                    console.log(province);
                };

                city = customer.city;
                CITY.innerHTML = '<input type="text" value="' + city + '" class="col-sm-6 col-sm-offset-1"/>';
            }

            EDIT_PROFILE_BTN.style.display = 'none';
            EDIT.style.display = 'block'; // НА НАЖАТИЕ КНОПКИ НЕ ЗАБЫТЬ ОТПРАВИТЬ states в customer.states и обнулить
            CANCEL.style.display = 'block';
            DELETE.style.display = 'block';

            EDIT.onclick = async function updateCustomer(event) {
                customer.name = NAME.firstElementChild.value;
                customer.surname = LAST_NAME.firstElementChild.value;
                customer.email = EMAIL.firstElementChild.value;
                // customer.country = COUNTRY_SELECT.options[COUNTRY_SELECT.selectedIndex].text;
                if (PROVINCE.style.display === 'block') {
                    customer.province = CANADA_PROVINCE_SELECT.options[CANADA_PROVINCE_SELECT.selectedIndex].text;
                    customer.city = CITY.firstElementChild.value;
                } else customer.states = states;


                let promise = await fetch('index/customer-data', {
                    method: 'put',
                    headers: {'Content-Type': 'application/json;charset=utf-8'},
                    body: JSON.stringify(customer)
                });
                // let result = await promise.text();
                if (promise.ok) {
                    SUCCESS_MESSAGE.innerHTML = await promise.text();
                    SUCCESS_MESSAGE.style.display = 'block';
                } else if (!promise.ok) {
                    // SUCCESS_MESSAGE.innerHTML = `Something went wrong: ${promise.status} : ${await promise.text()}`;
                    // SUCCESS_MESSAGE.style.display = 'block';
                    MODAL.style.display = 'flex';
                    let apiExceptionDetails = await promise.json();
                    let message = `Something went wrong:  : ${promise.status} : ${apiExceptionDetails.status} : ${apiExceptionDetails.message}}`;
                    console.log(message);
                    MODAL.lastElementChild.innerHTML = message;

                }

                viewProfile(customer);
                console.log(customer);
            };

            CANCEL.onclick = event => {
                clearVariables();
                VIEW_PROFILE_BTN.click();
            };

            DELETE.onclick = function (event) {
              if (confirm("Do you really want to delete your account?")) {
                  fetch('index/customer-data', {
                      method: 'DELETE',
                      headers: {'content-type': 'application/json'},
                      // body: JSON.stringify({id: '5bdcdfa40f0a326f858feae0'})
                  })
                      .then(res => res.text())
                      .then(message => console.log(message))
                      .then(setTimeout(() => window.location.href = 'login', 2000));
              }
            }
    };


    function clearVariables() {
        document.querySelectorAll('.state').forEach(state => state.style.display = 'none');
        USA_STATE_SELECTION.style.display = 'none';
        customer = {};
        name = '';
        surname = '';
        email = '';
        country = '';
        province = '';
        city = '';
        states = [];
    }

//    ================================================================================================ performance
//     getProfileOnButtonClick();
};
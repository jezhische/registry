window.onload = function () {
    // import {statesArray} from './us-states-array'
    let states = [];

    const LOGIN_ERROR = document.querySelector('#login-error');
    const LOGIN = document.querySelector('#login');
    const PASSWORD_ERROR = document.querySelector('#password-error');
    const PASSWORD = document.querySelector('#password');
    const NAME_ERROR = document.querySelector('#name-error');
    const NAME = document.querySelector('#name');
    const SURNAME_ERROR = document.querySelector('#surname-error');
    const SURNAME = document.querySelector('#surname');
    const EMAIL_ERROR = document.querySelector('#email-error');
    const EMAIL = document.querySelector('#email');
    const COUNTRY_SELECT_ERROR = document.querySelector('#country-select-error');
    const COUNTRY_SELECT = document.querySelector('#country-select');
    const USA_STATE_SELECTION = document.querySelector('#usa-state-selection');
    const CANADIAN_PROVINCE_SELECTION = document.querySelector('#canadian-province-selection');
    const US_SELECT = document.querySelector('#us-select');
    const USA_STATES_SELECT = document.querySelector('#usa-states-select');
    const USA_STATES_SELECT_ERROR = document.querySelector('#usa-states-select-error');
    const CANADA_PROVINCE_SELECT_ERROR = document.querySelector('#canada-province-select-error');
    const CANADA_PROVINCE_SELECT = document.querySelector('#canada-province-select');
    const CANADA_CITY = document.querySelector('#canada-city');
    const CANADA_CITY_ERROR = document.querySelector('#canada-city-error');
    const SUBMIT_BUTTON = document.querySelector('#submit-button');
    const SUCCESS_MESSAGE = document.querySelector('#success-message');



    function listenUSA() {
        CANADIAN_PROVINCE_SELECTION.style.display = 'none';
        USA_STATE_SELECTION.style.display = 'block';
        US_SELECT.style.display = 'block';
    }

    function listenCanada() {
        USA_STATE_SELECTION.style.display = 'none';
        CANADIAN_PROVINCE_SELECTION.style.display = 'block';
        US_SELECT.style.display = 'none';
    }

    function listenNothing() {
        USA_STATE_SELECTION.style.display = 'none';
        CANADIAN_PROVINCE_SELECTION.style.display = 'none';
        US_SELECT.style.display = 'none';
    }

        COUNTRY_SELECT.onchange = function (event) {
            let index = this.selectedIndex;
            console.log(index);
            switch (index) {
                case 0:
                    listenNothing();
                    break;
                case 1: {
                    listenUSA();
                    // fillStatesSelect();
                    listenUSAStatesSelectionOnchange();
                }
                    break;
                case 2:
                    listenCanada();
                    break;
            }
        };

    function listenUSAStatesSelectionOnchange() {
        USA_STATES_SELECT.style.display = 'block';
        USA_STATES_SELECT.onchange = function (event) {
            let choosenState = USA_STATES_SELECT.value;
            console.log('state: ' + choosenState);
            let stateIndex = states.indexOf(choosenState);
            console.log('state index: ' + stateIndex);
            if (stateIndex < 0) {
                states.push(choosenState);
                let element = document.createElement('DIV');
                element.innerHTML = '<div class="state col-md-8 col-md-offset-2" align="center">'
                    + choosenState + '<span class="round-border">X</span></div>';
                element.onclick = function(event) {
                    this.style.display = 'none';
                    states.splice(states.indexOf(choosenState), 1);
                };
                USA_STATE_SELECTION.append(element);
                console.log('states: ' + states);
            }
        };
    }

    function fillStatesSelect() {
        // the variable from us-states-array.js
        statesArray.forEach(state => {
            let option = document.createElement("OPTION");
            option.value = state;
            option.innerText = state;
            USA_STATES_SELECT.append(option);
        });
    }

    function fillProvincesSelect() {
        // the variable from canada-province-array.js
        provincesArray.forEach(province => {
            let option = document.createElement("OPTION");
            option.value = province;
            option.innerText = province;
            CANADA_PROVINCE_SELECT.append(option);
        });
    }

    SUBMIT_BUTTON.onclick = function(event) {
        event.preventDefault();
        doOnSubmit();
    };

    /**
     * Validation of the form fields and either error message output or creating model and post it to server
     * @return {Promise<void>}
     */
    async function doOnSubmit() {
        let model = {};
        let isModelValid = true;

        let login = LOGIN.value;
        if (LOGIN_ERROR.innerHTML.includes('Sorry, such login already exists')) {
            LOGIN_ERROR.style.display = 'block';
            isModelValid = false;
        } else if (login.length < 1) {
            LOGIN_ERROR.style.display = 'block';
            LOGIN_ERROR.innerHTML = 'Please input a login';
            isModelValid = false;
        } else {
            LOGIN_ERROR.style.display = 'none';
            model.login = login;
        }
        // ------------------------------------------
        let password = PASSWORD.value;
        if (password.length < 5) {
            PASSWORD_ERROR.style.display = 'block';
            PASSWORD_ERROR.innerHTML = 'Password must be at least 5 characters long';
            isModelValid = false;
        } else {
            PASSWORD_ERROR.style.display = 'none';
            model.password = password;
        }
        // ------------------------------------------
        let name = NAME.value;
        if (name.length < 1) {
            NAME_ERROR.style.display = 'block';
            NAME_ERROR.innerHTML = 'Please input a name';
            isModelValid = false;
        } else {
            NAME_ERROR.style.display = 'none';
            model.name = name;
        }
        // ------------------------------------------
        let surname = SURNAME.value;
        if (surname.length < 1) {
            SURNAME_ERROR.style.display = 'block';
            SURNAME_ERROR.innerHTML = 'Please input a surname';
            isModelValid = false;
        } else {
            SURNAME_ERROR.style.display = 'none';
            model.surname = surname;
        }
        // ------------------------------------------
        let email = EMAIL.value;
        if (email.length < 1) {
            EMAIL_ERROR.style.display = 'block';
            EMAIL_ERROR.innerHTML = 'Please input an email';
            isModelValid = false;
        }
        else if (!(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(String(email).toLowerCase()))) {
            EMAIL_ERROR.style.display = 'block';
            EMAIL_ERROR.innerHTML = 'Invalid email';
            isModelValid = false;
        }
        else {
            EMAIL_ERROR.style.display = 'none';
            model.email = email;
        }
        // ------------------------------------------
        let country = COUNTRY_SELECT.options[COUNTRY_SELECT.selectedIndex].text;
        if (COUNTRY_SELECT.selectedIndex === 0) {
            COUNTRY_SELECT_ERROR.style.display = 'block';
            COUNTRY_SELECT_ERROR.innerHTML = 'Please select a country';
            isModelValid = false;
        //    selected country is USA
        } else if (COUNTRY_SELECT.selectedIndex === 1) {
            COUNTRY_SELECT_ERROR.style.display = 'none';
            model.country = country;
            if (states.length < 3) {
                USA_STATES_SELECT_ERROR.style.display = 'block';
                USA_STATES_SELECT_ERROR.innerHTML = 'Please select at least three states that border with yours';
                isModelValid = false;
            } else {
                USA_STATES_SELECT_ERROR.style.display = 'none';
                model.states = states;
                model.province = '';
                model.city = '';
            }
        //  selected country is Canada
        } else {
            COUNTRY_SELECT_ERROR.style.display = 'none';
            model.country = country;
            if (CANADA_PROVINCE_SELECT.selectedIndex === 0) {
                CANADA_PROVINCE_SELECT_ERROR.style.display = 'block';
                CANADA_PROVINCE_SELECT_ERROR.innerHTML = 'Please select your province';
                isModelValid = false;
            } else {
                CANADA_PROVINCE_SELECT_ERROR.style.display = 'none';
                model.province = CANADA_PROVINCE_SELECT.options[CANADA_PROVINCE_SELECT.selectedIndex].text;
            }
            let city = CANADA_CITY.value;
            if (city.length < 2) {
                CANADA_CITY_ERROR.style.display = 'block';
                CANADA_CITY_ERROR.innerHTML = 'Please input right name of your city';
                isModelValid = false;
            } else {
                CANADA_CITY_ERROR.style.display = 'none';
                model.city = city;
                model.states = '';
            }
        }

        if (!isModelValid) return;
        else alert(JSON.stringify(model));
        // let promise = await fetch('registration', {
        //     method: 'POST',
        //     headers: {'Content-Type': 'application/json;charset=utf-8'},
        //     body: JSON.stringify(model)
        // });
        // let result = await promise.json();
    }

    fillStatesSelect();
    fillProvincesSelect();
};
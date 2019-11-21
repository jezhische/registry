window.onload = function () {
    const COUNTRY_SELECT = document.querySelector('#country-select');
    const USA_SELECTION = document.querySelector('#usa');
    const CANADA_SELECTION = document.querySelector('#canada');
    const USA_STATE_SELECTION = document.querySelector('#usa-state-selection');
    const CANADIAN_PROVINCE_SELECTION = document.querySelector('#canadian-province-selection');
    const USA_STATES_SELECT = document.querySelector('#usa-states-select');
    const LABEL_FOR_USA_STATES_SELECT = document.querySelector('.usa-states-select');
    const CRUTCH = document.querySelector('#crutch-input');


    function listenUSA() {
        CANADIAN_PROVINCE_SELECTION.style.display = 'none';
        USA_STATE_SELECTION.style.display = 'block';
    }

    function listenCanada() {
        USA_STATE_SELECTION.style.display = 'none';
        CANADIAN_PROVINCE_SELECTION.style.display = 'block';
    }

    function listenNothing() {
        USA_STATE_SELECTION.style.display = 'none';
        CANADIAN_PROVINCE_SELECTION.style.display = 'none';
    }

    function listenCountrySelectOnchange() {
        COUNTRY_SELECT.onchange = function (event) {
            let index = this.selectedIndex;
            console.log(index);
            switch (index) {
                case 0:
                    listenNothing();
                    break;
                case 1: {
                    listenUSA();
                    listenUSAStatesSelectionOnchange();
                }
                    break;
                case 2:
                    listenCanada();
                    break;
            }
        }
    }

    function listenUSAStatesSelectionOnchange() {
        // let model = @Html.Raw(Json.encode(Model.contact));
        USA_STATES_SELECT.style.display = 'block';
        LABEL_FOR_USA_STATES_SELECT.style.display = 'block';
        USA_STATES_SELECT.onchange = function (event) {
            // let contact = "[[${contact}]]";
            // let contact = JSON.parse('${contact}');
            let choosenState = USA_STATES_SELECT.value;
            console.log('state: ' + choosenState);
            CRUTCH.value += choosenState + " ";
        };
    }


    listenCountrySelectOnchange();
    // listenUSAStatesSelectionOnchange();
};
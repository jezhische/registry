window.onload = function () {
    // import statesArray from 'us-states-array'
    let statesArray = [
        "Alaska",
        "Alabama",
        "Arkansas",
        "American_Samoa",
        "Arizona",
        "California",
        "Colorado",
        "Connecticut",
        "District_of_Columbia",
        "Delaware",
        "Florida",
        "Georgia",
        "Guam",
        "Hawaii",
        "Iowa",
        "Idaho",
        "Illinois",
        "Indiana",
        "Kansas",
        "Kentucky",
        "Louisiana",
        "Massachusetts",
        "Maryland",
        "Maine",
        "Michigan",
        "Minnesota",
        "Missouri",
        "Mississippi",
        "Montana",
        "North Carolina",
        "North Dakota",
        "Nebraska",
        "New_Hampshire",
        "New_Jersey",
        "New_Mexico",
        "Nevada",
        "New_York",
        "Ohio",
        "Oklahoma",
        "Oregon",
        "Pennsylvania",
        "Puerto_Rico",
        "Rhode_Island",
        "South_Carolina",
        "South_Dakota",
        "Tennessee",
        "Texas",
        "Utah",
        "Virginia",
        "Virgin_Islands",
        "Vermont",
        "Washington",
        "Wisconsin",
        "West_Virginia",
        "Wyoming"];
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
                    // fillStatesSelect();
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
            let choosenState = USA_STATES_SELECT.value;
            console.log('state: ' + choosenState);
            if (!CRUTCH.value.includes(choosenState)) {
                CRUTCH.value += choosenState + " ";
                let element = document.createElement('DIV');
                element.innerHTML = '<div class="state col-md-12" align="center">'
                    + choosenState + '<span class="round-border">X</span></div>';
                element.onclick = function(event) {
                    this.style.display = 'none';
                    CRUTCH.value = CRUTCH.value.replace(choosenState + ' ', '');
                };
                USA_STATE_SELECTION.append(element);
            }
        };
    }

    function fillStatesSelect() {
        statesArray.forEach(state => {
            let option = document.createElement("OPTION");
            option.value = state;
            option.innerText = state;
            USA_STATES_SELECT.append(option);
        });
    }


    listenCountrySelectOnchange();
    fillStatesSelect();
    // listenUSAStatesSelectionOnchange();
};
const COUNTRY_SELECT = document.querySelector('#country-select');
const USA_SELECTION = document.querySelector('#usa');
const CANADA_SELECTION = document.querySelector('#canada');
const USA_STATE_SELECTION = document.querySelector('#usa-state-selection');
const CANADIAN_PROVINCE_SELECTION = document.querySelector('#canadian-province-selection');

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

function listenSelectOnchange() {
    COUNTRY_SELECT.onchange = function(event) {
        let index = this.selectedIndex;
        console.log(index);
        switch (index) {
            case 0: listenNothing();
            break;
            case 1: listenUSA();
            break;
            case 2: listenCanada();
            break;
        }
    }
}

// listenUSA();
// listenCanada();
listenSelectOnchange();


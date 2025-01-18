document.addEventListener("DOMContentLoaded", function () {
    // Inicjalizacja funkcjonalności przycisków zakładów
    initializeBetButtons();

    // Inicjalizacja funkcjonalności akordeonu
    initializeAccordion();

    // Inicjalizacja funkcjonalności dodawania do koszyka
    initializeAddToBasketButtons();

    //funkcjonalnośc do pobierania i wyswietlania meczy z danej kategorii
    initializeDynamicEventLoading();

    // Inicjalizacja funkcjonalności koszyka
    updateBasketSummary();

    // Walidacja koszyka
    validateBasket();

    //inicjalizacja przycisku do stawiania kuponu
    initializePlaceBetsButton();

    // Inicjalizacja event listenera dla stakeCombo
    initializeStakeComboListener();
});


/**
 * Funkcja dodająca event listener do elementu stakeInput
 * @param {HTMLInputElement} stakeInput - Element input, do którego dodajemy listener
 */
function addStakeInputListener(stakeInput) {
    stakeInput.addEventListener('input', () => {
        const stake = parseFloat(stakeInput.value) || 0;
        if (stake < 0) {
            stakeInput.value = 0;
        } else if (stakeInput.value.split('.')[1]?.length > 2) {
            stakeInput.value = stake.toFixed(2);
        }

        updateBasketSummary();
    });
}

/**
 * Funkcja inicjalizujaca StakeComboListener
 */
function initializeStakeComboListener() {
    const stakeCombo = document.querySelector('#combo-summary .stake-combo');
    if (stakeCombo) {
        addStakeInputListener(stakeCombo);
    }
}

/**
 * Funkcja obsługująca przełączanie między sekcjami "Pojedynczo" i "Kupon"
 */
function initializeBetButtons() {
    const singleBtn = document.getElementById('single-btn');
    const comboBtn = document.getElementById('combo-btn');
    const singleSection = document.getElementById('single-section');
    const comboSection = document.getElementById('combo-section');
    const singleSummary = document.getElementById('single-summary');
    const comboSummary = document.getElementById('combo-summary');
    const basketHeader = document.querySelector('.basket-header h5');

    singleBtn.addEventListener('click', () => {
        toggleSections(singleSection, comboSection);
        toggleSections(singleSummary, comboSummary);
        singleBtn.classList.add('active');
        comboBtn.classList.remove('active');
        basketHeader.innerText = 'Your bets';
        updateBasketSummary();
    });

    comboBtn.addEventListener('click', () => {
        toggleSections(comboSection, singleSection);
        toggleSections(comboSummary, singleSummary);
        singleBtn.classList.remove('active');
        comboBtn.classList.add('active');
        basketHeader.innerText = 'Your coupon';
        updateBasketSummary();
    });
}

/**
 * Funkcja obsługująca działanie akordeonów i aktywowanie linków
 */
function initializeAccordion() {
    const accordions = document.querySelectorAll(".accordion");


    accordions.forEach((accordion) => {
        accordion.addEventListener("show.bs.collapse", (event) => {
            const accordionBody = event.target.querySelector(".accordion-body");
            if (accordionBody) {
                const allLink = accordionBody.querySelector("a:first-child");
                if (allLink) {
                    allLink.click();
                }
            }
        });

        accordion.addEventListener("click", (event) => {
            if (event.target.tagName === "A") {
                updateActiveLink(accordion, event.target);
            }
        });
    });


}

/**
 * Funkcja przełącza widoczność dwóch sekcji
 * @param {HTMLElement} sectionToShow - Sekcja do pokazania
 * @param {HTMLElement} sectionToHide - Sekcja do ukrycia
 */
function toggleSections(sectionToShow, sectionToHide) {
    sectionToShow.classList.remove('d-none');
    sectionToHide.classList.add('d-none');
}

/**
 * Funkcja ustawia aktywny link w akordeonie
 * @param {HTMLElement} accordion - Akordeon, w którym działa funkcja
 * @param {HTMLElement} activeLink - Link, który ma zostać oznaczony jako aktywny
 */
function updateActiveLink(accordion, activeLink) {
    const links = accordion.querySelectorAll("a");
    links.forEach((link) => {
        link.classList.remove("active");
    });
    activeLink.classList.add("active");
}

/**
 * Funkcja czyszcząca koszyk
 */
function clearBasket() {
    const singleBasketBody = document.querySelector('#single-section .basket-items');
    const comboBasketBody = document.querySelector('#combo-section .basket-items');
    const stakeCombo = document.querySelector('#combo-summary .stake-combo');
    singleBasketBody.innerHTML = '';
    comboBasketBody.innerHTML = '';
    stakeCombo.value = '';
    updateBasketSummary();
}

/**
 * Funkcja inicjalizująca przyciski dodawania do koszyka
 */
function initializeAddToBasketButtons() {
    const betButtons = document.querySelectorAll('.event-buttons .btn');
    const singleBasketBody = document.querySelector('#single-section .basket-items');
    const comboBasketBody = document.querySelector('#combo-section .basket-items');
    const singleBtn = document.getElementById('single-btn');
    const comboBtn = document.getElementById('combo-btn');

    betButtons.forEach(button => {
        button.addEventListener('click', () => {
            const eventBox = button.closest('.event-box');
            const betName = eventBox.querySelector('.event-name').innerText;
            const betResult = button.querySelector('span').innerText;
            const eventOdds = button.querySelector('strong').innerText;

            const eventID = eventBox.getAttribute('data-event-id'); //pobieramy id eventu
            const eventEndTime = eventBox.getAttribute('data-event-end-time');
            console.log(eventID);
            //console.log(eventEndTime);

            console.log(betResult);

            let betTeam = '';
            if (betResult === 'Draw') {
                betTeam = 'Draw';
            }
            else if (betResult === 'Team 1') {
                betTeam = eventBox.querySelector('.event-teams .team-name-1').innerText;
            }
            else if (betResult === 'Team 2') {
                betTeam = eventBox.querySelector('.event-teams .team-name-2').innerText;
            }
            
            const basketItem = document.createElement('div');
            basketItem.classList.add('basket-item');

            basketItem.setAttribute('data-event-id', eventID); //Przypisanie eventID do elementu koszyka
            basketItem.setAttribute('data-event-end-time', eventEndTime);

            if (singleBtn.classList.contains('active')) {

                basketItem.innerHTML = `
                <div class="single-item d-flex justify-content-between align-items-start">
                    <div>
                        <div class="bet-name">${betName}</div>
                        <div class="bet-team">${betTeam}</div>
                    </div>
                    <div class="close-and-odds">
                        <button class="btn-close" aria-label="Close"></button>
                        <div class="event-odds">${eventOdds}</div>
                    </div>
                </div>
                <div class="item-stake">
                    <input type="number" min="0" step="0.01" class="stake-single form-control form-control-sm" placeholder="Stake (PLN)">
                    <span class="win-single">
                        <span class="win-single-text">Potential win:</span><br>
                        <span class="win-single-value">0,00</span> PLN
                    </span>
                </div>
            `;

                singleBasketBody.appendChild(basketItem);
            } else if (comboBtn.classList.contains('active')) {

                basketItem.innerHTML = `
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <div class="bet-name">${betName}</div>
                        <div class="bet-team">${betTeam}</div>
                    </div>
                    <div class="close-and-odds">
                        <button class="btn-close" aria-label="Close"></button>
                        <div class="event-odds">${eventOdds}</div>
                    </div>
                </div>
            `;

                comboBasketBody.appendChild(basketItem);
            }

            // Dodaj obsługę usuwania elementu z koszyka
            basketItem.querySelector('.btn-close').addEventListener('click', () => {
                if (singleBtn.classList.contains('active')) {
                    singleBasketBody.removeChild(basketItem);
                } else if (comboBtn.classList.contains('active')) {
                    comboBasketBody.removeChild(basketItem);
                }
                updateBasketSummary();
            });

            if (singleBtn.classList.contains('active')) {
                const stakeInput = basketItem.querySelector('.stake-single');
                if (stakeInput) {
                    addStakeInputListener(stakeInput);
                }
            }
            else if (comboBtn.classList.contains('active')) {
                const stakeCombo = document.querySelector('#combo-summary .stake-combo');
                addStakeInputListener(stakeCombo);
            }

            updateBasketSummary();
        });
    });
}

/**
 * Funkcja aktualizująca koszyk
 */
function updateBasketSummary() {
    const singleBtn = document.getElementById('single-btn');
    const comboBtn = document.getElementById('combo-btn');

    if (singleBtn.classList.contains('active')) {
        updateSingleBasketSummary();
    }
    else if (comboBtn.classList.contains('active')) {
        updateComboBasketSummary();
    }

    validateBasket();

    function updateSingleBasketSummary() {
        const singleBasketItems = document.querySelectorAll('#single-section .basket-item');
        const singleTotalStake = document.querySelector('#single-summary #single-total-stake');
        const singlePotentialWin = document.querySelector('#single-summary #single-potential-win');

        let singleTotal = 0;
        let singleWin = 0;
        singleBasketItems.forEach(item => {
            const stakeInput = item.querySelector('.stake-single');
            const odds = parseFloat(item.querySelector('.event-odds').innerText);
            const stake = parseFloat(stakeInput.value) || 0;
            const winValue = stake * odds * 0.88;

            singleTotal += stake;
            singleWin += winValue;

            item.querySelector('.win-single-value').innerText = winValue.toFixed(2);
        });

        singleTotalStake.innerText = singleTotal.toFixed(2);
        singlePotentialWin.innerText = singleWin.toFixed(2);
    }

    function updateComboBasketSummary() {
        const comboBasketItems = document.querySelectorAll('#combo-section .basket-item');
        const stakeCombo = document.querySelector('#combo-summary .stake-combo');
        const comboTotalOdds = document.querySelector('#combo-summary #combo-total-odds');
        const comboPotentialWin = document.querySelector('#combo-summary #combo-potential-win');

        let comboOdds = 0.00;
        if (comboBasketItems.length > 0) {
            comboOdds = 1.00;
        }

        comboBasketItems.forEach(item => {
            const odds = parseFloat(item.querySelector('.event-odds').innerText);
            comboOdds *= odds;
        });

        let comboTotal = parseFloat(stakeCombo.value) || 0;
        comboTotal = comboTotal.toFixed(2);
        comboTotalOdds.innerText = comboOdds.toFixed(2);
        comboPotentialWin.innerText = (comboTotal * 0.88 * comboOdds).toFixed(2);
    }
}

/**
 * Funkcja walidująca koszyk
 */
function validateBasket() {
    const singleBtn = document.getElementById('single-btn');
    const comboBtn = document.getElementById('combo-btn');
    const summaryButton = document.getElementById('summary-button');
    const walletBalance = parseFloat(document.querySelector('#wallet-balance').innerText);

    let isValid = true;
    let isError = false;
    let message = 'Place a bet';

    if (singleBtn.classList.contains('active')) {
        const singleBasketItems = document.querySelectorAll('#single-section .basket-item');
        const singleTotalStake = parseFloat(document.querySelector('#single-summary #single-total-stake').innerText);
        const eventResults = new Set();

        if (singleBasketItems.length === 0) {
            isValid = false;
            isError = false;
            message = "Add a bet";
        } else {
            for (let i = 0; i < singleBasketItems.length; i++) {
                const item = singleBasketItems[i];
                const eventID = item.getAttribute('data-event-id');
                const betTeam = item.querySelector('.bet-team').innerText;
                const odds = parseFloat(item.querySelector('.event-odds').innerText);

                if (eventResults.has(`${eventID}-${betTeam}`)) {
                    isValid = false;
                    isError = true;
                    message = "Duplicate bet!";
                    break;
                } else {
                    eventResults.add(`${eventID}-${betTeam}`);
                }

                if (odds < 1.14) {
                    isValid = false;
                    isError = true;
                    message = "Minimum odds is 1.14!";
                    break;
                }
            }

            if (isValid && singleTotalStake > walletBalance) {
                isValid = false;
                isError = true;
                message = "Not enough money!";
            }

            if (isValid) {
                for (let i = 0; i < singleBasketItems.length; i++) {
                    const item = singleBasketItems[i];
                    const stakeInput = item.querySelector('.stake-single');
                    let stake = parseFloat(stakeInput.value) || 0;
                    stake = stake.toFixed(2);

                    if (stake <= 0) {
                        isValid = false;
                        isError = false;
                        message = "Provide a stake";
                        break;
                    }
                }
            }
        }
    } else if (comboBtn.classList.contains('active')) {
        const comboBasketItems = document.querySelectorAll('#combo-section .basket-item');
        const eventIDs = new Set();
        const odds = parseFloat(document.querySelector('#combo-summary #combo-total-odds').innerText);
        const stakeCombo = document.querySelector('#combo-summary .stake-combo');
        let stake = parseFloat(stakeCombo.value) || 0;
        stake = stake.toFixed(2);

        if (comboBasketItems.length === 0) {
            isValid = false;
            isError = false;
            message = "Add a bet";
        } else if (comboBasketItems.length === 1) {
            isValid = false;
            isError = false;
            message = "Add a second bet";
        } else {
            for (let i = 0; i < comboBasketItems.length; i++) {
                const item = comboBasketItems[i];
                const eventID = item.getAttribute('data-event-id');

                if (eventIDs.has(eventID)) {
                    isValid = false;
                    isError = true;
                    message = "Conflicting bets!";
                    break;
                } else {
                    eventIDs.add(eventID);
                }
            }

            if (isValid && odds < 1.14) {
                isValid = false;
                isError = true;
                message = "Minimum odds is 1.14!";
            } else if (isValid && stake > walletBalance) {
                isValid = false;
                isError = true;
                message = "Not enough money!";
            } else if (isValid && stake <= 0) {
                isValid = false;
                isError = false;
                message = "Provide a stake";
            }
        }
    }

    if (isValid) {
        summaryButton.disabled = false;
        summaryButton.classList.remove('btn-error');
        summaryButton.innerText = "Place a bet";
    } else {
        summaryButton.disabled = true;
        summaryButton.innerText = message;
        summaryButton.classList.remove('btn-error');
        if (isError) {
            summaryButton.classList.add('btn-error');
        }
    }
}



//STREFA BETOWANIA i pobierania meczy




/**
 * inicajlizacja przycisku do betowania
 */
function initializePlaceBetsButton() {
    const summaryBtn = document.getElementById('summary-button');
    if (!summaryBtn) {
        console.error('Nie znaleziono przycisku summary-button!');
        return;
    }
    summaryBtn.addEventListener('click', () => {
        if (summaryBtn.innerText === 'Place a bet') {
            summaryBtn.innerText = 'Confirm';
        } else if (summaryBtn.innerText === 'Confirm') {
            sendBetsDetailsToBackend();
        }
    });
}
/**
 * funkcja do przekazywania danych o bet z frondend do backend
 */
function sendBetsDetailsToBackend() {
    const singleBtn = document.getElementById('single-btn');
    const comboBtn = document.getElementById('combo-btn');
    const summaryBtn = document.getElementById('summary-button');
    const walletBalance = document.getElementById('wallet-balance');

    summaryBtn.disabled = true;
    summaryBtn.innerText = 'Placing a bet...';
    let newWalletBalance = parseFloat(walletBalance.innerText);

    const betsData = {
        type: singleBtn.classList.contains('active') ? 'SINGLE' : 'COMBO',
        bets: [],
    };
    /*
    POJEDYNCZE ZAKŁADY!
    */
    if (singleBtn.classList.contains('active')) {
        //dane do pojedynczych zakładów
        const singleBasketItems = document.querySelectorAll('#single-section .basket-item');
        singleBasketItems.forEach(item => {
            const eventID = parseInt(item.getAttribute('data-event-id'));
            const odds = parseFloat(item.querySelector('.event-odds').innerText);
            const stake = parseFloat(item.querySelector('.stake-single').value);
            const potentialWin = parseFloat(item.querySelector('.win-single-value').innerText);
            const betTeam = item.querySelector('.bet-team').innerText;
            const betType = betTeam === 'Draw' ? 'DRAW' :
                (betTeam === item.querySelector('.bet-team').innerText ? 'TEAM_1' : 'TEAM_2');

            const betEndTime = item.getAttribute('data-event-end-time');

            betsData.bets.push({
                eventID,
                betTeam,
                betType,
                odds,
                stake,
                potentialWin,
                betEndTime,
            });

            newWalletBalance -= stake;
        });
    }
    /*
    KUPONY!
    */
    else if (comboBtn.classList.contains('active')) {
        //zbieranie danych dla kuponów
        const comboBasketItems = document.querySelectorAll('#combo-section .basket-item');
        const stakeCombo = document.querySelector('#combo-summary .stake-combo');
        const comboTotalOdds = document.querySelector('#combo-summary #combo-total-odds');
        const comboPotentialWin = document.querySelector('#combo-summary #combo-potential-win');

        const stake = parseFloat(stakeCombo.value) || 0;
        const totalOdds = parseFloat(comboTotalOdds.innerText) || 0;
        const potentialWin = parseFloat(comboPotentialWin.innerText) || 0;

        let maxEndTime = null; //najpizniejze betEndTime to bedzie endTime naszego kuponu całego

        comboBasketItems.forEach(item => {
            //solo-bets info do kuponu
            const eventID = parseInt(item.getAttribute('data-event-id'));
            const betEndTime = item.getAttribute('data-event-end-time');
            const odds = parseFloat(item.querySelector('.event-odds').innerText);
            const betTeam = item.querySelector('.bet-team').innerText;
            const betType = betTeam === 'Draw' ? 'DRAW' :
                (betTeam === item.querySelector('.bet-team').innerText ? 'TEAM_1' : 'TEAM_2');

            //aktualizujemy jak betTime jest wiekszy niz maxEndtime
            if (!maxEndTime || new Date(betEndTime) > new Date(maxEndTime)) {
                maxEndTime = betEndTime;
            }

            betsData.bets.push({
                eventID,
                betTeam,
                betType,
                odds,
                betEndTime,
            });
        });

        //teraz info do kuponu potrzebne
        betsData.couponData = ({
            odds: totalOdds,
            stake: stake,
            potentialWin,
            endTime: maxEndTime,
        });

        newWalletBalance -= stake;

    }
    console.log('Dane zakładów:', betsData);

    /*
    Content-Type mówi serwerowi, że wysyłane dane są w formacie JSON.
    Accept mówi serwerowi, że oczekujesz odpowiedzi w formacie JSON.
     */

    //wysyłanie danych do backend
    fetch(`bets/place`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify(betsData),
        credentials: 'same-origin', //ciasteczka maja zostac przesłane
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(errorData.error);
                });
            }
            return response.json();
        })
        .then(data => {
            //console.log('Zakłady wysłane pomyślnie:', data);
            // alert('Bets have been accepted!');
            walletBalance.innerText = newWalletBalance.toFixed(2);
            clearBasket();
        })
        .catch(error => {
            console.error('Błąd:', error);
            alert('Error: : ' + error.message);
        });
}




/**
 * endpoint do dynamicznego pobierania meczy z danej kategorii
 */
function initializeDynamicEventLoading(){
    const categoryLinks = document.querySelectorAll('.categories-box a');
    const eventsContainer = document.querySelector('.events-container');

    categoryLinks.forEach(link=>{
        link.addEventListener('click', function(event){
            event.preventDefault()

            //pobieramy categori i subkategorie
            //this.closest() - szuka najbliższego przodka elementu (w tym przypadku linku)
            const categoryButton = this.closest('div.accordion-item').querySelector('.accordion-button');
            const category = categoryButton.dataset.categoryId;
            const subcategory = this.dataset.subcategoryId;

            eventsContainer.innerHTML = '<p>Loading events...</p>';

            fetch(`/api/events?category=${category}&subcategory=${subcategory}`)
                .then(response => response.json())
                .then(events => {
                    eventsContainer.innerHTML = '';

                    if (events && events.length > 0) {
                        events.forEach(event => {
                            const eventTime = event.eventStartTime.replace('T', ' ').slice(0, 16);
                            const eventEndTime = event.eventEndTime.replace('T', ' ').slice(0, 16);

                            //id i eventEndTime przekezuje tutaj jako aktrybut potem do pobrania przy tworzeniu zakładu
                            //żeby potem nie musieć zadawac kolejnego pytania do bazy przy dodawaniu bet do bazy.
                            const eventHtml = `
                                <div class="event-box rounded" data-event-id="${event.id}" data-event-end-time="${eventEndTime}">
                                    <div class="event-header d-flex justify-content-between">
                                        <span class="event-date-time">${eventTime}</span>
                                        <span class="event-name">${event.eventName}</span>
                                    </div>
                                    <div class="event-teams d-flex justify-content-between my-2">
                                        <span class="team-name-1">${event.team1}</span>
                                        <span class="team-name-2">${event.team2}</span>
                                    </div>
                                    <div class="event-buttons d-flex justify-content-between">
                                        <button class="btn btn-outline-primary btn-sm"><span>Team 1</span><br><strong>${event.oddsTeam1}</strong></button>
                                        <button class="btn btn-outline-primary btn-sm"><span>Draw</span><br><strong>${event.oddsDraw}</strong></button>
                                        <button class="btn btn-outline-primary btn-sm"><span>Team 2</span><br><strong>${event.oddsTeam2}</strong></button>
                                    </div>
                                </div>`;
                            eventsContainer.innerHTML += eventHtml;
                        });
                    } else {
                        eventsContainer.innerHTML = '<p>No events found for this category.</p>';
                    }

                    //inicjalizacja przycisków
                    initializeAddToBasketButtons()

                })
                .catch(error => {
                    console.error('Error loading events:', error);
                    eventsContainer.innerHTML = '<p>Error loading events. Please try again later.</p>';
                });
        });
    });

    const firstCategoryButton = document.querySelector('.accordion-item:first-child .accordion-button');
    const firstSubcategoryLink = document.querySelector('.accordion-item:first-child .categories-box a:first-child');

    if (firstCategoryButton) {
        firstCategoryButton.click(); // Rozwija pierwszą kategorię
    }
    if (firstSubcategoryLink) {
        firstSubcategoryLink.click(); // Wybiera pierwszą podkategorię
    }
}
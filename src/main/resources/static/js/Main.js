document.addEventListener("DOMContentLoaded", function () {
    // Inicjalizacja funkcjonalności przycisków zakładów
    initializeBetButtons();

    // Inicjalizacja funkcjonalności akordeonu
    initializeAccordion();

    // Inicjalizacja funkcjonalności dodawania do koszyka
    initializeAddToBasketButtons();
});

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

    singleBtn.addEventListener('click', () => {
        toggleSections(singleSection, comboSection);
        toggleSections(singleSummary, comboSummary);
        singleBtn.classList.add('active');
        comboBtn.classList.remove('active');
    });

    comboBtn.addEventListener('click', () => {
        toggleSections(comboSection, singleSection);
        toggleSections(comboSummary, singleSummary);
        singleBtn.classList.remove('active');
        comboBtn.classList.add('active');
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

            console.log(betResult);

            let betTeam = '';
            if (betResult == 'X') {
                betTeam = 'Draw';
            }
            else if (betResult === '1') {
                betTeam = eventBox.querySelector('.event-teams .team-name-1').innerText;
            }
            else if (betResult === '2') {
                betTeam = eventBox.querySelector('.event-teams .team-name-2').innerText;
            }
            
            const basketItem = document.createElement('div');
            basketItem.classList.add('basket-item');

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
                    <input type="number" class="stake-single form-control form-control-sm" min=0 placeholder="Stake (PLN)">
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
            });
        });
    });
}

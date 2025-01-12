document.addEventListener("DOMContentLoaded", function () {
    // Inicjalizacja funkcjonalności przycisków zakładów
    initializeBetButtons();

    // Inicjalizacja funkcjonalności akordeonu
    initializeAccordion();

    // Inicjalizacja funkcjonalności dodawania do koszyka
    initializeAddToBasketButtons();

    //funkcjonalnośc do pobierania i wyswietlania meczy z danej kategorii
    initializeDynamicEventLoading();

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

        // // Automatically expand the first accordion item
        // const firstAccordionButton = accordion.querySelector(".accordion-item:first-child .accordion-button");
        // const firstAccordionBody = accordion.querySelector(".accordion-item:first-child .accordion-body");
        // if (firstAccordionButton) {
        //     firstAccordionButton.click();
        // }
        // if (firstAccordionBody) {
        //     const firstAllLink = firstAccordionBody.querySelector("a:first-child");
        //     if (firstAllLink) {
        //         firstAllLink.click();
        //     }
        // }
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
            if (betResult === 'X') {
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
                            const eventHtml = `
                                <div class="event-box rounded">
                                    <div class="event-header d-flex justify-content-between">
                                        <span class="event-date-time">${eventTime}</span>
                                        <span class="event-name">${event.eventName}</span>
                                    </div>
                                    <div class="event-teams d-flex justify-content-between my-2">
                                        <span class="team-name-1">${event.team1}</span>
                                        <span class="team-name-2">${event.team2}</span>
                                    </div>
                                    <div class="event-buttons d-flex justify-content-between">
                                        <button class="btn btn-outline-primary btn-sm"><span>1</span><br><strong>${event.oddsTeam1}</strong></button>
                                        <button class="btn btn-outline-primary btn-sm"><span>X</span><br><strong>${event.oddsDraw}</strong></button>
                                        <button class="btn btn-outline-primary btn-sm"><span>2</span><br><strong>${event.oddsTeam2}</strong></button>
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














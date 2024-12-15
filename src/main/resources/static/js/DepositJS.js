//Deposit simple animations

const blikIcon = document.getElementById("blikIcon")
const blikCodeContainer = document.getElementById("blikCodeContainer");
const confirmationButton = document.getElementById("confirmBtn");
const confirmationMessage = document.getElementById("confirmationMessage");


blikIcon.addEventListener("click", ()=> {
    blikCodeContainer.style.display="block"});

confirmationButton.addEventListener("click", () =>{
    confirmationMessage.style.display="block"});



//Deposit simple animations

const blikIcon = document.getElementById("blikIcon")
const blikCodeContainer = document.getElementById("blikCodeContainer");
const returnToAccountContainer = document.getElementById("returnToAccount");
const confirmationButton = document.getElementById("confirmPayment");
const amountInput = document.getElementById("amount");
const blikCodeInput = document.getElementById("blikCode");

blikIcon.addEventListener("click", ()=> {
        blikCodeContainer.style.display = "block"
        returnToAccountContainer.style.display = "none"
    }
);

const successMessage = document.querySelector('.confirmation-message');
if (successMessage) {
    successMessage.style.display = 'block'; //pokazanie komunikatu

    //przekirowanie na strone account (3s)
    setTimeout(() => {
        window.location.href = '/Account';
    }, 3000);
}


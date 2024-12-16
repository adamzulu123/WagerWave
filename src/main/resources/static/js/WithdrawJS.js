
const confirmWithdrawalBtn = document.getElementById("confirmWithdrawal");
const confirmationMessage = document.getElementById("confirmationMessage");

confirmWithdrawalBtn.addEventListener("click", ()=> {
   confirmationMessage.style.display ="block"
});

const successMessage = document.querySelector('.confirmation-message');
if (successMessage) {
   successMessage.style.display = 'block'; //pokazanie komunikatu

   //przekirowanie na strone account (3s)
   setTimeout(() => {
      window.location.href = '/Account';
   }, 3000);
}



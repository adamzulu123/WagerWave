const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

registerBtn.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

loginBtn.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});


document.addEventListener("DOMContentLoaded", () =>{
    const registerSuccess = document.getElementById("successRegisterMessage");
    const registerError = document.getElementById("errorRegisterMessage");

    //wyłacznie komunikatu po 5 sec
    setTimeout(() => {
        if (registerSuccess) {
            registerSuccess.style.opacity = "0";
            setTimeout(() => registerSuccess.style.visibility = "hidden", 1000);
        }
        if (registerError) {
            registerError.style.opacity = "0";
            setTimeout(() => registerError.style.visibility = "hidden", 1000);
        }
    }, 10000);
});





function validateForm() {
    const password = document.getElementById("password").value;
    const confirm = document.getElementById("confirmPassword").value;
    const mismatchMessage = document.getElementById("passwordMismatch");

    // Password policy: min 8 chars, 1 letter, 1 number
    const validPassword = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

    if (!validPassword.test(password)) {
        alert("Le mot de passe doit contenir au moins 8 caractères, avec au moins une lettre et un chiffre.");
        return false;
    }

    if (password !== confirm) {
        mismatchMessage.classList.remove("d-none");
        return false;
    } else {
        mismatchMessage.classList.add("d-none");
    }

    return true;
}
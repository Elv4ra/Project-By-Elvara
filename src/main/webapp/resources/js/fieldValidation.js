function validate(el, message) {
    if(el.checkValidity()) {
        el.style.cssText = "background: rgba(0, 255, 0, 0.25); outline: 1px solid rgba(0, 255, 0, 1);";
    } else {
        el.style.cssText = "background: rgba(255, 0, 0, 0.25); outline: 1px solid rgba(255, 0, 0, 1);";
        el.setCustomValidity(message);
        el.reportValidity();
    }
}

function removeValidationMessage(el) {
    el.setCustomValidity("");
    return true;
}

function validateEmail(el, message, email) {
    el.setCustomValidity("");
    if(el.value == email) {
        el.style.cssText = "background: rgba(0, 255, 0, 0.25); outline: 1px solid rgba(0, 255, 0, 1);";
    } else {
        el.style.cssText = "background: rgba(255, 0, 0, 0.25); outline: 1px solid rgba(255, 0, 0, 1);";
        el.setCustomValidity(message);
        el.reportValidity();
    }
}

function validatePriceAndSize(price, size) {
    el.setCustomValidity("");
    if (parseFloat(price.value) == 0) {
        el.style.cssText = "background: rgba(255, 0, 0, 0.25); outline: 1px solid rgba(255, 0, 0, 1);";
        el.setCustomValidity("Ціна має бути більшою за 0");
        el.reportValidity();
        return false;
    }
    if (parseFloat(size.value.split(/[a-zA-Zа-яёА-ЯЁіїєІЇЄ]/)[0]) == 0) {
        el.style.cssText = "background: rgba(255, 0, 0, 0.25); outline: 1px solid rgba(255, 0, 0, 1);";
        el.setCustomValidity("Числове значення об`єму має бути більшим за 0");
        el.reportValidity();
        return false;
    }
    return true;
}

function dropStyle(el) {
    el.style.cssText = "outline: 2px solid rgba(255, 0, 200, 1); background: rgba(255, 255, 255, 1);"
}

function validateFilter(el, message) {
    el.setCustomValidity("");
    if(el.value != "") {
        el.style.cssText = "background: rgba(0, 255, 0, 0.25); outline: 1px solid rgba(0, 255, 0, 1);";
    } else {
        el.style.cssText = "background: rgba(255, 0, 0, 0.25); outline: 1px solid rgba(255, 0, 0, 1);";
        el.setCustomValidity(message);
        el.reportValidity();
    }
}

function callValidityReport() {
    var fields = document.getElementsByTagName("input");
    for (let i = 0; i < fields.length-1; i++) {
        if(!fields[i].checkValidity()) {
            fields[i].style.cssText = "background: rgba(255, 0, 0, 0.25); outline: 1px solid rgba(255, 0, 0, 1);";
            fields[i].setCustomValidity("Заповніть поле");
            fields[i].reportValidity();
            break;
        }
    }
}
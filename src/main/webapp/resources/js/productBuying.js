function callModal() {
    var modal = document.getElementById("myModal");
    modal.style.display = "block";
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}

function changeTotal(el, price) {
    el.setCustomValidity("");
    if(parseInt(el.value) < 1) {
        el.setCustomValidity("Кількість товару має бути більшою за 1");
        el.reportValidity();
    }
    if(parseInt(el.value) > parseInt(el.max)) {
        el.setCustomValidity("Кількість товару має бути меншою за " + el.max);
        el.reportValidity();
    }
    var total = el.value * price;
    document.getElementById("total").textContent = total.toFixed(2);
}

function showAddress() {
    var address = document.myForm.address;
    if (document.myForm.delivery.value == 'DELIVERY') {
        address.style.display = 'block';
        address.disabled = false;
        address.setAttribute("required", "");
        document.getElementById('displayableLabel').style.display = 'block';
    }
    else {
        address.setCustomValidity("");
        address.style.display = 'none';
        address.disabled = true;
        address.required = false;
        document.getElementById('displayableLabel').style.display = 'none';
    }
}
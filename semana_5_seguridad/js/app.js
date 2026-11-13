// js/app.js - confirmaciones de formularios (sin JavaScript en línea, compatible con la política CSP)
document.addEventListener("submit", function (evento) {
    var formulario = evento.target;
    var mensaje = formulario.getAttribute("data-confirmar");
    if (mensaje && !window.confirm(mensaje)) {
        evento.preventDefault();
    }
});

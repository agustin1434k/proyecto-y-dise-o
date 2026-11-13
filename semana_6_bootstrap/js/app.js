// js/app.js - comportamiento del modal de confirmación de eliminación.
// Sin JavaScript en línea (compatible con la política CSP). La eliminación real
// la controla eliminar.php en el servidor (POST + token CSRF + validación del ID).
document.addEventListener("DOMContentLoaded", function () {
    var modal = document.getElementById("modalEliminar");
    if (!modal) {
        return;
    }
    modal.addEventListener("show.bs.modal", function (evento) {
        var boton = evento.relatedTarget; // el botón "Eliminar" que abrió el modal
        if (!boton) {
            return;
        }
        document.getElementById("modalEliminarId").value = boton.getAttribute("data-id") || "";
        // textContent (no innerHTML): el nombre se muestra como texto, nunca como HTML
        document.getElementById("modalEliminarNombre").textContent = boton.getAttribute("data-nombre") || "";
    });
});

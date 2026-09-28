// =========================================================================
// SELECCIÓN DE CLIENTE
// =========================================================================
function seleccionarCliente(btn) {
    let id = btn.dataset.id;
    let nombre = btn.dataset.nombre;

    if (document.getElementById("idCliente")) document.getElementById("idCliente").value = id;
    if (document.getElementById("nombreCliente")) document.getElementById("nombreCliente").value = nombre;

    // Limpiar la mascota anterior al cambiar de cliente
    if (document.getElementById("idMascota")) document.getElementById("idMascota").value = "";
    if (document.getElementById("nombreMascota")) document.getElementById("nombreMascota").value = "";
    sessionStorage.removeItem("mascotaId");
    sessionStorage.removeItem("mascotaNombre");

    sessionStorage.setItem("clienteId", id);
    sessionStorage.setItem("clienteNombre", nombre);

    let modalEl = document.getElementById("modalClientes");
    let modalInstance = bootstrap.Modal.getInstance(modalEl);
    if (modalInstance) {
        modalInstance.hide();
    } else {
        modalEl.querySelector(".btn-close").click();
    }
}

// =========================================================================
// SELECCIÓN DE MASCOTA
// =========================================================================
function seleccionarMascota(btn) {
    let id = btn.dataset.id;
    let nombre = btn.dataset.nombre;

    if (document.getElementById("idMascota")) document.getElementById("idMascota").value = id;
    if (document.getElementById("nombreMascota")) document.getElementById("nombreMascota").value = nombre;

    sessionStorage.setItem("mascotaId", id);
    sessionStorage.setItem("mascotaNombre", nombre);

    let modalEl = document.getElementById("modalMascotas");
    let modalInstance = bootstrap.Modal.getInstance(modalEl);
    if (modalInstance) {
        modalInstance.hide();
    } else {
        modalEl.querySelector(".btn-close").click();
    }
}

// =========================================================================
// SELECCIÓN DE CAJERA
// =========================================================================
function seleccionarCajera(btn) {
    let id = btn.dataset.id;
    let nombre = btn.dataset.nombre;

    if (document.getElementById("codCajera")) document.getElementById("codCajera").value = id;
    if (document.getElementById("nombreCajera")) document.getElementById("nombreCajera").value = nombre;

    sessionStorage.setItem("cajeraId", id);
    sessionStorage.setItem("cajeraNombre", nombre);

    let modalEl = document.getElementById("modalCajeras");
    let modalInstance = bootstrap.Modal.getInstance(modalEl);
    if (modalInstance) {
        modalInstance.hide();
    } else {
        modalEl.querySelector(".btn-close").click();
    }
}

// =========================================================================
// LIMPIAR DATOS DE LA SESIÓN AL CANCELAR O TERMINAR
// =========================================================================
function limpiarDatosVenta() {
    sessionStorage.clear();
}

// =========================================================================
// EVENTOS AL RECARGAR / CARGAR LA PÁGINA
// =========================================================================
document.addEventListener("DOMContentLoaded", () => {

    // 1. Restaurar Inputs desde Memoria
    let clienteId = sessionStorage.getItem("clienteId");
    let clienteNombre = sessionStorage.getItem("clienteNombre");
    if (clienteId && document.getElementById("idCliente")) {
        document.getElementById("idCliente").value = clienteId;
        document.getElementById("nombreCliente").value = clienteNombre;
    }

    let mascotaId = sessionStorage.getItem("mascotaId");
    let mascotaNombre = sessionStorage.getItem("mascotaNombre");
    if (mascotaId && document.getElementById("idMascota")) {
        document.getElementById("idMascota").value = mascotaId;
        document.getElementById("nombreMascota").value = mascotaNombre;
    }

    let cajeraId = sessionStorage.getItem("cajeraId");
    let cajeraNombre = sessionStorage.getItem("cajeraNombre");
    if (cajeraId && document.getElementById("codCajera")) {
        document.getElementById("codCajera").value = cajeraId;
        document.getElementById("nombreCajera").value = cajeraNombre;
    }

    // 2. Filtro Inteligente: Mostrar solo mascotas del cliente (Filtrado por ID exacto)
    let modalMascotasEl = document.getElementById("modalMascotas");
    if(modalMascotasEl) {
        modalMascotasEl.addEventListener('show.bs.modal', function (event) {
            let idClienteActual = document.getElementById("idCliente").value;
            
            if(!idClienteActual) {
                alert("Por favor, busque y seleccione un cliente primero.");
                event.preventDefault(); // Bloquea la apertura
                return;
            }

            document.querySelectorAll(".fila-mascota").forEach(fila => {
                let idDueno = fila.getAttribute("data-cliente");
                if(idDueno === idClienteActual) {
                    fila.style.display = ""; // Se muestra porque es de este cliente
                } else {
                    fila.style.display = "none"; // Se oculta porque es de otro
                }
            });
        });
    }

    // 3. Buscadores en tiempo real
    const txtBuscarCliente = document.getElementById("buscarCliente");
    if (txtBuscarCliente) {
        txtBuscarCliente.addEventListener("keyup", function () {
            let filtro = this.value.toLowerCase();
            document.querySelectorAll("#tablaClientes tbody tr").forEach(fila => {
                if(fila.cells.length > 1) {
                    let texto = fila.textContent.toLowerCase();
                    fila.style.display = texto.includes(filtro) ? "" : "none";
                }
            });
        });
    }

    const txtBuscarMascota = document.getElementById("buscarMascota");
    if (txtBuscarMascota) {
        txtBuscarMascota.addEventListener("keyup", function () {
            let filtro = this.value.toLowerCase();
            let idClienteActual = document.getElementById("idCliente").value;

            document.querySelectorAll(".fila-mascota").forEach(fila => {
                let idDueno = fila.getAttribute("data-cliente");
                let texto = fila.textContent.toLowerCase();
                
                // Muestra la fila SOLO si le pertenece al cliente Y coincide con el texto buscado
                if(idDueno === idClienteActual && texto.includes(filtro)) {
                    fila.style.display = "";
                } else {
                    fila.style.display = "none";
                }
            });
        });
    }

    // 4. Limpiar al enviar/cancelar
    const formVenta = document.getElementById("formVenta");
    if (formVenta) {
        formVenta.addEventListener("submit", () => {
            setTimeout(limpiarDatosVenta, 500);
        });
    }

    const btnCancelar = document.querySelector("a[href*='/venta/limpiar']");
    if (btnCancelar) {
        btnCancelar.addEventListener("click", () => {
            limpiarDatosVenta();
        });
    }

    // 5. SweetAlert
    document.querySelectorAll("form[action*='/venta/quitar/']").forEach(form => {
        form.addEventListener("submit", function(e) {
            e.preventDefault();
            Swal.fire({
                title: '¿Quitar producto?',
                text: 'Se eliminará del carrito actual',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: 'Sí, quitar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    form.submit();
                }
            });
        });
    });
});
(function () {
    'use strict';

    /* Modales */
    document.querySelectorAll('[data-modal-open]').forEach(function (trigger) {
        trigger.addEventListener('click', function () {
            var id = trigger.getAttribute('data-modal-open');
            var modal = document.getElementById(id);
            if (modal) modal.classList.add('open');
        });
    });

    document.querySelectorAll('[data-modal-close]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var modal = btn.closest('.modal-overlay');
            if (modal) modal.classList.remove('open');
        });
    });

    document.querySelectorAll('.modal-overlay').forEach(function (overlay) {
        overlay.addEventListener('click', function (e) {
            if (e.target === overlay) overlay.classList.remove('open');
        });
    });

    /* Tabs servicios */
    document.querySelectorAll('.tabs').forEach(function (tabsEl) {
        var buttons = tabsEl.querySelectorAll('.tab-btn');
        var container = tabsEl.closest('section') || document;
        buttons.forEach(function (btn) {
            btn.addEventListener('click', function () {
                var target = btn.getAttribute('data-tab');
                buttons.forEach(function (b) { b.classList.remove('active'); });
                btn.classList.add('active');
                container.querySelectorAll('.tab-panel').forEach(function (panel) {
                    panel.classList.toggle('active', panel.getAttribute('data-panel') === target);
                });
            });
        });
    });

    /* Modal especialista (profesionales) */
    document.querySelectorAll('.pro-card[data-especialista]').forEach(function (card) {
        card.addEventListener('click', function () {
            var data = JSON.parse(card.getAttribute('data-especialista'));
            var modal = document.getElementById('modal-especialista');
            if (!modal) return;
            modal.querySelector('[data-field="nombre"]').textContent = data.nombre;
            modal.querySelector('[data-field="titulo"]').textContent = data.titulo;
            modal.querySelector('[data-field="foto"]').src = data.fotoUrl;
            modal.querySelector('[data-field="foto"]').alt = data.nombre;
            var formacion = modal.querySelector('[data-field="formacion"]');
            formacion.innerHTML = '';
            (data.formacion || []).forEach(function (f) {
                var li = document.createElement('li');
                li.textContent = f;
                formacion.appendChild(li);
            });
            var coleg = document.createElement('li');
            coleg.textContent = data.colegiatura;
            formacion.appendChild(coleg);
            var tags = modal.querySelector('[data-field="especialidades"]');
            tags.innerHTML = '';
            (data.especialidades || []).forEach(function (esp) {
                var span = document.createElement('span');
                span.className = 'tag';
                span.textContent = esp;
                tags.appendChild(span);
            });
            modal.classList.add('open');
        });
    });

    /* Reserva: stepper */
    var reservaForm = document.getElementById('reserva-form');
    if (!reservaForm) return;

    var currentStep = 0;
    var steps = reservaForm.querySelectorAll('.step-panel');
    var stepperBtns = reservaForm.querySelectorAll('.stepper-btn');
    var btnPrev = reservaForm.querySelector('[data-step-prev]');
    var btnNext = reservaForm.querySelector('[data-step-next]');
    var btnSubmit = reservaForm.querySelector('[data-step-submit]');

    function formatearFechaCorta(iso) {
        if (!iso) return '';
        var p = iso.split('-');
        if (p.length !== 3) return iso;
        return parseInt(p[2], 10) + '/' + parseInt(p[1], 10) + '/' + p[0];
    }

    function formatoModalidad() {
        var f = (reservaForm.dataset.paqueteFormato || 'online').toLowerCase();
        return f === 'online' ? 'virtual' : f;
    }

    function actualizarTextosReserva() {
        var nombre = reservaForm.dataset.paqueteNombre || 'paquete';
        var precio = parseFloat(reservaForm.dataset.paquetePrecio || '0');
        var hora = reservaForm.querySelector('[name="hora"]').value;
        var fecha = reservaForm.querySelector('[name="fecha"]').value;
        var esp = reservaForm.dataset.especialistaNombre || '';
        var modalidad = formatoModalidad();

        var textoDatos = document.getElementById('texto-confirmacion-datos');
        if (textoDatos) {
            if (hora && fecha) {
                textoDatos.textContent = 'Ingresa tus datos para confirmar la cita de ' + nombre +
                    ' a las ' + hora + ' del ' + formatearFechaCorta(fecha) +
                    '. Precio: S/. ' + precio.toFixed(1) + '.';
            } else {
                textoDatos.textContent = 'Ingresa tus datos para confirmar la cita de ' + nombre + '.';
            }
        }

        var l1 = document.getElementById('resumen-linea1');
        var l2 = document.getElementById('resumen-linea2');
        var l3 = document.getElementById('resumen-linea3');
        var total = document.getElementById('resumen-total');
        if (l1) l1.textContent = nombre + ' – ' + modalidad;
        if (l2) l2.textContent = esp ? 'Con ' + esp : 'Con —';
        if (l3) l3.textContent = (fecha && hora) ? formatearFechaCorta(fecha) + ' · ' + hora : '—';
        if (total) total.textContent = 'Total: S/' + precio.toFixed(2);
    }

    function actualizarBoletaDniInfo() {
        var info = document.getElementById('boleta-dni-info');
        var dniInput = reservaForm.querySelector('[name="dni"]');
        if (!info || !dniInput) return;
        var dni = dniInput.value.trim();
        info.textContent = dni
            ? 'La boleta se emitirá con el DNI ' + dni + ' ingresado en tus datos.'
            : 'La boleta usará el DNI que ingresaste en el paso anterior.';
    }

    function toggleComprobanteCampos(tipo) {
        var boletaInfo = document.getElementById('boleta-dni-info');
        var ruc = document.getElementById('campo-ruc-comprobante');
        var rucInput = reservaForm.querySelector('[name="ruc"]');
        if (tipo === 'factura') {
            if (boletaInfo) boletaInfo.classList.remove('active');
            if (ruc) ruc.classList.add('active');
            if (rucInput) rucInput.setAttribute('required', 'required');
        } else {
            if (boletaInfo) {
                boletaInfo.classList.add('active');
                actualizarBoletaDniInfo();
            }
            if (ruc) ruc.classList.remove('active');
            if (rucInput) { rucInput.removeAttribute('required'); rucInput.value = ''; }
        }
    }

    function showStep(index) {
        currentStep = index;
        steps.forEach(function (panel, i) {
            panel.classList.toggle('active', i === index);
        });
        stepperBtns.forEach(function (btn, i) {
            btn.classList.toggle('active', i === index);
        });
        btnPrev.disabled = index === 0;
        btnNext.style.display = index < steps.length - 1 ? 'inline-flex' : 'none';
        btnSubmit.style.display = index === steps.length - 1 ? 'inline-flex' : 'none';
        if (index === 2 || index === 3) {
            actualizarTextosReserva();
            if (index === 3) actualizarBoletaDniInfo();
        }
    }

    stepperBtns.forEach(function (btn, i) {
        btn.addEventListener('click', function () {
            if (i < currentStep) showStep(i);
        });
    });

    btnPrev.addEventListener('click', function () {
        if (currentStep > 0) showStep(currentStep - 1);
    });

    btnNext.addEventListener('click', function () {
        if (validateStep(currentStep) && currentStep < steps.length - 1) {
            showStep(currentStep + 1);
        }
    });

    function validateStep(step) {
        if (step === 0) {
            var esp = reservaForm.querySelector('[name="especialistaId"]');
            if (!esp.value) { alert('Selecciona un especialista.'); return false; }
        }
        if (step === 1) {
            var fecha = reservaForm.querySelector('[name="fecha"]');
            var hora = reservaForm.querySelector('[name="hora"]');
            if (!fecha.value) { alert('Selecciona una fecha.'); return false; }
            if (!hora.value) { alert('Selecciona un horario.'); return false; }
        }
        if (step === 2) {
            var required = ['nombreCompleto', 'celular', 'correo', 'dni'];
            for (var i = 0; i < required.length; i++) {
                var field = reservaForm.querySelector('[name="' + required[i] + '"]');
                if (!field || !field.value.trim()) {
                    alert('Completa todos los datos del formulario.');
                    return false;
                }
            }
            var dniField = reservaForm.querySelector('[name="dni"]');
            if (dniField && !/^\d{8}$/.test(dniField.value.trim())) {
                alert('El DNI debe tener 8 dígitos.');
                return false;
            }
        }
        if (step === 3) {
            var comprobante = reservaForm.querySelector('[name="tipoComprobante"]');
            if (!comprobante.value) { alert('Selecciona boleta o factura.'); return false; }
            if (comprobante.value === 'factura') {
                var ruc = reservaForm.querySelector('[name="ruc"]');
                if (!ruc.value.trim()) { alert('Ingresa el número de RUC.'); return false; }
            }
        }
        return true;
    }

    btnSubmit.addEventListener('click', function (e) {
        if (!validateStep(3)) {
            e.preventDefault();
            return;
        }
        if (!validateStep(2)) {
            e.preventDefault();
            showStep(2);
        }
    });

    showStep(0);

    /* Selección especialista en popup reserva */
    var espSeleccionado = null;
    document.querySelectorAll('[data-select-especialista]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var id = btn.getAttribute('data-id');
            var nombre = btn.getAttribute('data-nombre');
            reservaForm.querySelector('[name="especialistaId"]').value = id;
            reservaForm.dataset.especialistaNombre = nombre;
            var label = document.getElementById('especialista-seleccionado');
            if (label) label.textContent = 'Seleccionado: ' + nombre;
            espSeleccionado = id;
            actualizarTextosReserva();
            document.getElementById('modal-elegir-especialista').classList.remove('open');
            cargarHorarios(id);
            initCalendar();
        });
    });

    function cargarHorarios(especialistaId) {
        var grid = document.getElementById('horarios-grid');
        if (!grid) return;
        grid.innerHTML = '<p class="text-muted">Cargando horarios...</p>';
        fetch('/api/horarios/' + especialistaId)
            .then(function (r) { return r.json(); })
            .then(function (horas) {
                grid.innerHTML = '';
                if (!horas.length) {
                    grid.innerHTML = '<p class="text-muted">No hay horarios disponibles.</p>';
                    return;
                }
                horas.forEach(function (h) {
                    var btn = document.createElement('button');
                    btn.type = 'button';
                    btn.className = 'hora-btn';
                    btn.textContent = h;
                    btn.addEventListener('click', function () {
                        grid.querySelectorAll('.hora-btn').forEach(function (b) { b.classList.remove('selected'); });
                        btn.classList.add('selected');
                        reservaForm.querySelector('[name="hora"]').value = h;
                        actualizarTextosReserva();
                    });
                    grid.appendChild(btn);
                });
            })
            .catch(function () {
                grid.innerHTML = '<p class="text-muted">Error al cargar horarios.</p>';
            });
    }

    /* Calendario simple */
    var calMonth = new Date().getMonth();
    var calYear = new Date().getFullYear();
    var calSelected = null;

    function initCalendar() {
        var cal = document.getElementById('calendario');
        if (!cal || cal.dataset.initialized) return;
        cal.dataset.initialized = 'true';
        renderCalendar();
        cal.querySelector('[data-cal-prev]').addEventListener('click', function () {
            calMonth--;
            if (calMonth < 0) { calMonth = 11; calYear--; }
            renderCalendar();
        });
        cal.querySelector('[data-cal-next]').addEventListener('click', function () {
            calMonth++;
            if (calMonth > 11) { calMonth = 0; calYear++; }
            renderCalendar();
        });
    }

    function renderCalendar() {
        var cal = document.getElementById('calendario');
        if (!cal) return;
        var grid = cal.querySelector('.cal-grid-days');
        var title = cal.querySelector('[data-cal-title]');
        var meses = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
        title.textContent = meses[calMonth] + ' ' + calYear;
        grid.innerHTML = '';
        var first = new Date(calYear, calMonth, 1).getDay();
        var days = new Date(calYear, calMonth + 1, 0).getDate();
        var today = new Date();
        today.setHours(0,0,0,0);
        for (var i = 0; i < first; i++) {
            var empty = document.createElement('span');
            grid.appendChild(empty);
        }
        for (var d = 1; d <= days; d++) {
            var btn = document.createElement('button');
            btn.type = 'button';
            btn.className = 'cal-day';
            btn.textContent = d;
            var date = new Date(calYear, calMonth, d);
            if (date < today) btn.disabled = true;
            if (calSelected && calSelected.getDate() === d && calSelected.getMonth() === calMonth && calSelected.getFullYear() === calYear) {
                btn.classList.add('selected');
            }
            btn.addEventListener('click', function (day, dateObj) {
                return function () {
                    calSelected = dateObj;
                    var fechaInput = reservaForm.querySelector('[name="fecha"]');
                    var y = dateObj.getFullYear();
                    var m = String(dateObj.getMonth() + 1).padStart(2, '0');
                    var dd = String(dateObj.getDate()).padStart(2, '0');
                    fechaInput.value = y + '-' + m + '-' + dd;
                    var label = document.getElementById('fecha-seleccionada');
                    if (label) {
                        label.textContent = dateObj.toLocaleDateString('es-PE', { weekday: 'long', day: 'numeric', month: 'long' });
                    }
                    renderCalendar();
                    actualizarTextosReserva();
                };
            }(d, date));
            grid.appendChild(btn);
        }
    }

    document.querySelectorAll('.comprobante-opcion').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var tipo = btn.getAttribute('data-comprobante');
            reservaForm.querySelector('[name="tipoComprobante"]').value = tipo;
            document.querySelectorAll('.comprobante-opcion').forEach(function (b) { b.classList.remove('selected'); });
            btn.classList.add('selected');
            toggleComprobanteCampos(tipo);
        });
    });

    toggleComprobanteCampos('boleta');

    /* Métodos de pago */
    document.querySelectorAll('.pago-metodo:not(.comprobante-opcion)').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var metodo = btn.getAttribute('data-metodo');
            reservaForm.querySelector('[name="metodoPago"]').value = metodo;
            document.querySelectorAll('.pago-metodo:not(.comprobante-opcion)').forEach(function (b) { b.classList.remove('selected'); });
            btn.classList.add('selected');
            document.querySelectorAll('.pago-panel').forEach(function (panel) {
                panel.classList.toggle('active', panel.getAttribute('data-pago') === metodo);
            });
        });
    });

    var firstPago = document.querySelector('.pago-metodo:not(.comprobante-opcion)');
    if (firstPago) firstPago.click();
})();

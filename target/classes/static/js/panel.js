(function () {
    'use strict';

    /* Modales comunes */
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

    function openModal(id) {
        var modal = document.getElementById(id);
        if (modal) modal.classList.add('open');
    }

    /* Búsqueda historia: actualizar placeholder al cambiar tipo */
    var selectTipo = document.getElementById('tipo');
    var inputValor = document.getElementById('valor');

    function actualizarCamposBusqueda() {
        if (!selectTipo) return;
        var esCodigo = selectTipo.value === 'codigo';
        document.querySelectorAll('.valor-label-dni').forEach(function (el) {
            el.classList.toggle('hidden', esCodigo);
        });
        document.querySelectorAll('.valor-label-codigo').forEach(function (el) {
            el.classList.toggle('hidden', !esCodigo);
        });
        document.querySelectorAll('.valor-hint-dni').forEach(function (el) {
            el.classList.toggle('hidden', esCodigo);
        });
        document.querySelectorAll('.valor-hint-codigo').forEach(function (el) {
            el.classList.toggle('hidden', !esCodigo);
        });
        if (inputValor) {
            inputValor.placeholder = esCodigo ? 'Ej. HC-2026-00001' : 'Ej. 72839102';
        }
    }

    if (selectTipo) {
        selectTipo.addEventListener('change', actualizarCamposBusqueda);
        actualizarCamposBusqueda();
    }

    /* Historia clínica */
    var historiaActual = null;
    var anamnesisEditando = false;
    var diagEditIndex = null;
    var visitaEditIndex = null;
    var medEditIndex = null;
    var evaluacionEditIndex = null;

    var CIE10 = {
        'F41.1': 'Trastorno de ansiedad generalizada',
        'F40.1': 'Fobia social',
        'F32.1': 'Episodio depresivo moderado',
        'F33.0': 'Trastorno depresivo recurrente, episodio leve',
        'F43.1': 'Trastorno de estrés postraumático',
        'F90.0': 'Trastorno por déficit de atención con hiperactividad',
        'E03.9': 'Hipotiroidismo, no especificado'
    };

    var hcData = {
        diagnosticos: [
            { codigo: 'F41.1', nombre: 'Trastorno de ansiedad generalizada' },
            { codigo: 'F40.1', nombre: 'Fobia social' },
            { codigo: 'E03.9', nombre: 'Hipotiroidismo, no especificado' }
        ],
        visitas: [
            { fecha: '2024-04-07', hora: '10:00', sesion: 3, descripcion: 'Seguimiento de técnicas de respiración.', estado: 'Pendiente' },
            { fecha: '2024-04-01', hora: '10:00', sesion: 2, descripcion: 'Exploración de eventos disparadores.', estado: 'Completada' },
            { fecha: '2024-03-24', hora: '10:00', sesion: 1, descripcion: 'Primera evaluación e inicio del plan.', estado: 'Completada' }
        ],
        medicacion: [
            { nombre: 'Sertralina 50 mg', dosis: '1 vez/día', desde: '2023-12-10', hasta: 'Actualidad' },
            { nombre: 'Melatonina 3 mg', dosis: '1 vez/día', desde: '2023-10-10', hasta: '2024-02-15' }
        ],
        evaluaciones: [
            { nombre: 'GAD-7 — Ansiedad Generalizada', interpretacion: 'Severa (>15)', puntaje: '15/21' },
            { nombre: 'PHQ-9 — Depresión', interpretacion: 'Leve (5-9)', puntaje: '9/27' }
        ]
    };

    function leerHistoriaDesdeBtn(btn) {
        return {
            codigo: btn.getAttribute('data-hc-codigo'),
            paciente: btn.getAttribute('data-hc-paciente'),
            documento: btn.getAttribute('data-hc-documento'),
            fechaNacimiento: btn.getAttribute('data-hc-fecha-nac'),
            edad: btn.getAttribute('data-hc-edad'),
            sexo: btn.getAttribute('data-hc-sexo'),
            gradoInstruccion: btn.getAttribute('data-hc-grado'),
            ocupacion: btn.getAttribute('data-hc-ocupacion'),
            ultimaCita: btn.getAttribute('data-hc-ultima'),
            numeroHijos: btn.getAttribute('data-hc-hijos'),
            residencia: btn.getAttribute('data-hc-residencia')
        };
    }

    function resetAnamnesisEdicion() {
        anamnesisEditando = false;
        var btn = document.getElementById('btn-toggle-anamnesis');
        if (btn) {
            btn.textContent = '✎ Agregar anamnesis';
            btn.classList.remove('btn-primary');
            btn.classList.add('btn-outline');
        }
        document.querySelectorAll('.hc-editable').forEach(function (ta) {
            ta.disabled = true;
        });
    }

    function activarTabHc(tabId) {
        document.querySelectorAll('.hc-tab').forEach(function (t) {
            t.classList.toggle('active', t.getAttribute('data-hc-tab') === tabId);
        });
        document.querySelectorAll('.hc-tab-panel').forEach(function (p) {
            p.classList.toggle('active', p.getAttribute('data-hc-panel') === tabId);
        });
    }

    function renderDiagnosticos() {
        var list = document.getElementById('hc-diagnosticos-list');
        if (!list) return;
        list.innerHTML = '';
        hcData.diagnosticos.forEach(function (d, i) {
            var row = document.createElement('div');
            row.className = 'hc-list-item';
            row.innerHTML =
                '<span class="badge badge-mono">' + d.codigo + '</span>' +
                '<span class="flex-1">' + d.nombre + '</span>' +
                '<button type="button" class="btn btn-outline btn-sm btn-edit-diag" data-index="' + i + '">✎ Editar</button>';
            list.appendChild(row);
        });
        list.querySelectorAll('.btn-edit-diag').forEach(function (btn) {
            btn.addEventListener('click', function () {
                abrirModalDiag(parseInt(btn.getAttribute('data-index'), 10));
            });
        });
    }

    function renderVisitas() {
        var tbody = document.getElementById('hc-visitas-tbody');
        if (!tbody) return;
        tbody.innerHTML = '';
        hcData.visitas.forEach(function (v, i) {
            var tr = document.createElement('tr');
            var descCorta = v.descripcion.length > 60 ? v.descripcion.slice(0, 60) + '…' : v.descripcion;
            var verMas = v.descripcion.length > 60
                ? '<button type="button" class="btn-link-sm btn-ver-desc-visita" data-index="' + i + '">Ver más</button>'
                : '';
            tr.innerHTML =
                '<td>' + v.fecha + '</td>' +
                '<td>' + v.hora + '</td>' +
                '<td>Sesión ' + v.sesion + '</td>' +
                '<td class="text-muted">' + descCorta + verMas + '</td>' +
                '<td><span class="badge' + (v.estado === 'Completada' ? ' badge-muted' : '') + '">' + v.estado + '</span></td>' +
                '<td><button type="button" class="btn btn-outline btn-sm btn-edit-visita" data-index="' + i + '">✎ Editar</button></td>';
            tbody.appendChild(tr);
        });
        tbody.querySelectorAll('.btn-edit-visita').forEach(function (btn) {
            btn.addEventListener('click', function () {
                abrirModalVisita(parseInt(btn.getAttribute('data-index'), 10));
            });
        });
        tbody.querySelectorAll('.btn-ver-desc-visita').forEach(function (btn) {
            btn.addEventListener('click', function () {
                var v = hcData.visitas[parseInt(btn.getAttribute('data-index'), 10)];
                document.getElementById('modal-visita-desc-titulo').textContent =
                    'Sesión ' + v.sesion + ' · ' + v.fecha;
                document.getElementById('modal-visita-desc-texto').textContent = v.descripcion;
                openModal('modal-visita-desc');
            });
        });
    }

    function renderMedicacion() {
        var list = document.getElementById('hc-medicacion-list');
        if (!list) return;
        list.innerHTML = '';
        hcData.medicacion.forEach(function (m, i) {
            var row = document.createElement('div');
            row.className = 'hc-list-item stacked';
            row.innerHTML =
                '<div class="flex-1">' +
                '<p><strong>' + m.nombre + '</strong></p>' +
                '<p class="text-muted">Dosis: ' + m.dosis + ' · Desde: ' + m.desde + ' · Hasta: ' + m.hasta + '</p>' +
                '</div>' +
                '<button type="button" class="btn btn-outline btn-sm btn-edit-med" data-index="' + i + '">✎ Editar</button>';
            list.appendChild(row);
        });
        list.querySelectorAll('.btn-edit-med').forEach(function (btn) {
            btn.addEventListener('click', function () {
                abrirModalMedicacion(parseInt(btn.getAttribute('data-index'), 10));
            });
        });
    }

    function renderevaluaciones() {
        var grid = document.getElementById('hc-evaluaciones-grid');
        if (!grid) return;
        grid.innerHTML = '';
        hcData.evaluaciones.forEach(function (e, i) {
            var card = document.createElement('div');
            card.className = 'card panel-card';
            card.innerHTML =
                '<div style="display:flex;justify-content:space-between;gap:0.5rem;align-items:flex-start;">' +
                '<p style="margin:0;flex:1;"><strong>' + e.nombre + '</strong></p>' +
                '<button type="button" class="btn btn-outline btn-sm btn-edit-evaluacion" data-index="' + i + '">✎ Editar</button>' +
                '</div>' +
                '<p class="text-muted">' + e.interpretacion + '</p>' +
                '<p>Puntaje: <strong>' + e.puntaje + '</strong></p>';
            grid.appendChild(card);
        });
        grid.querySelectorAll('.btn-edit-evaluacion').forEach(function (btn) {
            btn.addEventListener('click', function () {
                abrirModalevaluacion(parseInt(btn.getAttribute('data-index'), 10));
            });
        });
    }

    function renderHcSecciones() {
        renderDiagnosticos();
        renderVisitas();
        renderMedicacion();
        renderevaluaciones();
    }

    function mostrarHistoria(h) {
        if (!h) return;
        historiaActual = h;
        resetAnamnesisEdicion();
        activarTabHc('anamnesis');

        document.getElementById('hc-paciente').textContent = h.paciente;
        document.getElementById('hc-documento').textContent = h.documento;
        document.getElementById('hc-codigo').textContent = h.codigo;
        document.getElementById('hc-fecha-nac').textContent =
            h.fechaNacimiento + ' (' + h.edad + ' a.)';
        document.getElementById('hc-sexo').textContent = h.sexo;
        document.getElementById('hc-grado').textContent = h.gradoInstruccion;
        document.getElementById('hc-ocupacion').textContent = h.ocupacion;
        document.getElementById('hc-ultima').textContent = h.ultimaCita;
        document.getElementById('hc-hijos').textContent = h.numeroHijos;
        document.getElementById('hc-residencia').textContent = h.residencia;

        fetch('/especialista/historias/api/detalle/' + h.codigo)
            .then(function(res) { return res.json(); })
            .then(function(data) {
                if (data) {
                    document.getElementById('hc-motivo').value = data.motivoConsulta || '';
                    document.getElementById('hc-ninez').value = data.histNinez || '';
                    document.getElementById('hc-adolescencia').value = data.histAdolescencia || '';
                    document.getElementById('hc-adultez').value = data.histAdultez || '';
                    document.getElementById('hc-sustancias').value = data.usoSustancias || '';
                    document.getElementById('hc-madre').value = data.histFamiliarMadre || '';
                    document.getElementById('hc-padre').value = data.histFamiliarPadre || '';
                    document.getElementById('hc-hermanos').value = data.histFamiliarHermanos || '';
                    document.getElementById('hc-pareja').value = data.histFamiliarPareja || '';
                    document.getElementById('hc-hijos-fam').value = data.histFamiliarHijos || '';

                    hcData.diagnosticos = data.diagnosticos ? data.diagnosticos.map(function(d) { return { codigo: d.codCie, nombre: d.descripcion }; }) : [];
                    hcData.visitas = data.visitas ? data.visitas.map(function(v) { return { fecha: v.fecha, hora: v.hora, sesion: 1, descripcion: v.descripcion, estado: 'Completada' }; }) : [];
                    hcData.medicacion = data.psicofarmacos ? data.psicofarmacos.map(function(p) { return { nombre: p.nombre, dosis: p.dosis, desde: p.fechaInicio, hasta: p.fechaFin || 'Actualidad' }; }) : [];
                    hcData.evaluaciones = data.evaluaciones ? data.evaluaciones.map(function(e) { return { nombre: e.nombreevaluacion, interpretacion: e.nivel, puntaje: e.puntaje + (e.puntajeMax ? '/' + e.puntajeMax : '') }; }) : [];
                }
                renderHcSecciones();
                openModal('modal-historia');
            })
            .catch(function(err) {
                console.error(err);
                renderHcSecciones();
                openModal('modal-historia');
            });
    }

    document.querySelectorAll('.btn-ver-historia').forEach(function (btn) {
        btn.addEventListener('click', function () {
            mostrarHistoria(leerHistoriaDesdeBtn(btn));
        });
    });

    document.querySelectorAll('.hc-tab').forEach(function (tab) {
        tab.addEventListener('click', function () {
            activarTabHc(tab.getAttribute('data-hc-tab'));
        });
    });

    var btnToggleAnamnesis = document.getElementById('btn-toggle-anamnesis');
    if (btnToggleAnamnesis) {
        btnToggleAnamnesis.addEventListener('click', function () {
            anamnesisEditando = !anamnesisEditando;
            document.querySelectorAll('.hc-editable').forEach(function (ta) {
                ta.disabled = !anamnesisEditando;
            });
            if (anamnesisEditando) {
                btnToggleAnamnesis.textContent = '✓ Guardar anamnesis';
                btnToggleAnamnesis.classList.remove('btn-outline');
                btnToggleAnamnesis.classList.add('btn-primary');
            } else {
                btnToggleAnamnesis.textContent = '✎ Agregar anamnesis';
                btnToggleAnamnesis.classList.remove('btn-primary');
                btnToggleAnamnesis.classList.add('btn-outline');
            }
        });
    }

    document.querySelectorAll('[data-modal-close-nested]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var modal = btn.closest('.modal-overlay');
            if (modal) modal.classList.remove('open');
        });
    });

    document.querySelectorAll('.modal-nested').forEach(function (overlay) {
        overlay.addEventListener('click', function (e) {
            if (e.target === overlay) overlay.classList.remove('open');
        });
    });

    function abrirModalDiag(index) {
        diagEditIndex = index != null ? index : null;
        var d = index != null ? hcData.diagnosticos[index] : null;
        document.getElementById('modal-diag-titulo').textContent =
            d ? 'Editar diagnóstico' : 'Nuevo diagnóstico';
        document.getElementById('diag-codigo').value = d ? d.codigo : '';
        document.getElementById('diag-nombre').value = d ? d.nombre : '';
        document.getElementById('diag-confirmar').checked = false;
        validarDiagForm();
        openModal('modal-diag');
    }

    function validarDiagForm() {
        var codigo = document.getElementById('diag-codigo').value.trim().toUpperCase();
        var nombre = CIE10[codigo] || '';
        var confirmar = document.getElementById('diag-confirmar').checked;
        document.getElementById('diag-nombre').value = nombre;
        document.getElementById('btn-guardar-diag').disabled = !(nombre && confirmar);
    }

    document.getElementById('diag-codigo') && document.getElementById('diag-codigo').addEventListener('input', validarDiagForm);
    document.getElementById('diag-confirmar') && document.getElementById('diag-confirmar').addEventListener('change', validarDiagForm);

    document.getElementById('btn-add-diagnostico') && document.getElementById('btn-add-diagnostico').addEventListener('click', function () {
        abrirModalDiag(null);
    });

    document.getElementById('btn-guardar-diag') && document.getElementById('btn-guardar-diag').addEventListener('click', function () {
        var item = {
            codigo: document.getElementById('diag-codigo').value.trim().toUpperCase(),
            nombre: document.getElementById('diag-nombre').value
        };
        if (diagEditIndex !== null) {
            hcData.diagnosticos[diagEditIndex] = item;
        } else {
            hcData.diagnosticos.unshift(item);
        }
        document.getElementById('modal-diag').classList.remove('open');
        renderDiagnosticos();
    });

    function abrirModalVisita(index) {
        visitaEditIndex = index != null ? index : null;
        var v = index != null ? hcData.visitas[index] : null;
        document.getElementById('modal-visita-titulo').textContent = v ? 'Editar visita' : 'Nueva visita';
        document.getElementById('visita-fecha').value = v ? v.fecha : '';
        document.getElementById('visita-hora').value = v ? v.hora : '';
        document.getElementById('visita-sesion').value = v ? v.sesion : 1;
        document.getElementById('visita-descripcion').value = v ? v.descripcion : '';
        document.getElementById('visita-estado').value = v ? v.estado : 'Pendiente';
        openModal('modal-visita');
    }

    document.getElementById('btn-add-visita') && document.getElementById('btn-add-visita').addEventListener('click', function () {
        abrirModalVisita(null);
    });

    document.getElementById('btn-guardar-visita') && document.getElementById('btn-guardar-visita').addEventListener('click', function () {
        var item = {
            fecha: document.getElementById('visita-fecha').value,
            hora: document.getElementById('visita-hora').value,
            sesion: parseInt(document.getElementById('visita-sesion').value, 10) || 1,
            descripcion: document.getElementById('visita-descripcion').value.trim(),
            estado: document.getElementById('visita-estado').value
        };
        if (!item.fecha || !item.hora || !item.descripcion) return;
        if (visitaEditIndex !== null) {
            hcData.visitas[visitaEditIndex] = item;
        } else {
            hcData.visitas.unshift(item);
        }
        document.getElementById('modal-visita').classList.remove('open');
        renderVisitas();
    });

    function abrirModalMedicacion(index) {
        medEditIndex = index != null ? index : null;
        var m = index != null ? hcData.medicacion[index] : null;
        document.getElementById('modal-med-titulo').textContent = m ? 'Editar psicofármaco' : 'Nuevo psicofármaco';
        document.getElementById('med-nombre').value = m ? m.nombre : '';
        document.getElementById('med-dosis').value = m ? m.dosis : '';
        document.getElementById('med-desde').value = m ? m.desde : '';
        var esActualidad = m && m.hasta === 'Actualidad';
        document.getElementById('med-actualidad').checked = esActualidad;
        document.getElementById('med-hasta').value = esActualidad ? '' : (m ? m.hasta : '');
        document.getElementById('med-hasta').disabled = esActualidad;
        openModal('modal-medicacion');
    }

    document.getElementById('med-actualidad') && document.getElementById('med-actualidad').addEventListener('change', function () {
        document.getElementById('med-hasta').disabled = this.checked;
        if (this.checked) document.getElementById('med-hasta').value = '';
    });

    document.getElementById('btn-add-medicacion') && document.getElementById('btn-add-medicacion').addEventListener('click', function () {
        abrirModalMedicacion(null);
    });

    document.getElementById('btn-guardar-medicacion') && document.getElementById('btn-guardar-medicacion').addEventListener('click', function () {
        var item = {
            nombre: document.getElementById('med-nombre').value.trim(),
            dosis: document.getElementById('med-dosis').value.trim(),
            desde: document.getElementById('med-desde').value,
            hasta: document.getElementById('med-actualidad').checked
                ? 'Actualidad'
                : document.getElementById('med-hasta').value
        };
        if (!item.nombre || !item.dosis || !item.desde) return;
        if (medEditIndex !== null) {
            hcData.medicacion[medEditIndex] = item;
        } else {
            hcData.medicacion.unshift(item);
        }
        document.getElementById('modal-medicacion').classList.remove('open');
        renderMedicacion();
    });

    function abrirModalevaluacion(index) {
        evaluacionEditIndex = index != null ? index : null;
        var e = index != null ? hcData.evaluaciones[index] : null;
        document.getElementById('modal-evaluacion-titulo').textContent = e ? 'Editar evaluacion' : 'Nueva evaluacion';
        document.getElementById('evaluacion-nombre').value = e ? e.nombre : '';
        document.getElementById('evaluacion-interp').value = e ? e.interpretacion : '';
        document.getElementById('evaluacion-puntaje').value = e ? e.puntaje : '';
        openModal('modal-evaluacion');
    }

    document.getElementById('btn-add-evaluacion') && document.getElementById('btn-add-evaluacion').addEventListener('click', function () {
        abrirModalevaluacion(null);
    });

    document.getElementById('btn-guardar-evaluacion') && document.getElementById('btn-guardar-evaluacion').addEventListener('click', function () {
        var item = {
            nombre: document.getElementById('evaluacion-nombre').value.trim(),
            interpretacion: document.getElementById('evaluacion-interp').value.trim(),
            puntaje: document.getElementById('evaluacion-puntaje').value.trim()
        };
        if (!item.nombre || !item.interpretacion || !item.puntaje) return;
        if (evaluacionEditIndex !== null) {
            hcData.evaluaciones[evaluacionEditIndex] = item;
        } else {
            hcData.evaluaciones.unshift(item);
        }
        document.getElementById('modal-evaluacion').classList.remove('open');
        renderevaluaciones();
    });

    if (document.getElementById('hc-diagnosticos-list')) {
        renderHcSecciones();
    }

    /* Citas — columnas por día (Lun–Vie) */
    var citasSemanaEl = document.getElementById('citas-semana');
    var DIAS_SEMANA = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes'];

    if (citasSemanaEl) {
        var todasLasCitas = Array.isArray(window.__CITAS_DATA__) ? window.__CITAS_DATA__ : [];

        var semanaOffset = 0;
        var semanaLabel = document.getElementById('semana-label');
        var btnPrev = document.getElementById('semana-prev');
        var btnNext = document.getElementById('semana-next');
        var citaSeleccionada = null;

        function escHtml(s) {
            if (!s) return '';
            return String(s)
                .replace(/&/g, '&amp;')
                .replace(/</g, '&lt;')
                .replace(/"/g, '&quot;');
        }

        function attrsHistoria(c) {
            var h = c.historia || {};
            return ' data-hc-codigo="' + escHtml(h.codigo) + '"' +
                ' data-hc-paciente="' + escHtml(h.paciente) + '"' +
                ' data-hc-documento="' + escHtml(h.documento) + '"' +
                ' data-hc-fecha-nac="' + escHtml(h.fechaNacimiento) + '"' +
                ' data-hc-edad="' + escHtml(h.edad) + '"' +
                ' data-hc-sexo="' + escHtml(h.sexo) + '"' +
                ' data-hc-grado="' + escHtml(h.gradoInstruccion) + '"' +
                ' data-hc-ocupacion="' + escHtml(h.ocupacion) + '"' +
                ' data-hc-ultima="' + escHtml(h.ultimaCita) + '"' +
                ' data-hc-hijos="' + escHtml(h.numeroHijos) + '"' +
                ' data-hc-residencia="' + escHtml(h.residencia) + '"';
        }

        function renderCitasSemana() {
            /* Todas las citas mock son de la semana base (offset 0) */
            var filtradas = semanaOffset === 0 ? todasLasCitas : [];

            var html = '';
            for (var d = 0; d < 5; d++) {
                var delDia = filtradas.filter(function (c) { return c.dia === d; });
                html += '<div class="citas-dia">';
                html += '<div class="citas-dia-header"><p class="citas-dia-nombre">' + DIAS_SEMANA[d] + '</p></div>';
                html += '<div class="citas-dia-body" data-dia="' + d + '">';
                if (delDia.length === 0) {
                    html += '<p class="citas-dia-vacio text-muted">Sin citas</p>';
                } else {
                    delDia.forEach(function (c) {
                        html += '<button type="button" class="cita-card btn-cita-detalle"' +
                            ' data-fecha="' + escHtml(c.fecha) + '"' +
                            ' data-hora="' + escHtml(c.hora) + '"' +
                            ' data-paciente="' + escHtml(c.paciente) + '"' +
                            ' data-servicio="' + escHtml(c.servicio) + '"' +
                            ' data-dni="' + escHtml(c.dni) + '"' +
                            attrsHistoria(c) + '>' +
                            '<p class="cita-hora">🕐 <span>' + escHtml(c.hora) + '</span></p>' +
                            '<p class="cita-paciente">👤 <span>' + escHtml(c.paciente) + '</span></p>' +
                            '<p class="cita-servicio text-muted">' + escHtml(c.servicio) + '</p>' +
                            '</button>';
                    });
                }
                html += '</div></div>';
            }
            citasSemanaEl.innerHTML = html;
        }

        function actualizarSemana() {
            if (semanaLabel) {
                var inicio = 12 + semanaOffset * 7;
                var fin = 16 + semanaOffset * 7;
                semanaLabel.textContent = 'Semana del ' + inicio + ' – ' + fin + ' de mayo, 2026';
            }
            renderCitasSemana();
        }

        if (btnPrev) {
            btnPrev.addEventListener('click', function () {
                semanaOffset--;
                actualizarSemana();
            });
        }
        if (btnNext) {
            btnNext.addEventListener('click', function () {
                semanaOffset++;
                actualizarSemana();
            });
        }

        citasSemanaEl.addEventListener('click', function (e) {
            var btn = e.target.closest('.btn-cita-detalle');
            if (!btn) return;
            citaSeleccionada = {
                fecha: btn.getAttribute('data-fecha'),
                hora: btn.getAttribute('data-hora'),
                paciente: btn.getAttribute('data-paciente'),
                servicio: btn.getAttribute('data-servicio'),
                dni: btn.getAttribute('data-dni'),
                historia: leerHistoriaDesdeBtn(btn)
            };
            var detFecha = document.getElementById('cita-det-fecha');
            var detHora = document.getElementById('cita-det-hora');
            var detServ = document.getElementById('cita-det-servicio');
            var detPac = document.getElementById('cita-det-paciente');
            var detDni = document.getElementById('cita-det-dni');
            var detHc = document.getElementById('cita-det-hc');
            if (detFecha) detFecha.textContent = citaSeleccionada.fecha;
            if (detHora) detHora.textContent = citaSeleccionada.hora;
            if (detServ) detServ.textContent = citaSeleccionada.servicio;
            if (detPac) detPac.textContent = citaSeleccionada.paciente;
            if (detDni) detDni.textContent = citaSeleccionada.dni;
            if (detHc) detHc.textContent = citaSeleccionada.historia.codigo;
            openModal('modal-cita');
        });

        var btnVerHistoriaCita = document.getElementById('btn-ver-historia');
        if (btnVerHistoriaCita) {
            btnVerHistoriaCita.addEventListener('click', function () {
                if (citaSeleccionada) {
                    var modalCita = document.getElementById('modal-cita');
                    if (modalCita) modalCita.classList.remove('open');
                    mostrarHistoria(citaSeleccionada.historia);
                }
            });
        }

        actualizarSemana();
    }

    /* Horarios */
    var slotsPosibles = [
        '10:00', '11:00', '12:00', '13:00', '14:00', '15:00',
        '16:00', '17:00', '18:00', '19:00', '20:00', '21:00'
    ];

    function actualizarContador(dia) {
        var wrap = document.querySelector('.slots-wrap[data-dia="' + dia + '"]');
        if (!wrap) return;
        var activos = wrap.querySelectorAll('.slot-btn.active').length;
        var label = document.querySelector('.slot-count[data-dia="' + dia + '"]');
        if (label) {
            label.textContent = activos + ' slot' + (activos === 1 ? '' : 's');
        }
    }

    document.querySelectorAll('.slot-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            btn.classList.toggle('active');
            var icon = btn.querySelector('.slot-icon');
            if (icon) icon.textContent = btn.classList.contains('active') ? '×' : '+';
            actualizarContador(btn.getAttribute('data-dia'));
        });
    });

    document.querySelectorAll('.btn-toggle-todos').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var dia = btn.getAttribute('data-dia');
            var wrap = document.querySelector('.slots-wrap[data-dia="' + dia + '"]');
            if (!wrap) return;
            var todos = wrap.querySelectorAll('.slot-btn.active').length === slotsPosibles.length;
            wrap.querySelectorAll('.slot-btn').forEach(function (slot) {
                slot.classList.toggle('active', !todos);
                var icon = slot.querySelector('.slot-icon');
                if (icon) icon.textContent = slot.classList.contains('active') ? '×' : '+';
            });
            btn.textContent = todos ? 'Marcar todos' : 'Quitar todos';
            actualizarContador(dia);
        });
    });

    var btnGuardarHorarios = document.getElementById('btn-guardar-horarios');
    if (btnGuardarHorarios) {
        btnGuardarHorarios.addEventListener('click', function () {
            var msg = document.getElementById('horarios-mensaje');
            if (msg) {
                msg.style.display = 'block';
                msg.textContent = 'Horarios guardados correctamente.';
            }
        });
    }

    /* Filtros tablas */
    function filtrarTabla(tablaId, emptyId, fnMatch) {
        var tabla = document.getElementById(tablaId);
        var empty = document.getElementById(emptyId);
        if (!tabla) return;
        var rows = tabla.querySelectorAll('tbody .data-row');
        var visibles = 0;
        rows.forEach(function (row) {
            var show = fnMatch(row);
            row.style.display = show ? '' : 'none';
            if (show) visibles++;
        });
        if (empty) empty.style.display = visibles === 0 ? 'block' : 'none';
    }

    var filtroEmp = document.getElementById('filtro-empleado');
    var filtroTipoEmp = document.getElementById('filtro-tipo-emp');
    var filtroVisEmp = document.getElementById('filtro-vis-emp');

    function aplicarFiltroEmpleados() {
        var q = (filtroEmp && filtroEmp.value || '').toLowerCase();
        var tipo = filtroTipoEmp ? filtroTipoEmp.value : 'all';
        var vis = filtroVisEmp ? filtroVisEmp.value : 'all';
        filtrarTabla('tabla-empleados', 'empleados-empty', function (row) {
            var nombre = (row.getAttribute('data-nombre') || '').toLowerCase();
            var email = (row.getAttribute('data-email') || '').toLowerCase();
            var tel = (row.getAttribute('data-tel') || row.getAttribute('data-telefono') || '').toLowerCase();
            var tServ = row.getAttribute('data-tipo') || '';
            var activo = row.getAttribute('data-activo') === 'true';
            if (tipo !== 'all' && tServ !== tipo) return false;
            if (vis === 'activos' && !activo) return false;
            if (vis === 'inactivos' && activo) return false;
            if (q && nombre.indexOf(q) === -1 && email.indexOf(q) === -1 && tel.indexOf(q) === -1) return false;
            return true;
        });
    }

    if (filtroEmp) {
        [filtroEmp, filtroTipoEmp, filtroVisEmp].forEach(function (el) {
            if (el) el.addEventListener('input', aplicarFiltroEmpleados);
            if (el) el.addEventListener('change', aplicarFiltroEmpleados);
        });
    }

    var filtroSrv = document.getElementById('filtro-servicio');
    var filtroTipoSrv = document.getElementById('filtro-tipo-srv');
    var filtroEstadoSrv = document.getElementById('filtro-estado-srv');

    function aplicarFiltroServicios() {
        var q = (filtroSrv && filtroSrv.value || '').toLowerCase();
        var tipo = filtroTipoSrv ? filtroTipoSrv.value : 'all';
        var estado = filtroEstadoSrv ? filtroEstadoSrv.value : 'all';
        filtrarTabla('tabla-servicios', 'servicios-empty', function (row) {
            var nombre = (row.getAttribute('data-nombre') || '').toLowerCase();
            var t = row.getAttribute('data-tipo') || '';
            var e = row.getAttribute('data-estado') || '';
            if (tipo !== 'all' && t !== tipo) return false;
            if (estado !== 'all' && e !== estado) return false;
            if (q && nombre.indexOf(q) === -1) return false;
            return true;
        });
    }

    if (filtroSrv) {
        [filtroSrv, filtroTipoSrv, filtroEstadoSrv].forEach(function (el) {
            if (el) el.addEventListener('input', aplicarFiltroServicios);
            if (el) el.addEventListener('change', aplicarFiltroServicios);
        });
    }

    var filtroPkg = document.getElementById('filtro-paquete');
    var filtroVisPkg = document.getElementById('filtro-vis-pkg');

    function aplicarFiltroPaquetes() {
        var q = (filtroPkg && filtroPkg.value || '').toLowerCase();
        var vis = filtroVisPkg ? filtroVisPkg.value : 'all';
        filtrarTabla('tabla-paquetes', 'paquetes-empty', function (row) {
            var nombre = (row.getAttribute('data-nombre') || '').toLowerCase();
            var activo = row.getAttribute('data-activo') === 'true';
            if (vis === 'activos' && !activo) return false;
            if (vis === 'inactivos' && activo) return false;
            if (q && nombre.indexOf(q) === -1) return false;
            return true;
        });
    }

    if (filtroPkg) {
        [filtroPkg, filtroVisPkg].forEach(function (el) {
            if (el) el.addEventListener('input', aplicarFiltroPaquetes);
            if (el) el.addEventListener('change', aplicarFiltroPaquetes);
        });
    }

    /* Modal empleado */
    var modalEmp = document.getElementById('modal-empleado');
    var formEmp = document.getElementById('form-empleado');

    function abrirEmpleado(data) {
        document.getElementById('modal-empleado-titulo').textContent =
            data.id ? 'Editar empleado' : 'Nuevo empleado';
        document.getElementById('emp-id').value = data.id || '';
        document.getElementById('emp-id-display').value = data.id || '(nuevo)';
        var activo = document.getElementById('emp-activo');
        activo.checked = data.activo !== false;
        document.getElementById('emp-activo-hidden').value = activo.checked ? 'true' : 'false';
        document.getElementById('emp-activo-label').textContent =
            activo.checked ? 'El empleado está activo' : 'Inactivo';
        document.getElementById('emp-nombre').value = data.nombre || '';
        document.getElementById('emp-email').value = data.email || '';
        document.getElementById('emp-telefono').value = data.telefono || '';
        document.getElementById('emp-rol').value = data.rol || 'Especialista';
        document.getElementById('emp-tipo').value = data.tipo || '—';
        openModal('modal-empleado');
    }

    if (document.getElementById('emp-activo')) {
        document.getElementById('emp-activo').addEventListener('change', function () {
            var checked = this.checked;
            document.getElementById('emp-activo-hidden').value = checked ? 'true' : 'false';
            document.getElementById('emp-activo-label').textContent =
                checked ? 'El empleado está activo' : 'Inactivo';
        });
    }

    document.querySelectorAll('.btn-editar-empleado').forEach(function (btn) {
        btn.addEventListener('click', function () {
            abrirEmpleado({
                id: btn.getAttribute('data-id'),
                activo: btn.getAttribute('data-activo') === 'true',
                nombre: btn.getAttribute('data-nombre'),
                email: btn.getAttribute('data-email'),
                telefono: btn.getAttribute('data-telefono'),
                rol: btn.getAttribute('data-rol'),
                tipo: btn.getAttribute('data-tipo')
            });
        });
    });

    var btnNuevoEmp = document.getElementById('btn-nuevo-empleado');
    if (btnNuevoEmp) {
        btnNuevoEmp.addEventListener('click', function () {
            abrirEmpleado({ activo: true, rol: 'Especialista', tipo: 'Especialista en duelo neonatal' });
        });
    }

    /* Modal paquete */
    function abrirPaquete(data) {
        document.getElementById('modal-paquete-titulo').textContent =
            data.id ? 'Editar paquete' : 'Nuevo paquete';
        document.getElementById('pkg-id').value = data.id || '';
        document.getElementById('pkg-id-display').value = data.id || '(nuevo)';
        var activo = document.getElementById('pkg-activo');
        activo.checked = data.activo !== false;
        document.getElementById('pkg-activo-hidden').value = activo.checked ? 'true' : 'false';
        document.getElementById('pkg-nombre').value = data.nombre || '';
        document.getElementById('pkg-descripcion').value = data.descripcion || '';
        document.getElementById('pkg-precio').value = data.precio || 0;
        openModal('modal-paquete');
    }

    if (document.getElementById('pkg-activo')) {
        document.getElementById('pkg-activo').addEventListener('change', function () {
            document.getElementById('pkg-activo-hidden').value = this.checked ? 'true' : 'false';
        });
    }

    document.querySelectorAll('.btn-editar-paquete').forEach(function (btn) {
        btn.addEventListener('click', function () {
            abrirPaquete({
                id: btn.getAttribute('data-id'),
                activo: btn.getAttribute('data-activo') === 'true',
                nombre: btn.getAttribute('data-nombre'),
                descripcion: btn.getAttribute('data-descripcion'),
                precio: btn.getAttribute('data-precio')
            });
        });
    });

    var btnNuevoPkg = document.getElementById('btn-nuevo-paquete');
    if (btnNuevoPkg) {
        btnNuevoPkg.addEventListener('click', function () {
            abrirPaquete({ activo: true, precio: 0 });
        });
    }
})();

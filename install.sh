#!/usr/bin/env bash
# ttcuz plugin builder: one-command install for Termux (and Linux).
#
#   curl -fsSL https://raw.githubusercontent.com/tremorttcuz/ttcuz-plugins/main/install.sh | bash
#
# Installs Python and JDK 17 if they are missing, puts the builder in ~/.ttcuz-plugins
# and adds the command `ttcuz-plugin`. Run it again to update.
set -e

REPO="https://github.com/tremorttcuz/ttcuz-plugins/archive/refs/heads/main.zip"
HOME_DIR="$HOME/.ttcuz-plugins"
say() { printf '\033[1;36m==>\033[0m %s\n' "$1"; }

if [ -n "$PREFIX" ] && [ -d "/data/data/com.termux" ]; then
    TERMUX=1
    BIN="$PREFIX/bin"
else
    TERMUX=0
    BIN="$HOME/.local/bin"
    mkdir -p "$BIN"
fi

if [ "$TERMUX" = 1 ]; then
    say "Пакеты Termux: python, openjdk-17, unzip, curl"
    pkg install -y python openjdk-17 unzip curl
    if [ ! -d "$HOME/storage/downloads" ]; then
        say "Доступ к «Загрузкам»: нажмите «Разрешить»"
        termux-setup-storage || true
        sleep 3
    fi
else
    for tool in python3 javac unzip curl; do
        command -v "$tool" >/dev/null 2>&1 || { echo "Нужен $tool. Установите его и запустите снова."; exit 1; }
    done
fi

say "Скачиваю сборщик"
TMP="$(mktemp -d)"
curl -fsSL "$REPO" -o "$TMP/kit.zip"
unzip -q "$TMP/kit.zip" -d "$TMP"
# keep the downloaded tools (android.jar, r8.jar) across updates
if [ -d "$HOME_DIR/tools" ]; then mv "$HOME_DIR/tools" "$TMP/tools-kept"; fi
rm -rf "$HOME_DIR"
mv "$TMP"/ttcuz-plugins-main "$HOME_DIR"
if [ -d "$TMP/tools-kept" ]; then mv "$TMP/tools-kept" "$HOME_DIR/tools"; fi
rm -rf "$TMP"

PY=python3; command -v python3 >/dev/null 2>&1 || PY=python
cat > "$BIN/ttcuz-plugin" <<EOF
#!/usr/bin/env bash
# ttcuz-plugin <папка> [-o файл.ctt]   собрать плагин
# ttcuz-plugin new <имя>              новый плагин из примера
# ttcuz-plugin update                 обновить сборщик
KIT="$HOME_DIR"
case "\$1" in
  update) exec bash -c "curl -fsSL https://raw.githubusercontent.com/tremorttcuz/ttcuz-plugins/main/install.sh | bash" ;;
  new)
    [ -n "\$2" ] || { echo "Укажите имя: ttcuz-plugin new my-plugin"; exit 1; }
    BASE="\$HOME/storage/downloads"; [ -d "\$BASE" ] || BASE="\$PWD"
    TARGET="\$BASE/\$2"
    [ -e "\$TARGET" ] && { echo "Уже есть: \$TARGET"; exit 1; }
    cp -r "\$KIT/examples/hello" "\$TARGET"
    rm -f "\$TARGET"/*.ctt
    ID="my.\$(echo "\$2" | tr -c 'A-Za-z0-9._\n-' '_')"
    sed -i "s/\"id\": \"ttcuz.hello\"/\"id\": \"\$ID\"/; s/\"name\": \"Привет\"/\"name\": \"\$2\"/" "\$TARGET/manifest.json"
    echo "Создан плагин: \$TARGET"
    echo "Собрать: ttcuz-plugin \"\$TARGET\""
    exit 0 ;;
  ""|-h|--help)
    sed -n '2,4p' "\$0" | sed 's/^# //'; exit 0 ;;
esac
FOLDER="\$1"; shift
OUT=()
# by default the .ctt goes to Downloads (Termux), or next to the plugin folder
if [[ " \$* " != *" -o "* && " \$* " != *" --out "* ]]; then
  NAME="\$(basename "\$FOLDER")"
  if [ -d "\$HOME/storage/downloads" ]; then DEST="\$HOME/storage/downloads"; else DEST="\$(cd "\$(dirname "\$FOLDER")" && pwd)"; fi
  OUT=(-o "\$DEST/\$NAME.ctt")
fi
PYTHONPATH="\$KIT" exec $PY -m ttcuz.plugin "\$FOLDER" "\${OUT[@]}" "\$@"
EOF
chmod +x "$BIN/ttcuz-plugin"

say "Готово"
echo "Новый плагин в «Загрузках»:   ttcuz-plugin new my-plugin"
echo "Собрать его:                  ttcuz-plugin ~/storage/downloads/my-plugin"
echo "Пример:                       ttcuz-plugin $HOME_DIR/examples/hello"
[ "$TERMUX" = 1 ] || case ":$PATH:" in *":$BIN:"*) ;; *) echo "Добавьте $BIN в PATH." ;; esac

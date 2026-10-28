# Сборка плагина на компьютере

Windows, Linux или macOS. Аккаунт GitHub не нужен.

## 1. Установите JDK 17 и Python 3

| Система | Как |
|---|---|
| Windows | JDK: [adoptium.net](https://adoptium.net), Temurin 17; при установке отметьте **Set JAVA_HOME** и **Add to PATH**. Python: [python.org](https://www.python.org/downloads/); отметьте **Add python.exe to PATH**. |
| Ubuntu / Debian | `sudo apt install openjdk-17-jdk python3 unzip` |
| Arch | `sudo pacman -S jdk17-openjdk python unzip` |
| macOS | `brew install openjdk@17 python` |

Проверка (новое окно терминала):

```sh
python --version
javac -version
```

В Linux и macOS команда называется `python3`.

## 2. Скачайте и распакуйте

На странице репозитория нажмите **Code → Download ZIP** (файл `ttcuz-plugins-main.zip`). Архив окажется в «Загрузках». Распакуйте его двойным щелчком или командой.

**Windows (PowerShell):**

```powershell
cd $HOME\Downloads
Expand-Archive .\ttcuz-plugins-main.zip -DestinationPath . -Force
cd .\ttcuz-plugins-main
```

**Linux / macOS:**

```sh
cd ~/Downloads && unzip -o ttcuz-plugins-main.zip && cd ttcuz-plugins-main
```

## 3. Соберите пример

Windows:

```powershell
python -m ttcuz.plugin examples\hello -o hello.ctt
```

Linux / macOS:

```sh
python3 -m ttcuz.plugin examples/hello -o hello.ctt
```

Первая сборка скачивает в `tools/` `android.jar` и `r8.jar`, около 80 МБ, дальше всё работает без интернета. В конце появится строка `…hello.ctt, NNNN bytes`.

Отправьте `hello.ctt` на телефон и установите: **Настройки ttcuz → Плагины → Установить .ctt**.

## Свой плагин

Скопируйте `examples/hello` в новую папку, поменяйте `manifest.json` и код в `java/`. Что писать — в [plugins.md](plugins.md). Сборка:

```sh
python3 -m ttcuz.plugin путь/к/my-plugin -o my-plugin.ctt
```

Ошибки и их решения — в таблице в [termux.md](termux.md): они те же.

**Linux одной командой** (как в Termux): `curl -fsSL https://raw.githubusercontent.com/tremorttcuz/ttcuz-plugins/main/install.sh | bash`, потом `ttcuz-plugin new my-plugin`.

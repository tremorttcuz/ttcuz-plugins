# Сборка плагина на телефоне (Termux)

Без компьютера и без аккаунта GitHub. Нужен Termux и интернет при первой сборке.

## 1. Установите Termux

Ставьте Termux **из F-Droid** (f-droid.org → Termux). Версия из Google Play устарела, и пакеты в ней не устанавливаются.

## 2. Установите сборщик одной командой

Откройте Termux и вставьте:

```sh
curl -fsSL https://raw.githubusercontent.com/tremorttcuz/ttcuz-plugins/main/install.sh | bash
```

Если попросит доступ к файлам — нажмите **Разрешить**. Команда ставит Python и JDK 17, скачивает сборщик в `~/.ttcuz-plugins` и добавляет команду `ttcuz-plugin`. Повторный запуск или `ttcuz-plugin update` обновляет сборщик.

## 3. Первый плагин

```sh
ttcuz-plugin new my-plugin
```

В «Загрузках» появится папка `my-plugin` с готовым плагином-примером. Соберите его:

```sh
ttcuz-plugin ~/storage/downloads/my-plugin
```

Первая сборка скачивает `android.jar` и `r8.jar` (около 80 МБ), дальше интернет не нужен. Готовый `my-plugin.ctt` окажется в «Загрузках».

Код правьте в любом редакторе (например, Acode): `my-plugin/java/…/Hello.java` и `my-plugin/manifest.json`. Что писать — в [plugins.md](plugins.md).

## 4. Установите плагин

В TikTok с ttcuz: **Настройки ttcuz → Плагины → Установить .ctt** → выберите файл из «Загрузок».

---

## Без установщика: из архива

Если `curl` недоступен, можно вручную.

```sh
pkg update -y && pkg install -y python openjdk-17 unzip
```

```sh
termux-setup-storage
```

Скачайте архив на странице репозитория (**Code → Download ZIP**, файл `ttcuz-plugins-main.zip` в «Загрузках») и распакуйте:

```sh
cd ~ && unzip -o ~/storage/downloads/ttcuz-plugins-main.zip && cd ttcuz-plugins-main
```

> Если файл скачивался несколько раз, Android добавляет номер: `ttcuz-plugins-main (1).zip`. Точное имя: `ls ~/storage/downloads`. Имя с пробелом — в кавычках: `unzip -o ~/storage/downloads/"ttcuz-plugins-main (1).zip"`.

Сборка примера:

```sh
python -m ttcuz.plugin examples/hello -o ~/storage/downloads/hello.ctt
```

## Если что-то не так

| Что видно | Что делать |
|---|---|
| `No such file or directory` с путём к zip | Неверное имя архива. Посмотрите его: `ls ~/storage/downloads` |
| `Permission denied` на `~/storage/...` | Ещё раз выполните `termux-setup-storage` и разрешите доступ |
| `javac not found` | `pkg install -y openjdk-17` |
| `No module named ttcuz` | Сборка из архива: сначала `cd ~/ttcuz-plugins-main` |
| `ttcuz-plugin: command not found` | Установщик не завершился: запустите команду установки ещё раз |
| Ошибка загрузки при первой сборке | Нет интернета. Повторите команду, загрузка продолжится |
| `manifest.json has no …` | В манифесте не хватает поля `id` или `entry` |
| `the entry class … is not among the compiled classes` | `entry` в манифесте не совпадает с пакетом и именем класса |
| `javac failed` и текст ошибки | Ошибка в коде. Строка и причина показаны выше |
| `OutOfMemoryError` | Закройте другие приложения и повторите |
| Termux закрывается посреди сборки | Держите его на экране или выполните `termux-wake-lock` |

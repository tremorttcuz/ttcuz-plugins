# Как написать плагин

Плагин — это Java-класс, который наследует `cat.ttcuz.framework.plugin.TtcuzPlugin` и переопределяет нужные ему хуки. Сборщик компилирует Java, собирает один dex и упаковывает его в `.ctt` вместе с `manifest.json` и необязательной `icon.png`.

```
my-plugin.ctt
├── manifest.json   id, имя, версия, автор, главный класс
├── classes.dex     код плагина
└── icon.png        иконка, необязательно
```

> **Безопасность.** У плагина нет песочницы: он работает внутри TikTok со всеми его правами. Ставьте только то, что прочитали сами.

Как собирать: на телефоне — [termux.md](termux.md), на компьютере — [pc.md](pc.md).

---

## Первый плагин с нуля

### Шаг 1. Папки

```
my-plugin/
├── manifest.json
└── java/
    └── com/me/counter/
        └── Main.java
```

Путь в `java/` должен совпадать с пакетом класса: пакет `com.me.counter` лежит в папке `java/com/me/counter/`.

### Шаг 2. manifest.json

```json
{
  "id": "com.me.counter",
  "name": "Счётчик запусков",
  "version": "1.0",
  "author": "me",
  "description": "Считает запуски приложения",
  "entry": "com.me.counter.Main",
  "api": 1,
  "tiktok": "47.2.41"
}
```

### Шаг 3. Main.java

```java
package com.me.counter;

import android.app.Activity;

import cat.ttcuz.framework.plugin.PluginSettings;
import cat.ttcuz.framework.plugin.TtcuzPlugin;

public final class Main extends TtcuzPlugin {

    @Override
    public void onStart() {
        int starts = ttcuz().prefs().getInt("starts", 0) + 1;
        ttcuz().prefs().edit().putInt("starts", starts).apply();
        ttcuz().log("запуск №" + starts);
    }

    // Своя страница настроек плагина; значения попадают в prefs() под этими ключами.
    @Override
    public void onSettings(PluginSettings settings) {
        settings.toggle("greet", "Показывать приветствие", true);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        // Вызывается на каждом экране: должно работать быстро.
        if (ttcuz().prefs().getBoolean("greet", true)
                && activity.getClass().getName().endsWith("MainActivity")) {
            ttcuz().toast("Это запуск №" + ttcuz().prefs().getInt("starts", 0));
            ttcuz().prefs().edit().putBoolean("greet", false).apply();
        }
    }
}
```

### Шаг 4. Сборка

```bash
python3 -m ttcuz.plugin my-plugin -o my-plugin.ctt
```

Иконку добавить просто: положите `icon.png` (квадрат, 128×128 достаточно) в папку плагина.

---

## manifest.json

| Поле | Обязательно | Что это |
|---|---|---|
| `id` | да | Имя плагина на диске и в настройках: латиница, цифры, `.`, `-`, `_`, начинается с буквы или цифры, до 128 символов. По нему плагин обновляется, поэтому не меняйте его между версиями. |
| `entry` | да | Полное имя главного класса, например `com.me.counter.Main`. |
| `name` | нет | Имя в списке (по умолчанию `id`). |
| `version` | нет | Строка рядом с именем (по умолчанию `1.0`). |
| `author`, `description` | нет | Автор и описание в списке. |
| `api` | нет | Версия Plugin API. Сейчас она одна: `1`. |
| `tiktok` | нет | Версия TikTok, для которой плагин написан. Если указана, должна быть `47.2.41`. |

---

## Хуки (TtcuzPlugin)

Переопределяйте только нужные. Все остальные по умолчанию ничего не делают.

| Хук | Когда вызывается |
|---|---|
| `onLoad()` | Плагин загружен, до `onStart`. Может бросить исключение: тогда плагин не включится, а ошибка будет видна в списке. |
| `onStart()` / `onStop()` | Плагин включён / выключен (или приложение закрывается). |
| `onActivityCreated`, `onActivityResumed`, `onActivityPaused`, `onActivityDestroyed(Activity)` | Жизненный цикл экранов. `onActivityResumed` вызывается часто, должен работать быстро. |
| `onAccountChanged(String uid)` | Сменился аккаунт. |
| `onPreferenceChanged(String key, String value)` | Изменилась настройка плагина. |
| `onSettings(PluginSettings)` | Строится страница настроек плагина: `text`, `action`, `toggle`, `choice`, `slider`, `view`. |
| `boolean onOpenSettings(Activity)` | Пользователь открыл настройки плагина. Верните `true`, если показали своё окно вместо страницы. |
| `int onColour(int)` | Каждый перекрашиваемый цвет. Это горячий путь: здесь ничего не создавайте и ничего не логируйте. |
| `onView(View)` | Каждый view, который мод перекрашивает. |
| `String onRegion(String key, String value)` | Ответ о стране или операторе (`sim_country`, `network_operator` и т. д.). |
| `List onFeed(List posts)` | Страница ленты до отрисовки. Можно вернуть список без лишних постов. |
| `String onNickname(String uid, String name)` | Ник перед выводом на экран. |
| `CharSequence onText(TextView, CharSequence)` | Текст перед выводом во view. |
| `onEvent(String name, Object value)` | Событие от другого плагина (`ttcuz().emit`). |

Если хук выбросит исключение, ошибка запишется в журнал мода и появится в списке плагинов. TikTok при этом не упадёт.

---

## Возможности мода (PluginContext)

Доступ к моду — через `ttcuz()` внутри плагина.

- `app()` — контекст приложения; `screen()` — текущий экран или `null`.
- `prefs()`, `accountPrefs()` — свои настройки, общие или для текущего аккаунта.
- `bundle()` — распакованный `.ctt` (только чтение); `data()` — своя папка для файлов.
- `accountId()`, `accent()` — текущий аккаунт и цвет акцента.
- `hostLoader()`, `type(name)`, `method(type, name, params…)`, `field(type, name)` — доступ к классам TikTok через рефлексию.
- `log(line)` — строка в журнал мода; `toast(text)` — короткое сообщение.
- `ui(runnable)`, `later(runnable, ms)`, `away(runnable)` — главный поток, отложенный запуск, фоновый поток. Отложенное отменяется, когда плагин выключают.
- `emit(name, value)` — событие всем плагинам.

Полные сигнатуры: `sdk/cat/ttcuz/framework/plugin/`.

---

## Правила и ограничения

- `classes.dex` до 16 МиБ, `manifest.json` до 64 КиБ, `icon.png` до 1 МиБ. Сборщик проверяет это заранее.
- Только один dex: если код не помещается в один, сборщик откажется собирать.
- Классы `cat.ttcuz.framework.plugin.*` в ваш dex не попадают, их предоставляет приложение. Не кладите в плагин свои копии этих классов.
- Java 17 в исходниках разрешена; d8 переводит код под Android 7+ (min-api 24).
- Сторонние библиотеки (`.jar`) пока не поддерживаются: переносите нужный код в `java/` исходниками.

---

## Ошибки и их решение

| Сообщение | Причина и решение |
|---|---|
| `javac not found: install JDK 17 or newer` | Не установлен JDK или он не в PATH. Termux: `pkg install openjdk-17`. |
| `no manifest.json in …` | Неверная папка: укажите ту, где лежит `manifest.json`. |
| `manifest.json has no id, entry …` | В манифесте не хватает обязательных полей. |
| `api must be 1` / `tiktok '…'` | Плагин написан под другую версию API или TikTok. |
| `manifest.json is not valid JSON` | Ошибка синтаксиса: лишняя или пропущенная запятая, кавычки «ёлочки» вместо `"`. |
| `id 'a b': Latin letters, digits…` | В `id` недопустимые символы. |
| `entry 'Main' is not a fully qualified class name` | Нужно полное имя: `com.me.counter.Main`. |
| `the entry class … is not among the compiled classes` | Пакет в `.java` не совпадает с `entry` или с путём папки. |
| `no .java under …/java` | Исходники должны лежать в подпапке `java/`. |
| `icon.png is not a PNG` | Файл переименован из jpg или webp. Сохраните его настоящим PNG. |
| `javac failed` с текстом ошибки | Ошибка в коде: строка и причина показаны над сообщением. |
| Ошибка загрузки при первом запуске | Нет интернета или сайт недоступен. Повторите команду; если что-то повреждено, удалите папку `tools/`. |
| `OutOfMemoryError` (Termux) | Закройте другие приложения: d8 нужно несколько сотен МБ. |

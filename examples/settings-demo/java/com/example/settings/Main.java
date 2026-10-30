package com.example.settings;

import android.widget.TextView;

import cat.ttcuz.framework.plugin.PluginSettings;
import cat.ttcuz.framework.plugin.TtcuzPlugin;

/** text, action, toggle, choice, slider and view; values live in prefs() under their keys. */
public final class Main extends TtcuzPlugin {

    @Override
    public void onSettings(PluginSettings s) {
        s.text("Строка без действия: заголовок или пояснение.");
        s.action("Показать сообщение", () -> ttcuz().toast("Привет из настроек"));
        s.toggle("enabled", "Включить что-то", true);
        s.choice("mode", "Режим", new String[]{"Тихий", "Обычный", "Громкий"}, 1);
        s.slider("size", "Размер", 10, 40, 20);
        TextView note = new TextView(ttcuz().app());
        note.setText("Свой view: что угодно, нарисованное плагином.");
        s.view(note);
    }

    @Override
    public void onPreferenceChanged(String scope, String key) {
        // scope: "plugin" for prefs(), "plugin-account" for accountPrefs()
        if ("plugin".equals(scope) && "mode".equals(key)) {
            ttcuz().log("режим теперь " + ttcuz().prefs().getInt("mode", 1));
        }
    }

    @Override
    public void onAccountChanged(String uid) {
        // settings of one account: accountPrefs() is a different file for each account
        int visits = ttcuz().accountPrefs().getInt("visits", 0) + 1;
        ttcuz().accountPrefs().edit().putInt("visits", visits).apply();
    }
}

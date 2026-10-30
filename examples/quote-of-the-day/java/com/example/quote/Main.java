package com.example.quote;

import cat.ttcuz.framework.plugin.PluginSettings;
import cat.ttcuz.framework.plugin.TtcuzPlugin;

/** Network on the plugin's own thread, a notification, an Android permission asked for once. */
public final class Main extends TtcuzPlugin {
    private static final long DAY = 24L * 60 * 60 * 1000;

    @Override
    public void onStart() {
        long last = ttcuz().prefs().getLong("last", 0);
        if (System.currentTimeMillis() - last < DAY) return;
        if (!ttcuz().has("android.permission.POST_NOTIFICATIONS")) {
            ttcuz().request("android.permission.POST_NOTIFICATIONS", granted -> { if (granted) fetch(); });
        } else {
            fetch();
        }
    }

    private void fetch() {
        ttcuz().away(() -> {                       // never on the main thread
            try {
                String line = ttcuz().http("https://api.github.com/zen").trim();
                ttcuz().prefs().edit().putLong("last", System.currentTimeMillis()).putString("quote", line).apply();
                ttcuz().notify("Цитата дня", line);
            } catch (java.io.IOException error) {
                ttcuz().log("не получилось: " + error.getMessage());
            }
        });
    }

    @Override
    public void onSettings(PluginSettings settings) {
        settings.text("Последняя: " + ttcuz().prefs().getString("quote", "ещё нет"));
        settings.action("Получить сейчас", this::fetch);
    }
}

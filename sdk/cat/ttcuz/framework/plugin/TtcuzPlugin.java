package cat.ttcuz.framework.plugin;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import java.util.List;

/**
 * A ttcuz plugin. Override the hooks it needs; ttcuz() is everything the mod hands it.
 *
 * API 2 (this one) checks the "permissions" list of manifest.json: onView and onScreen need "interface", onText and
 * onNickname "text", onFeed "feed", onColour "colours", onRegion "region", onEvent "events", onAccountChanged
 * "account". API 1 plugins keep working as they are.
 *
 * API 3 adds "effects" (blur, motion blur, colour matrices, shaders) and "mod": the mod's own screens
 * (onModScreen, onModPanel), rows in the mod's settings (onModSettings) and the mod's settings themselves.
 */
public abstract class TtcuzPlugin {
    public static final int API = 3;
    private PluginContext context;

    public void onAccountChanged(String str) {
    }

    public void onActivityCreated(Activity activity) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public int onColour(int i) {
        return i;
    }

    public void onEvent(String str, Object obj) {
    }

    public List onFeed(List list) {
        return list;
    }

    public void onLoad() throws Exception {
    }

    public String onNickname(String str, String str2) {
        return str2;
    }

    public boolean onOpenSettings(Activity activity) {
        return false;
    }

    public void onPreferenceChanged(String str, String str2) {
    }

    public String onRegion(String str, String str2) {
        return str2;
    }

    public void onSettings(PluginSettings pluginSettings) {
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public CharSequence onText(TextView textView, CharSequence charSequence) {
        return charSequence;
    }

    public void onView(View view) {
    }

    /** A screen came to the front and has been laid out: content is its android.R.id.content. Needs "interface". */
    public void onScreen(Activity activity, android.view.ViewGroup content) {
    }

    /** The mod's settings screen was drawn (again): root is all of it, to restyle or add to. Needs "mod". */
    public void onModScreen(Activity activity, android.view.ViewGroup root) {
    }

    /** One of the mod's sheets is opening: title is its heading, content its rows. Needs "mod". */
    public void onModPanel(String title, android.view.ViewGroup content) {
    }

    /** Rows of this plugin's own inside the mod's settings, under "Plugins". Needs "mod". */
    public void onModSettings(PluginSettings page) {
    }

    /** An Android permission this plugin asked for with ttcuz().request(...) was answered. */
    public void onPermission(String permission, boolean granted) {
    }

    public final void attach(PluginContext pluginContext) {
        if (this.context != null) {
            throw new IllegalStateException("Already attached");
        }
        this.context = pluginContext;
    }

    public final PluginContext ttcuz() {
        return this.context;
    }
}

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
 */
public abstract class TtcuzPlugin {
    public static final int API = 2;
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

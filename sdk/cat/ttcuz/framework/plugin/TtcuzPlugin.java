package cat.ttcuz.framework.plugin;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import java.util.List;

/* loaded from: /home/danik/Downloads/ttcuz-work/oldapk/classes54.dex */
public abstract class TtcuzPlugin {
    public static final int API = 1;
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

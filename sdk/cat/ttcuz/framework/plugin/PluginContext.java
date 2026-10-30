package cat.ttcuz.framework.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Compile-time signatures of what ttcuz hands a plugin. The app provides the real class at run time, so the
 * plugin builder leaves this one out of the plugin's dex; the bodies here are never run.
 *
 * Plugin API 2: what a plugin may use follows "permissions" in its manifest.json. A call it did not ask for throws
 * SecurityException naming the permission. ttcuz permissions: interface, text, feed, colours, region, events,
 * network, notifications, clipboard, storage, account, tiktok; Android ones by their full name
 * (android.permission.CAMERA, RECORD_AUDIO, ACCESS_FINE_LOCATION, READ_CONTACTS, ...).
 */
public final class PluginContext implements AutoCloseable {
    /** A choice from menu(). */
    public interface Pick { void picked(int index); }
    /** The answer to request(). */
    public interface Result { void done(boolean granted); }

    /** The plugin's id from its manifest. */
    public final String id = null;

    private PluginContext() { throw new UnsupportedOperationException("provided by ttcuz"); }

    // ---- permissions
    /** The plugin API from the manifest (1 or 2). */
    public int api() { throw new UnsupportedOperationException(); }
    /** The permissions from the manifest. */
    public List<String> permissions() { throw new UnsupportedOperationException(); }
    /** Whether the plugin may use this permission. */
    public boolean can(String permission) { throw new UnsupportedOperationException(); }

    // ---- basics
    /** The application context. */
    public Context app() { throw new UnsupportedOperationException(); }
    /** The activity on screen, or null. */
    public Activity screen() { throw new UnsupportedOperationException(); }
    /** The signed-in TikTok account id, or null. Needs "account". */
    public String accountId() { throw new UnsupportedOperationException(); }
    /** The accent colour the user chose. */
    public int accent() { throw new UnsupportedOperationException(); }
    /** The plugin's own settings, shared by every account. */
    public SharedPreferences prefs() { throw new UnsupportedOperationException(); }
    /** The plugin's settings for the account signed in now. */
    public SharedPreferences accountPrefs() { throw new UnsupportedOperationException(); }
    /** The unpacked .ctt (read only). */
    public File bundle() { throw new UnsupportedOperationException(); }
    /** A folder the plugin may write to. */
    public File data() { throw new UnsupportedOperationException(); }
    /** TikTok's class loader. Needs "tiktok". */
    public ClassLoader hostLoader() { throw new UnsupportedOperationException(); }
    /** A TikTok class by name. Needs "tiktok". */
    public Class<?> type(String name) throws ClassNotFoundException { throw new UnsupportedOperationException(); }
    /** A method of a TikTok class, made accessible. Needs "tiktok". */
    public Method method(String type, String name, Class<?>... parameters) throws ReflectiveOperationException { throw new UnsupportedOperationException(); }
    /** A field of a TikTok class, made accessible. Needs "tiktok". */
    public Field field(String type, String name) throws ReflectiveOperationException { throw new UnsupportedOperationException(); }
    /** A line in the mod's diary, marked with the plugin's id. */
    public void log(String message) { throw new UnsupportedOperationException(); }
    /** A short message on screen. */
    public void toast(String message) { throw new UnsupportedOperationException(); }
    /** Sends an event to every plugin's onEvent. */
    public void emit(String name, Object value) { throw new UnsupportedOperationException(); }
    /** Runs on the main thread. */
    public void ui(Runnable action) { throw new UnsupportedOperationException(); }
    /** Runs on the main thread after a delay in milliseconds; cancelled when the plugin stops. */
    public void later(Runnable action, long delay) { throw new UnsupportedOperationException(); }
    /** Runs on the plugin's background thread. */
    public void away(Runnable action) { throw new UnsupportedOperationException(); }

    // ---- interface
    /** px for dp on this phone. */
    public int dp(float value) { throw new UnsupportedOperationException(); }
    /** The colours of the mod's screens now: {page, card, text}. */
    public int[] palette() { throw new UnsupportedOperationException(); }
    /** Puts a view over the screen in the plugin's own layer (gravity: android.view.Gravity). Needs "interface". */
    public View overlay(Activity activity, View view, int gravity) { throw new UnsupportedOperationException(); }
    /** Puts a view over the screen, top left. Needs "interface". */
    public View overlay(Activity activity, View view) { throw new UnsupportedOperationException(); }
    /** Takes an overlay away. */
    public void removeOverlay(View view) { throw new UnsupportedOperationException(); }
    /** A view of the screen by its TikTok resource name. Needs "interface". */
    public View find(Activity activity, String idName) { throw new UnsupportedOperationException(); }
    /** Every view of this type under root. Needs "interface". */
    public <T extends View> List<T> views(View root, Class<T> type) { throw new UnsupportedOperationException(); }
    /** The mod's entrance animation. */
    public void animateIn(View view) { throw new UnsupportedOperationException(); }
    /** The mod's exit animation, then after (may be null). */
    public void animateOut(View view, Runnable after) { throw new UnsupportedOperationException(); }
    /** A dialog in the mod's style; cancel may be null. */
    public void dialog(String title, String message, String ok, Runnable onOk, String cancel) { throw new UnsupportedOperationException(); }
    /** A list to choose from, in the mod's style. */
    public void menu(String title, String[] items, Pick pick) { throw new UnsupportedOperationException(); }

    // ---- Android permissions
    /** Whether Android has granted it. */
    public boolean has(String androidPermission) { throw new UnsupportedOperationException(); }
    /** Asks the person for an Android permission listed in the manifest; the answer also reaches onPermission. */
    public void request(String androidPermission, Result result) { throw new UnsupportedOperationException(); }

    // ---- network, files, phone
    /** GET over https, from away(). Needs "network". */
    public String http(String url) throws java.io.IOException { throw new UnsupportedOperationException(); }
    /** Any method over https, from away(). Needs "network". */
    public String http(String method, String url, String body, String contentType) throws java.io.IOException { throw new UnsupportedOperationException(); }
    /** A notification. Needs "notifications". */
    public void notify(String title, String text) { throw new UnsupportedOperationException(); }
    /** Copies text. Needs "clipboard". */
    public void copy(String text) { throw new UnsupportedOperationException(); }
    /** What is copied now. Needs "clipboard". */
    public String paste() { throw new UnsupportedOperationException(); }
    /** Saves a file to Downloads/ttcuz/&lt;id&gt;/. Needs "storage". */
    public android.net.Uri saveDownload(String name, String mime, byte[] bytes) throws java.io.IOException { throw new UnsupportedOperationException(); }
    /** Opens a link. */
    public void open(String url) { throw new UnsupportedOperationException(); }
    /** Android's share sheet with this text. */
    public void share(String text) { throw new UnsupportedOperationException(); }
    /** A short vibration. Needs android.permission.VIBRATE. */
    public void vibrate(long millis) { throw new UnsupportedOperationException(); }

    public void accountChanged() { throw new UnsupportedOperationException(); }
    @Override public void close() { throw new UnsupportedOperationException(); }
}

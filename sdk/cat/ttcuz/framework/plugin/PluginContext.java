package cat.ttcuz.framework.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Compile-time signatures of what ttcuz hands a plugin. The app provides the real class at run time, so the
 * plugin builder leaves this one out of the plugin's dex; the bodies here are never run.
 */
public final class PluginContext implements AutoCloseable {
    /** The plugin's id from its manifest. */
    public final String id = null;

    private PluginContext() { throw new UnsupportedOperationException("provided by ttcuz"); }

    /** The application context. */
    public Context app() { throw new UnsupportedOperationException(); }
    /** The activity on screen, or null. */
    public Activity screen() { throw new UnsupportedOperationException(); }
    /** The signed-in TikTok account id, or "" when nobody is. */
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
    /** TikTok's class loader. */
    public ClassLoader hostLoader() { throw new UnsupportedOperationException(); }
    /** A TikTok class by name. */
    public Class<?> type(String name) throws ClassNotFoundException { throw new UnsupportedOperationException(); }
    /** A method of a TikTok class, made accessible. */
    public Method method(String type, String name, Class<?>... parameters) throws ReflectiveOperationException { throw new UnsupportedOperationException(); }
    /** A field of a TikTok class, made accessible. */
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
    public void accountChanged() { throw new UnsupportedOperationException(); }
    @Override public void close() { throw new UnsupportedOperationException(); }
}

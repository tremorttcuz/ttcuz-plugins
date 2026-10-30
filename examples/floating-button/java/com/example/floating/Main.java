package com.example.floating;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cat.ttcuz.framework.plugin.TtcuzPlugin;

/** A round button over the feed (MainActivity), in the plugin's own layer. */
public final class Main extends TtcuzPlugin {
    private View button;

    // @uses interface: ставит кнопку на каждый открытый экран
    @Override
    public void onScreen(Activity activity, ViewGroup content) {
        boolean feed = activity.getClass().getName().endsWith(".MainActivity");
        if (!feed) return;
        if (button != null && button.isAttachedToWindow()) return; // already there

        TextView round = new TextView(activity);
        round.setText("+");
        round.setTextSize(22);
        round.setTextColor(0xFFFFFFFF);
        round.setGravity(Gravity.CENTER);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(ttcuz().accent());
        round.setBackground(shape);
        round.setOnClickListener(v -> {
            int taps = ttcuz().prefs().getInt("taps", 0) + 1;
            ttcuz().prefs().edit().putInt("taps", taps).apply();
            ttcuz().toast("Нажатий: " + taps);
        });

        FrameLayout.LayoutParams size = new FrameLayout.LayoutParams(ttcuz().dp(52), ttcuz().dp(52));
        size.setMargins(0, 0, ttcuz().dp(16), ttcuz().dp(140));
        round.setLayoutParams(size);
        // @uses interface: круглая кнопка поверх экрана
        button = ttcuz().overlay(activity, round, Gravity.BOTTOM | Gravity.END);
        ttcuz().animateIn(button);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // the layer belongs to that screen; drop the reference so the screen can go
        if (button != null && !button.isAttachedToWindow()) button = null;
    }
}

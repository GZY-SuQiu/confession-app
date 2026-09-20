package com.secret.love;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity {

    private static final String[] NO_LINES = {
            "你确定？",
            "再想想？",
            "最后一次机会哦～",
            "「不愿意」正在减肥…",
            "它跑不动了，点「愿意」吧 ♥"
    };

    private View setupPanel, confessPanel, root;
    private EditText etName, etMsg;
    private Button btnStart, btnYes, btnNo;
    private TextView heartBig, tvName, tvAsk, tvSuccess;
    private Random random = new Random();
    private int escapeCount = 0;
    private boolean finished = false;
    private android.animation.AnimatorSet pulse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root);
        setupPanel = findViewById(R.id.setupPanel);
        confessPanel = findViewById(R.id.confessPanel);
        etName = findViewById(R.id.etName);
        etMsg = findViewById(R.id.etMsg);
        btnStart = findViewById(R.id.btnStart);
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        heartBig = findViewById(R.id.heartBig);
        tvName = findViewById(R.id.tvName);
        tvAsk = findViewById(R.id.tvAsk);
        tvSuccess = findViewById(R.id.tvSuccess);

        btnStart.setOnClickListener(v -> startConfession());
        btnYes.setOnClickListener(v -> accept());
        btnNo.setOnClickListener(v -> dodge());

        android.os.Handler main = new android.os.Handler(android.os.Looper.getMainLooper());
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> main.post(() -> showCrash(e)));

        startHeartPulse();
    }

    private void showCrash(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().getName()).append(": ").append(e.getMessage()).append("\n");
        for (StackTraceElement el : e.getStackTrace()) {
            sb.append("  at ").append(el).append("\n");
            if (sb.length() > 1200) break;
        }
        TextView tv = new TextView(this);
        tv.setText(sb.toString());
        tv.setBackgroundColor(0xE0100510);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTextSize(10f);
        tv.setPadding(48, 48, 48, 48);
        tv.setGravity(android.view.Gravity.TOP);
        tv.setTypeface(android.graphics.Typeface.MONOSPACE);
        root.addView(tv, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    private void startConfession() {
        String name = etName.getText().toString().trim();
        String msg = etMsg.getText().toString().trim();

        tvName.setText(name.isEmpty() ? "亲爱的" : name);
        tvAsk.setText(msg.isEmpty()
                ? "在吗？\n有件事憋在心里很久了，\n必须当面说清楚——\n\n我愿意陪你到老吗？"
                : msg);

        setupPanel.setVisibility(View.GONE);
        confessPanel.setVisibility(View.VISIBLE);
        startHeartPulse();
    }

    private void startHeartPulse() {
        if (pulse != null) pulse.cancel();
        android.animation.ObjectAnimator ax =
                android.animation.ObjectAnimator.ofFloat(heartBig, "scaleX", 1f, 1.25f);
        android.animation.ObjectAnimator ay =
                android.animation.ObjectAnimator.ofFloat(heartBig, "scaleY", 1f, 1.25f);
        for (android.animation.ValueAnimator a : new android.animation.ValueAnimator[]{ax, ay}) {
            a.setDuration(700);
            a.setRepeatCount(android.animation.ValueAnimator.INFINITE);
            a.setRepeatMode(android.animation.ValueAnimator.REVERSE);
            a.setInterpolator(new LinearInterpolator());
        }
        pulse = new android.animation.AnimatorSet();
        pulse.playTogether(ax, ay);
        pulse.start();
    }

    private void accept() {
        if (finished) return;
        finished = true;

        if (pulse != null) pulse.cancel();
        tvAsk.animate().alpha(0f).setDuration(300).start();
        btnYes.animate().alpha(0f).setDuration(300).start();
        btnNo.animate().alpha(0f).setDuration(300).start();

        String name = tvName.getText().toString();
        tvSuccess.setText("❤ 太棒了 ❤\n\n" + name + " 答应你了！\n从今以后，\n牵手、拥抱、偏爱和例外，\n都是你的了。\n\n—— 表白成功，锁死！ 🎉");
        tvSuccess.setVisibility(View.VISIBLE);
        tvSuccess.setAlpha(0f);
        tvSuccess.setScaleX(0.6f);
        tvSuccess.setScaleY(0.6f);
        tvSuccess.animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(600).setInterpolator(new DecelerateInterpolator()).start();

        animateHeartBurst();
        vibrate(400);
    }

    private void animateHeartBurst() {
        heartBig.animate().cancel();
        heartBig.setScaleX(1f);
        heartBig.setScaleY(1f);
        heartBig.animate()
                .scaleX(2.4f).scaleY(2.4f).alpha(0.15f)
                .setDuration(900).setInterpolator(new AccelerateInterpolator())
                .start();
    }

    private void dodge() {
        if (finished) return;
        escapeCount++;

        int w = root.getWidth();
        int h = root.getHeight();
        int bw = btnNo.getWidth();
        int bh = btnNo.getHeight();

        int maxX = Math.max(0, w - bw - 8);
        int maxY = Math.max(0, h - bh - 8);
        int targetX = random.nextInt(maxX + 1);
        int targetY = random.nextInt(maxY + 1);

        FrameLayout.LayoutParams lp =
                (FrameLayout.LayoutParams) btnNo.getLayoutParams();
        lp.leftMargin = targetX;
        lp.topMargin = targetY;
        lp.gravity = android.view.Gravity.TOP | android.view.Gravity.START;
        btnNo.setLayoutParams(lp);

        btnNo.animate()
                .scaleX(1.1f).scaleY(1.1f)
                .setDuration(150)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> btnNo.animate().scaleX(1f).scaleY(1f).setDuration(150).start())
                .start();

        if (escapeCount >= NO_LINES.length) {
            btnNo.setText(NO_LINES[NO_LINES.length - 1]);
            btnNo.setAlpha(0.35f);
            btnNo.setEnabled(false);
            // 把残骸放到「愿意」按钮旁边，嘲讽拉满
            FrameLayout.LayoutParams yesLp =
                    (FrameLayout.LayoutParams) btnYes.getLayoutParams();
            lp.leftMargin = Math.max(8, targetX);
            lp.topMargin = Math.max(0, h - bh - 40);
            btnNo.setLayoutParams(lp);
        } else {
            btnNo.setText(NO_LINES[escapeCount]);
        }
    }

    private void vibrate(long ms) {
        if (Build.VERSION.SDK_INT >= 26) {
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vib != null) vib.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.EFFECT_HEAVY_CLICK));
        }
    }
}

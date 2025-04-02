package com.example.kiosk;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

public class TimerHelper {
    private CountDownTimer countDownTimer;
    private boolean isCountDownRunning = false;
    // countdownTime 변수 사용
    private long countdownTime;
    private final long COUNTDOWN_INTERVAL = 1000;    // 1초 간격
    private Context context;
    private TextView timerTextView;

    // 타이머 종료 시 호출할 콜백 인터페이스
    public interface OnTimerFinishListener {
        void onTimerFinish();
    }

    private OnTimerFinishListener onTimerFinishListener;

    // 콜백 설정 메서드
    public void setOnTimerFinishListener(OnTimerFinishListener listener) {
        this.onTimerFinishListener = listener;
    }

    // 새 생성자: ageGroup을 받아 countdownTime 설정
    public TimerHelper(Context context, TextView timerTextView, String ageGroup) {
        this.context = context;
        this.timerTextView = timerTextView;
        if ("senior".equalsIgnoreCase(ageGroup)) {
            this.countdownTime = 500 * 1000; // 500초
        } else {
            this.countdownTime = 300 * 1000; // 300초
        }
    }

    // 기존 생성자를 그대로 두거나, 아래와 같이 기본값 생성자를 사용할 수 있습니다.
    public TimerHelper(Context context, TextView timerTextView) {
        this(context, timerTextView, "");
    }

    // 타이머 시작: 기존 타이머 중지 후 새로 시작
    public void startTimer() {
        stopTimer();
        countDownTimer = new CountDownTimer(countdownTime, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                timerTextView.setText(String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
                Toast.makeText(context, "주문 시간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
                if (onTimerFinishListener != null) {
                    onTimerFinishListener.onTimerFinish();
                }
                startTimer();
            }
        };
        countDownTimer.start();
        isCountDownRunning = true;
    }

    // 타이머 중지
    public void stopTimer() {
        if (isCountDownRunning && countDownTimer != null) {
            countDownTimer.cancel();
        }
        isCountDownRunning = false;
    }
}

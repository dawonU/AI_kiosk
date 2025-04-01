package com.example.kiosk;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

public class TimerHelper {
    private CountDownTimer countDownTimer;
    private boolean isCountDownRunning = false;
    private final long COUNTDOWN_TIME = 300000;    // 300초 = 300,000ms
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

    // 생성자: 타이머를 표시할 TextView와 Context를 전달
    public TimerHelper(Context context, TextView timerTextView) {
        this.context = context;
        this.timerTextView = timerTextView;
    }

    // 타이머 시작: 기존 타이머 중지 후 새로 시작 (타이머 종료 시 콜백 호출 후 재시작)
    public void startTimer() {
        stopTimer();
        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                timerTextView.setText(String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
                Toast.makeText(context, "주문 시간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
                // 타이머 종료 시 콜백 호출 (MenuMain에서 장바구니 초기화 등 처리)
                if (onTimerFinishListener != null) {
                    onTimerFinishListener.onTimerFinish();
                }
                // 타이머 재시작 (300초부터 다시 시작)
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

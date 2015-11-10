package com.itheima.simplewave.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyRing extends View {

	private int startX;
	private int startY;
	private int radius;
	private Paint mPaint;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 半径变大
			// 粗度变大
			// 颜色越来越淡
			radius += 5;
			mPaint.setStrokeWidth(radius / 5);
			int al = mPaint.getAlpha();
			al -= 10;
			if (al < 0) {
				al = 0;
			}
			mPaint.setAlpha(al);
			invalidate();// 刷新
			if (al > 0) {// 只有圆环没有完全消失,才去绘制
				handler.sendEmptyMessageDelayed(0, 50);// 继续发送消息,发送延时的消息
			}
		}
	};

	public MyRing(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public MyRing(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public MyRing(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		radius=0;//初始化的时候把宽度再设为0;
		
		mPaint = new Paint();
		mPaint.setStrokeWidth(radius / 3);
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Style.STROKE);// 画圆环
		mPaint.setAntiAlias(true);// 去锯齿
		mPaint.setAlpha(255);// 完全不透明
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle(startX, startY, radius, mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) event.getX();
			startY = (int) event.getY();

			// 重新初始化数据
			initView();
			handler.sendEmptyMessage(0);
			break;
		}
		return super.onTouchEvent(event);
	}
}

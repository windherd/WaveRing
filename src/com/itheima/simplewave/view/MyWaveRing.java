package com.itheima.simplewave.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 圆环波浪开发流程: 
 * 1.重写onTouchEvent,ACTION_DOWN,ACTION_MOVE
 * 2.封装一个wave对象
 * 3.圆环集合mWaveList
 * 4.添加一个点addPoint(),第一次进入,启动绘制流程 
 * 5.handler中接收消息 
 * 6刷新集合中圆环的 属性 
 * 7invalidate,刷新界面,继续发消息
 * 8.onDraw,绘制所有圆环
 * 9.圆环的间距,透明度,宽度变化速度的微调
 * 
 * @author Administrator
 * 
 */
public class MyWaveRing extends View {
	private static final int MIN_DIS = 10;// 两个圆环间的最小间距
	private ArrayList<Wave> mWaveLists = new ArrayList<Wave>();// 圆环的集合
	private int[] mColors = new int[] { Color.RED, Color.YELLOW, Color.BLUE,
			Color.GREEN, Color.CYAN ,Color.MAGENTA};
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			flushData();
			invalidate();// 刷新,重新走onDraw
			if (!mWaveLists.isEmpty()) {
				// 集合为空时不再发送消息
				handler.sendEmptyMessageDelayed(0, 50);
			}

		}
	};

	public MyWaveRing(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 刷新数据
	 */
	protected void flushData() {
		ArrayList<Wave> removeLists = new ArrayList<Wave>();// 要移除的Wave对象收集起来
		// 遍历圆环的集合,修改每个圆环的属性
		for (Wave wave : mWaveLists) {
			wave.radius += 3;
			wave.paint.setStrokeWidth(wave.radius / 3);
			if (wave.paint.getAlpha() <= 0) {
				// mWaveLists.remove(wave);//并发修改异常
				removeLists.add(wave);
				continue;// 如果满足条件后面的不执行,循环下一个
			}
			int alpha = wave.paint.getAlpha();
			alpha -= 5;
			if (alpha < 0) {
				alpha = 0;
			}
			wave.paint.setAlpha(alpha);
		}
		mWaveLists.removeAll(removeLists);// 从原始集合中移除已经消失的对象
	}

	public MyWaveRing(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyWaveRing(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 遍历集合画出所有圆
		for (Wave wave : mWaveLists) {
			canvas.drawCircle(wave.x, wave.y, wave.radius, wave.paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			addPoint((int) event.getX(), (int) event.getY());
			break;
		}
		return true;//表示处理这个事件
	}

	/**
	 * 添加一个点
	 * 
	 * @param x
	 * @param y
	 */
	private void addPoint(int x, int y) {
		if (mWaveLists.isEmpty()) {// 第一次
			addWave(x, y);
			handler.sendEmptyMessage(0);
		} else {// 不是第一次不用发消息了,但是要把wave对象加入集合
			Wave lastWave = mWaveLists.get(mWaveLists.size() - 1);// 拿到最后一个圆环
			if (Math.abs(x - lastWave.x) > MIN_DIS
					|| Math.abs(y - lastWave.y) > MIN_DIS) {// 要添加的圆和上一个圆的是否达到一定值
				addWave(x, y);
			}
		}
	}

	/**
	 * 添加一个波浪
	 */
	private void addWave(int x, int y) {
		Wave wave = new Wave();
		wave.x = x;
		wave.y = y;
		Paint paint = new Paint();
		paint.setStrokeWidth(wave.radius / 3);
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);// 画圆环
		paint.setAntiAlias(true);// 去锯齿
		paint.setAlpha(255);// 完全不透明

		// 设置随机颜色
		// Random random=new Random();
		// random.nextInt(5);//0,1,2,3,4
		int colorIndex = (int) (Math.random() * 6);// 这样的好处是不用new对象
		paint.setColor(mColors[colorIndex]);
		wave.paint = paint;
		mWaveLists.add(wave);
	}

	public class Wave {
		public int x;
		public int y;
		public int radius;
		public Paint paint;
	}

}

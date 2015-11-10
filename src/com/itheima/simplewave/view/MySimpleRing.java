package com.itheima.simplewave.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class MySimpleRing extends View {

	private Paint mPaint;
	private Paint mPaintCicle;
	public MySimpleRing(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public MySimpleRing(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public MySimpleRing(Context context) {
		super(context);
		initView();
	}
	private void initView() {
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		mPaint.setStrokeWidth(5);
		mPaintCicle=new Paint();
		mPaintCicle.setAntiAlias(true);//去除锯齿
		mPaintCicle.setStyle(Style.STROKE);//空心圆
		mPaintCicle.setStrokeWidth(5);//线的宽度
		mPaintCicle.setColor(Color.RED);
		
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(200, 200);//确定组件大小
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < 200; i+=20) {
			canvas.drawLine(0, i, 200, i, mPaint);//画线
			canvas.drawLine(i, 0, i, 200, mPaint);
		}
		//可以移动画布
		canvas.translate(-20, -20);//向左上方移动画布
		canvas.drawCircle(getWidth()/2, getHeight()/2, 50, mPaintCicle);//画圆
	}

}

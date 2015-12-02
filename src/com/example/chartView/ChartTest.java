package com.example.chartView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class ChartTest extends View {

	private Paint paintBg;
	private Paint linePaint;
	private Paint bgLinePaint;
	// 圆心画笔
	Paint pointPaint;
	// 圆环画笔
	Paint ringPaint;
	// 文字画笔
	Paint textPaint;

	private float[] values;
	private int values_count = 0;
	private float maxvalue = 0;

	private int width;
	private int heigth;

	private int paddingBottom = 50;// 底部预留

	// =============================================================背景颜色
	LinearGradient lg;

	public ChartTest(Context context) {
		this(context, null);
	}

	public ChartTest(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ChartTest(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPaint();
	}

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		paintBg = new Paint();
		paintBg.setColor(Color.GREEN);
		paintBg.setAntiAlias(true);

		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.BLUE);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setStrokeWidth(10);

		bgLinePaint = new Paint();
		bgLinePaint.setAntiAlias(true);
		bgLinePaint.setColor(Color.argb(75, 255, 255, 255));
		bgLinePaint.setStyle(Paint.Style.STROKE);
		bgLinePaint.setStrokeWidth(2);

		pointPaint = new Paint();
		pointPaint.setAntiAlias(true);
		pointPaint.setColor(Color.rgb(70, 193, 242));
		pointPaint.setStyle(Paint.Style.FILL);

		ringPaint = new Paint();
		ringPaint.setAntiAlias(true);
		ringPaint.setColor(Color.WHITE);
		ringPaint.setStyle(Paint.Style.STROKE);
		ringPaint.setStrokeWidth(6);

		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(15);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (getMeasuredWidth() > 0 || getMeasuredHeight() > 0) {
			this.width = getMeasuredWidth();
			this.heigth = getMeasuredHeight();
		}
	}

	Path path = new Path();

	List<Point> mPoints = new ArrayList<Point>();

	int margin;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		margin=width/values.length;
		// 画出屏幕底部水平的线
		path.moveTo(0, heigth - paddingBottom);
		path.lineTo(width, heigth - paddingBottom);
		canvas.drawPath(path, linePaint);

		// 回退到左下角，待画
		path.moveTo(0, heigth - paddingBottom);

		if (lg == null) {
			lg = new LinearGradient(0, heigth - paddingBottom, 0, heigth,
					Color.argb(255, 70, 193, 242), Color.argb(255, 41, 28, 86),
					Shader.TileMode.CLAMP);
		}
		paintBg.setShader(lg);

		// 绘制底部的长方形
		Rect rect = new Rect(0, heigth - paddingBottom, width, heigth);
		canvas.drawRect(rect, paintBg);

		if (mPoints.size() != values.length) {
			mPoints.clear();
			for (int i = 0; i < values.length; i++) {
				Point point = new Point(margin * (i + 1), heigth
						- paddingBottom);
				mPoints.add(point);
			}
		}

		if (mPoints != null && mPoints.size() != 0) {
			for (int i = 0; i < mPoints.size(); i++) {
				if (i != mPoints.size() - 1) {
					path.reset();
					path.moveTo(mPoints.get(i).x, mPoints.get(i).y);
					path.lineTo(mPoints.get(i).x, 0);
					canvas.drawPath(path, bgLinePaint);
				}
			}

			// 画点
			for (int p = 0; p < mPoints.size(); p++) {
				int pointX = mPoints.get(p).x;
				int pointY = (int) (heigth - (heigth * (1 - values[p])));

				int nextPointX = 0;
				int nextPointY = 0;
				if (p != mPoints.size() - 1) {
					nextPointX = mPoints.get(p + 1).x;
					nextPointY = (int) (heigth - (heigth * (1 - values[p + 1])));
				} else {
					nextPointX = mPoints.get(p).x;
					nextPointY = (int) (heigth - (heigth * (1 - values[p])));
				}

				// 先连线，再画点，可以防止被看到断开的部分←_←
				// 连线
				linePaint.setStrokeWidth(3);
				path.reset();
				path.moveTo(pointX, pointY);
				path.lineTo(nextPointX, nextPointY);
				canvas.drawPath(path, linePaint);

				canvas.drawCircle(pointX, pointY, 10, pointPaint);
				// 圆环
				canvas.drawCircle(pointX, pointY, 10, ringPaint);
				String text = "" + values[p] + "f";

				float xOffset = textPaint.measureText(text) / 2;
				float yOffset = textPaint.measureText(text) * 2 / 3;

				// 文字
				canvas.drawText(text, pointX + xOffset, pointY - yOffset,
						textPaint);

			}
		}

	}

	/**
	 * =============================================================
	 * setter和getter
	 */

	/** 设置数据 */
	public void setValues(float[] values, float maxvalue) {
		this.values = values;
		this.maxvalue = maxvalue;
		invalidate();
	}
}

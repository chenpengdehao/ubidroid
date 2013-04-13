package org.alljoyn.bus.samples.simpleservice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class CircleView extends View 
{
	public float x;
	public float y;
	public int r;
	private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public CircleView(Context context, float x, float y, int r, int color, float strokeWidth)
	{
		super(context);
		//0xFF00FF00
		mPaint.setColor(color);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(strokeWidth);
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawCircle(x, y, r, mPaint);
	}
}

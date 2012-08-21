package com.metacube.noteprise.util.richtexteditor;

import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerDialog extends Dialog {

    public interface OnColorChangedListener 
    {
        void colorChanged(int color);
    }

    private OnColorChangedListener mListener;

    private static class ColorPickerView extends View 
    {
        private Paint mPaint;
        private OnColorChangedListener mListener;

        ColorPickerView(Context c, OnColorChangedListener l, int color) 
        {
            super(c);
            mListener = l;

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Style.FILL);
        }

        @Override 
        protected void onDraw(Canvas canvas) 
        {        
            mPaint.setColor(Color.BLACK);
            canvas.drawRect(5, 0, 30, 25, mPaint);
            
            mPaint.setColor(Color.WHITE);
            canvas.drawRect(35, 0, 60, 25, mPaint);
            
            mPaint.setColor(Color.RED);
            canvas.drawRect(65, 0, 90, 25, mPaint);
            
            mPaint.setColor(Color.GREEN);
            canvas.drawRect(95, 0, 120, 25, mPaint);
            
            mPaint.setColor(Color.BLUE);
            canvas.drawRect(125, 0, 150, 25, mPaint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
        {
            setMeasuredDimension(155, 30);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) 
        {
        	if (event.getAction() == MotionEvent.ACTION_DOWN)
        	{
	        	int c = 0;
	        	
	        	if(event.getX() >= 5 && event.getX() <= 30)
	        		c = Color.BLACK;
	        	else if(event.getX() >= 35 && event.getX() <= 60)
	        		c = Color.WHITE;
	        	else if(event.getX() >= 65 && event.getX() <= 90)
	        		c = Color.RED;
	        	else if(event.getX() >= 95 && event.getX() <= 120)
	        		c = Color.GREEN;
	        	else if(event.getX() >= 125 && event.getX() <= 150)
	        		c = Color.BLUE;
	        	
	            mListener.colorChanged(c);
        	}

            return true;
        }
    }

    public ColorPickerDialog(Context context,
                             OnColorChangedListener listener,
                             int initialColor) {
        super(context);

        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(int color) {
                mListener.colorChanged(color);
                dismiss();
            }
        };

        setContentView(new ColorPickerView(getContext(), l, 0));
        setTitle("Pick a color:");
    }
}

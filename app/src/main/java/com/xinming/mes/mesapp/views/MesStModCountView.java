package com.xinming.mes.mesapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class MesStModCountView extends View {

    private TextPaint mTextPaint;
    private Paint mLinePaint;
    private float mTextWidth;
    private float mTextHeight;

    public MesStModCountView(Context context) {
        super(context);
        init(null, 0);
    }

    public MesStModCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MesStModCountView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attribute

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mLinePaint = new Paint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(5);
        canvas.drawLine(0,0,0,contentHeight,mLinePaint);
        canvas.drawLine(0,contentHeight/4,contentWidth,contentHeight/4,mLinePaint);
        canvas.drawLine(0,contentHeight/2,contentWidth,contentHeight/2,mLinePaint);
        canvas.drawLine(0,contentHeight/4*3,contentWidth,contentHeight/4*3,mLinePaint);
        // Draw the text.
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStrokeWidth(5);
        mTextPaint.setTextSize(60);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        float leftX =  contentWidth/10;
        float leftY = contentHeight /16;
        float offsetY = 30;
        canvas.drawText("I:E",
                leftX,
                (float)(leftY*2.5),
                mTextPaint);

        canvas.drawText("MV",
                leftX,
                leftY*6,
                mTextPaint);

        canvas.drawText("L/min",
                leftX,
                leftY*7+offsetY,
                mTextPaint);

        canvas.drawText("Leak",
                leftX,
                leftY*10,
                mTextPaint);

        canvas.drawText("L/min",
                leftX,
                leftY*11+offsetY,
                mTextPaint);

        canvas.drawText("SpO2",
                leftX,
                leftY*14,
                mTextPaint);

        canvas.drawText("%",
                leftX,
                leftY*15+offsetY,
                mTextPaint);

//        float rightX = contentWidth;
//        float rightY = contentHeight /16;
//        mTextPaint.setColor(Color.GREEN);
//        mTextPaint.setTextAlign(Paint.Align.RIGHT);
//        mTextPaint.setTextSize(100);
//        canvas.drawText("1:1.7",
//                rightX,
//                rightY*3,
//                mTextPaint);
//
//        canvas.drawText("30.0",
//                rightX,
//                rightY*7,
//                mTextPaint);
//
//        canvas.drawText("19",
//                rightX,
//                rightY*11,
//                mTextPaint);
//
//        canvas.drawText("--",
//                rightX,
//                rightY*15,
//                mTextPaint);
        }


}

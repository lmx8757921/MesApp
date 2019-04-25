package com.xinming.mes.mesapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.xinming.mes.mesapp.R;

/**
 * TODO: document your custom view class.
 */
public class MesCircularView extends View {
    private String unit; // 单位
    private String type; // 显示类型
    private int unitSize;//单位结果显示文字大小
    private int typeSize;//显示类型结果显示文字大小
    private int offsetX;//图形X轴坐标偏移量
    private int offsetY;//图形Y轴坐标偏移量
    private Paint cPaint;
    private Paint mTextPaint;
    public MesCircularView(Context context) {
        super(context);
        init(null, 0);
    }

    public MesCircularView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MesCircularView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MesCircularView, defStyle, 0);

        unit = a.getString(
                R.styleable.MesCircularView_unit);

        type = a.getString(
                R.styleable.MesCircularView_type);


        unitSize = a.getInt(R.styleable.MesCircularView_unitSize,20);
        typeSize = a.getInt(R.styleable.MesCircularView_typeSize,100);
        offsetX = a.getInt(R.styleable.MesCircularView_offsetX,0);
        offsetY = a.getInt(R.styleable.MesCircularView_offsetY,0);
        cPaint = new Paint();
        mTextPaint = new Paint();
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

        // Draw the text.
        cPaint.setStyle(Paint.Style.STROKE);
        cPaint.setColor(Color.WHITE);
        cPaint.setStrokeWidth(10);

        int r = (contentWidth > contentHeight ) ? contentHeight/3 : contentHeight/3;
        canvas.drawCircle(contentWidth/2 + offsetX,contentHeight/2+offsetY,r,cPaint);
        mTextPaint.setColor(Color.WHITE);//设置画笔颜色
        mTextPaint.setStrokeWidth(30);
        if( type != null && !type.isEmpty()){
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(typeSize) ;//设置绘制文本时的文字大小
            canvas.drawText(type,
                    contentWidth/2+offsetX,
                    contentHeight/2 + r + typeSize +offsetY,
                    mTextPaint);
        }

        if( unit != null && !unit.isEmpty()){
            mTextPaint.setTextSize(unitSize) ;//设置绘制文本时的文字大小
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(unit,
                    contentWidth/2 + offsetX,
                    contentHeight/2 + r - unitSize/2 +offsetY,
                    mTextPaint);
        }
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(int unitSize) {
        this.unitSize = unitSize;
    }

    public int getTypeSize() {
        return typeSize;
    }

    public void setTypeSize(int typeSize) {
        this.typeSize = typeSize;
    }


    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

}

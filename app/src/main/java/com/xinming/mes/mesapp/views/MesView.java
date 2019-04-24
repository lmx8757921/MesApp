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
 * MES special view class.
 */
public class MesView extends View {

    private String unit; // 单位
    private String type; // 显示类型
    private String result; //界面动态结果
    private int resultSize;//结果显示文字大小
    private Paint cPaint;
    private Paint mTextPaint;

    public MesView(Context context) {
        super(context);
        init(null, 0);
    }

    public MesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MesView, defStyle, 0);

        unit = a.getString(
                R.styleable.MesView_unit);

        type = a.getString(
                R.styleable.MesView_type);

        result = a.getString(
                R.styleable.MesView_result);

        resultSize = a.getInt(R.styleable.MesView_resultSize,80);

//        unitSize = a.getInt(R.styleable.MesView_unitSize,50);
//
//        typeSize = a.getInt(R.styleable.MesView_typeSize,100);
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
//        canvas.drawText(mExampleString,
//                paddingLeft + (contentWidth - mTextWidth) / 2,
//                paddingTop + (contentHeight + mTextHeight) / 2,
//                mTextPaint);
        cPaint.setStyle(Paint.Style.STROKE);
        cPaint.setColor(Color.WHITE);
        cPaint.setStrokeWidth(15);
        canvas.drawCircle(contentWidth/2,contentHeight/2,contentWidth/6,cPaint);

        mTextPaint.setColor(Color.WHITE);//设置画笔颜色
        if(result != null && !result.isEmpty()){
            mTextPaint.setTextSize(resultSize) ;//设置绘制文本时的文字大小
            canvas.drawText(result,
                    contentWidth/2 -resultSize /2,
                     (contentHeight) / 2 +resultSize/4 ,
                    mTextPaint);

        }

//        if( type != null && !type.isEmpty()){
//            mTextPaint.setTextSize(typeSize) ;//设置绘制文本时的文字大小
//            mTextPaint.setTextAlign(Paint.Align.CENTER);
//            canvas.drawText(type,
//                    contentWidth/2,
//                    contentHeight/2 +contentWidth/6 +typeSize  ,
//                    mTextPaint);
//        }
//
//        if( unit != null && !unit.isEmpty()){
//            mTextPaint.setTextSize(unitSize) ;//设置绘制文本时的文字大小
//            mTextPaint.setTextAlign(Paint.Align.CENTER);
//            canvas.drawText(unit,
//                    contentWidth/2 ,
//                    contentHeight/2 +contentWidth/6 -unitSize ,
//                    mTextPaint);
//        }
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


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getResultSize() {
        return resultSize;
    }

    public void setResultSize(int resultSize) {
        this.resultSize = resultSize;
    }


}

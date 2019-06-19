package com.xinming.mes.mesapp.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.entity.ChartData;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;

/**
 * Created by Administrator on 2019/6/18.
 */

public class MesFPChartSpecialView extends View {
    private String unit; //单位 cmh2o,kap,hpa
    private String type; //类型 P,F
    private int max;//坐标最大值
    private int min;//坐标最小值
    private int count4x = 10;//x轴的刻度个数
    private float cXLength;//x轴的实际长度
    private float cYLength;//y轴的实际长度
    private int contentWidth;//视图宽度
    private int contentHeight;//视图高度
    private float x0 = 0;//零点X坐标
    private float xEnd = 0;//终点X坐标
    private float y0 = 0;//零点Y坐标

    //直线坐标画笔
    private Paint mLinePaint = new Paint();
    //文字画笔
    private Paint mTextPaint = new Paint();
    //曲线画笔
    private Paint mCurvePaint = new Paint();

    //流量显示范围模板，默认选第0个
    private int[][] flow_templetes = new int[][]{{120,-30},{200,-200}};
    //压力显示范围模板，默认选第0个，单位kpa
    private int[][] pressure_kpa_templetes = new int[][]{{12,0},{200,0}};
    //压力显示范围模板，默认选第0个，单位cmh2o,hpa
    private int[][] pressure_other_templetes = new int[][]{{120,0},{2000,0}};
    //当前模板
    private int[] currentTemplete = null;
    //默认的模板
    private int[] defaultTemplete = null;
    //存放前十秒图表数据集合
    private List<ChartData> last10Datas = new ArrayList<>();
    //存放现在前十秒图表数据集合
    private List<ChartData> current10Datas = new ArrayList<>();

    //最后一次画点数据数据
    //private ChartData lastData = null;
    //最大最小值点的坐标0:max x,1:max y,2:min x,3:min y,
    private float[] xys = new float[4];

    public MesFPChartSpecialView(Context context) {
        super(context);
        init(null, 0);
    }

    public MesFPChartSpecialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MesFPChartSpecialView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * 添加数据
     */
    public void addData(ChartData data){
        ChartData lastData = null;
        if(current10Datas.size() != 0){
            lastData = current10Datas.get(current10Datas.size() -1);
        }
        //首先计算步长
        calcStepX(data,lastData);
        //整理数据
        reOrganizeDatas(data);
        //判断数据范围，越界使用新模板，否则使用老模板
        judgeDatasScope(last10Datas);
        judgeDatasScope(current10Datas);
    }

    /**
     * 重置数据
     */
    public void resetData(){
        last10Datas.clear();
        current10Datas.clear();
    }

    private void init(AttributeSet attrs, int defStyle)  {

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MesFPChartView, defStyle, 0);

        unit = a.getString(
                R.styleable.MesFPChartView_unit);

        type = a.getString(
                R.styleable.MesFPChartView_type);
        //初始化设置默认的模板
        defaultTemplete = getTemplete(0);
        currentTemplete = defaultTemplete;
        max = currentTemplete[0];
        min = currentTemplete[1];

        mTextPaint.setTextSize(30);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(5);

        mCurvePaint.setStyle(Paint.Style.STROKE);
        mCurvePaint.setAntiAlias(true);
        mCurvePaint.setStrokeWidth(6);
    }


    /**
     * 获取图表刻度模板
     * @param inx
     * @return
     */
    private int[] getTemplete(int inx){
        if("F".equals(type)){
            return flow_templetes[inx];
        } else if ("P".equals(type)) {
            if("kpa".equals(unit)){
                return  pressure_kpa_templetes[inx];
            }else if("cmh2o".equals(unit) || "hpa".equals(unit)){
                return pressure_other_templetes[inx];
            }else{
                throw new RuntimeException("图表的属性type:" + type +"设置错误，请选择kpa，cmh2o，hpa！");
            }
        }else{
            throw new RuntimeException("图表的属性type:" + type +"设置错误，请选择P或者F！");
        }


    }

    /**
     * 重新设置模板
     * @param t
     */
    private void resetTemplete(int[] t){
        currentTemplete = t;
        max = currentTemplete[0];
        min = currentTemplete[1];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;
        cXLength = (float)(contentWidth * 0.9 - mTextPaint.getTextSize()*2);
        cYLength = contentHeight - mTextPaint.getTextSize()*2 ;
        //画坐标
        drawCoordinate(canvas);
        //处理曲线，画图
        exccuteChart(canvas);
    }

    /**
     * 画坐标系
     * @param canvas
     */
    private void drawCoordinate(Canvas canvas){

        float startX = (float)(contentWidth *0.1);
        float startY = (float)(contentHeight - mTextPaint.getTextSize());

        //画纵坐标轴Y
        canvas.drawLine(startX,startY,startX, mTextPaint.getTextSize(),mLinePaint);
        int minAbs = Math.abs(min);
        if(min < 0){
            y0 = (float)(startY - (minAbs * cYLength)/(max - min));
        }else{
            y0 = startY;
        }
        //画横坐标轴X
        x0 = startX;
        xEnd = startX + cXLength;
        canvas.drawLine(x0,y0,contentWidth,y0,mLinePaint);
        canvas.drawText("0",startX-mTextPaint.getTextSize(),y0,mTextPaint);

        //写类型和单位值文字在坐标轴左侧
        canvas.drawText(type,(float) 40,mTextPaint.getTextSize(),mTextPaint);
        canvas.drawText(unit,(float) (contentWidth * 0.08),(float)(cYLength/4*3),mTextPaint);

        Paint dottedLinePaint = new Paint();
        dottedLinePaint.setStyle(Paint.Style.STROKE);
        dottedLinePaint.setColor(Color.BLACK);
        dottedLinePaint.setStrokeWidth(3);
        //虚线效果
        PathEffect effects = new DashPathEffect(new float[] { 10, 8, 10, 8 }, 0);
        dottedLinePaint.setPathEffect(effects);
//
        //Y轴正方向延X轴画两条虚线
        Path path = new Path();
        float y = (float) (y0-(y0 - mTextPaint.getTextSize())/2);
        path.moveTo(startX,y );
        path.lineTo((float)contentWidth,y);
        //第一条虚线在X轴上方
        canvas.drawPath(path, dottedLinePaint);
        //写数字,设定一个宽度
        float textWidth = mTextPaint.getTextSize();
        canvas.drawText(String.valueOf(max/2),startX-textWidth,y,mTextPaint);

        y =  (float) (mTextPaint.getTextSize());
        path.moveTo(startX,y );
        path.lineTo((float)contentWidth,y);
        canvas.drawPath(path, dottedLinePaint);
        //写数字
        canvas.drawText(String.valueOf(max),startX-textWidth,y,mTextPaint);
        xys[0] = startX-textWidth;
        xys[1] = y;
        //Y轴负方向延X轴画一条虚线
        if(min < 0 ){
            y = (float) (y0 + (minAbs * cYLength)/(max - min));
            path.moveTo(startX,y );
            path.lineTo((float)contentWidth,y);
            canvas.drawPath(path, dottedLinePaint);
            //写数字
            canvas.drawText(String.valueOf(min),startX-textWidth,y,mTextPaint);
            xys[2] = startX-textWidth;
            xys[3] = y;
        }

        //X轴正方向延Y轴画count4x条虚线
        float x = startX;
        for(int i =1;i <=count4x;i++){
            double graduation = (double)((cXLength)/count4x);
            x +=graduation;
            path.moveTo(x,startY);
            path.lineTo(x,mTextPaint.getTextSize());
            canvas.drawPath(path, dottedLinePaint);
            //写数字
            canvas.drawText(String.valueOf(i),x-mTextPaint.getTextSize(),y0+mTextPaint.getTextSize(),mTextPaint);
        }




    }

    /**
     * 写最大最小值
     */
    private void drawMaxMinText(Canvas canvas){
        //写数字
        canvas.drawText(String.valueOf(min),xys[0],xys[1],mTextPaint);
        if(min < 0 ){
            //写数字
            canvas.drawText(String.valueOf(min),xys[2],xys[3],mTextPaint);
        }
    }

    /**
     * 计算两点之间的宽度
     * @param data
     */
    private void calcStepX(ChartData data,ChartData lastData){

        if(lastData != null){
            Date currentTime = data.getTime();
            Date lastTime = lastData.getTime();
            //两个时间相隔的秒数
            float diffSecond = ((float)(currentTime.getTime() -lastTime.getTime()))/1000;
            data.setStepX(diffSecond/count4x*cXLength);
        }else{
            data.setStepX(0);//第一个点没有宽度
        }
    }

    /**
     * 计算真实坐标点
     */
    private void calcRealCoordinate(ChartData data,ChartData lastData,boolean isFoward){
        float x = 0;
        float y = 0;
        double d = data.getData();
        y = calcRealY(d);
        data.setStartY(y);
        if(lastData != null){
            if(isFoward){//正向计算
                x = lastData.getStartX() + data.getStepX();//使用前一点数据的步长
            }else{
                x = lastData.getStartX() - lastData.getStepX();//使用后一点数据的步长
            }
            data.setStartX(x);
        }else{
            if(isFoward) {//正向计算
                data.setStartX(x0);
            }else{
                data.setStartX(xEnd);
            }
        }
    }

    /**
     * 计算真实的Y坐标
     * @param d
     * @return
     */
    private float calcRealY(double d){
        return (float)(y0 - d * cYLength/(max -min) );
    }

    /**
     * 处理曲线图
     */
    private void exccuteChart(Canvas canvas){
        //画图
        drawChart(canvas);
    }

    /**
     * 画图
     * @param canvas
     */
    private void drawChart(Canvas canvas){
        if(last10Datas.size() > 0){
            drawChartReverse(canvas);
        }else{
            drawChartFoward(canvas);
        }
    }
    /**
     * 正向画曲线
     * @param canvas
     */
    private void drawChartFoward(Canvas canvas){
        ChartData lastData = null;
        ChartData d = null;
        Logger.d("drawChartFoward current10Datas.size="+current10Datas.size());
        for(int i =0; i< current10Datas.size();i++){
            d = current10Datas.get(i);
            mCurvePaint.setColor(d.getColor());
            //计算真实坐标
            calcRealCoordinate(d,lastData,true);
            if(lastData != null){
                canvas.drawLine(lastData.getStartX(),lastData.getStartY(),d.getStartX(),d.getStartY(),mCurvePaint);
            }
            lastData = d;

        }
    }
    /**
     * 反向画曲线
     * @param canvas
     */
    private void drawChartReverse(Canvas canvas) {
        Logger.d("drawChartReverse current10Datas.size="+current10Datas.size());
        Logger.d("drawChartReverse last10Datas.size="+last10Datas.size());
        ChartData lastData = null;
        for(int i =current10Datas.size()-1; i>=0 ;i--){
            ChartData d = current10Datas.get(i);
            if (lastData != null) {
                //计算真实坐标
                calcRealCoordinate(d,lastData,false);
                mCurvePaint.setColor(lastData.getColor());//反向的使用上一次的点的颜色
                canvas.drawLine(lastData.getStartX(), lastData.getStartY(), d.getStartX(), d.getStartY(), mCurvePaint);
            }
            lastData = d;
        }
        if(lastData != null){
            //最后画X0点与最后一个点的线，直线
            mCurvePaint.setColor(lastData.getColor());
            if(lastData.getStartX() != x0){
                canvas.drawLine(lastData.getStartX(), lastData.getStartY(), x0, lastData.getStartY(), mCurvePaint);
            }
        }

        //画上十秒剩余可用点的线
        lastData = null;
        for(int i =last10Datas.size()-1; i>=0 ;i--){
            ChartData d = last10Datas.get(i);
            //计算真实坐标
            if (lastData != null) {
                mCurvePaint.setColor(lastData.getColor());//反向的使用上一次的点的颜色
                canvas.drawLine(lastData.getStartX(), lastData.getStartY(), d.getStartX(), d.getStartY(), mCurvePaint);
            }
            lastData = d;
        }

    }



    /**
     * 判断数据范围，越界使用新模板，否则使用老模板
     */
    private void judgeDatasScope(List<ChartData> datas){
        for(ChartData d : datas){
            double v = d.getData();
            //当前模板不是默认模板，判断是否可以恢复默认模板
            if(defaultTemplete != currentTemplete){
                //在默认模板范围的使用默认的
                if(v<=defaultTemplete[0] && v>= defaultTemplete[1]){
                    continue;
                }else{
                    //超出了默认范围直接退出，继续使用当前模板
                    return;
                }

            }else{
                //否则判断是否需要新模板
                if(v>max || v< min){
                    //越界了使用新模板
                    int[] t = getTemplete(1);
                    resetTemplete(t);
                    return;
                }
            }
        }

        //判断后默认模板满足要求，重置
        resetTemplete(defaultTemplete);

    }

    /**
     * 整理数据
     */
    private void reOrganizeDatas(ChartData currentData){
        float step = currentData.getStepX();
        boolean needDelete = false;
        for(ChartData d: current10Datas){
            step += d.getStepX();
            if(step > cXLength){
                //如果点数超了，把当前所有点移到上一个十秒点的集合
                last10Datas.clear();
                last10Datas.addAll(current10Datas);
                current10Datas.clear();
                currentData.setStepX(0);//步长清零，第一个点
                break;
            }
        }

        current10Datas.add(currentData);
        //累加步长，多余的点从上一十秒数据中删除
        float x = x0;
        for(ChartData d: current10Datas){
            x += d.getStepX();
        }
        Deque<ChartData> tempDatas = new ArrayDeque<>();
        if(last10Datas.size() > 1){
            int i = last10Datas.size()-1;
            ChartData d = last10Datas.get(i);
            float x1 = d.getStartX();
            tempDatas.push(d);
            for(i = i -1; i>=0 ;i--){
                x1 -= d.getStepX();
                if(x1 < x){
                    //清除多余的元素
                    Logger.d("current10Datas="+current10Datas.size());
                    Logger.d("last10Datas="+last10Datas.size());
                    Logger.d("X="+x);
                    Logger.d("X1="+x1);
                    Logger.d("getStepX="+d.getStepX());
                    Logger.d("i="+i);
                    needDelete = true;
                    break;
                }
                d = last10Datas.get(i);
                tempDatas.push(d);
            }

            if(needDelete){
                last10Datas.clear();
                last10Datas.addAll(tempDatas);
            }

            if(last10Datas.size() > 1){
                currentData.setStartX(last10Datas.get(0).getStartX());
                currentData.setStartY(calcRealY(currentData.getData()));
            }else{
                //小于2个点清空
                last10Datas.clear();
            }
        }else{
            last10Datas.clear();
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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getCount4x() {
        return count4x;
    }

    public void setCount4x(int count4x) {
        this.count4x = count4x;
    }
}

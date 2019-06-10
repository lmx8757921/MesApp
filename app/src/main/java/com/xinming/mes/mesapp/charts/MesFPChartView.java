package com.xinming.mes.mesapp.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.xinming.mes.mesapp.R;
import com.xinming.mes.mesapp.entity.ChartData;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;

/**
 * 绘制MES曲线图的view
 */
public class MesFPChartView extends View {
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
    private boolean refreshText= false;//是否刷新最大最小值
    private boolean isXOutOfBound = false;//X轴点数是否溢出标志

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
    //图表数据集合
    private List<ChartData> datas = new ArrayList<>();
    //最后一次画点数据数据
    private ChartData lastData = null;
    //最大最小值点的坐标0:max x,1:max y,2:min x,3:min y,
    private float[] xys = new float[4];

    public MesFPChartView(Context context) {
        super(context);
        init(null, 0);
    }

    public MesFPChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MesFPChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * 添加数据
     */
    public void addData(ChartData data){
        calcStepX(data);
        datas.add(data);
    }

    /**
     * 重置数据
     */
    public void resetData(){
        datas.clear();
        lastData = null;
        refreshText= false;//是否刷新最大最小值
        isXOutOfBound = false;//X轴点数是否溢出标志
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

        mTextPaint.setTextSize(20);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

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
        cXLength = (float)(contentWidth * 0.8 - mTextPaint.getTextSize()*2);
        cYLength = contentHeight - mTextPaint.getTextSize()*2 ;
        //判断数据范围，越界使用新模板，否则使用老模板
        judgeDatasScope();
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
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStrokeWidth(5);
        float startX = (float)(contentWidth *0.2);
        float startY = (float)(cYLength - mTextPaint.getTextSize());

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
        canvas.drawText("0",startX-mTextPaint.getTextSize() * String.valueOf(max).length(),y0,mTextPaint);

        //写类型和单位值文字在坐标轴左侧
        canvas.drawText(type,(float)(contentWidth *0.1),mTextPaint.getTextSize(),mTextPaint);

        canvas.drawText(unit,(float)(contentWidth *0.1),(float)(cYLength/2),mTextPaint);

//        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(1);
        //虚线效果
        PathEffect effects = new DashPathEffect(new float[] { 10, 8, 10, 8 }, 0);
        mLinePaint.setPathEffect(effects);
//
        //Y轴正方向延X轴画两条虚线
        Path path = new Path();
        float y = (float) (y0-(y0 - mTextPaint.getTextSize())/2);
        path.moveTo(startX,y );
        path.lineTo((float)contentWidth,y);
        //第一条虚线在X轴上方
        canvas.drawPath(path, mLinePaint);
        //写数字,设定一个宽度
        float textWidth = mTextPaint.getTextSize() * String.valueOf(max).length();
        canvas.drawText(String.valueOf(max/2),startX-textWidth,y,mTextPaint);

        y =  (float) (mTextPaint.getTextSize());
        path.moveTo(startX,y );
        path.lineTo((float)contentWidth,y);
        canvas.drawPath(path, mLinePaint);
        //写数字
        canvas.drawText(String.valueOf(max),startX-textWidth,y,mTextPaint);
        xys[0] = startX-textWidth;
        xys[1] = y;
        //Y轴负方向延X轴画一条虚线
        if(min < 0 ){
            y = (float) (y0 + (minAbs * cYLength)/(max - min));
            path.moveTo(startX,y );
            path.lineTo((float)contentWidth,y);
            canvas.drawPath(path, mLinePaint);
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
            canvas.drawPath(path, mLinePaint);
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
    private void calcStepX(ChartData data){
        if(datas.size() > 0){
            lastData = datas.get(datas.size()-1);
            Date currentTime = data.getTime();
            Date lastTime = lastData.getTime();
            //两个时间相隔的秒数
            float diffSecond = ((float)(currentTime.getTime() -lastTime.getTime()))/1000;
            data.setStepX(diffSecond/count4x*cXLength);
        }else{
            data.setStepX(cXLength/count4x);
        }
    }

    /**
     * 计算真实坐标点
     */
    private void calcRealCoordinate(ChartData data,boolean isFoward){
        float x = 0;
        float y = 0;
        int d = data.getData();
        y = y0 - d * cYLength/(max -min) ;
        data.setStartY(y);
        if(lastData != null){
            if(isFoward){//正向计算
                x = lastData.getStartX() + data.getStepX();
            }else{
                x = lastData.getStartX() - data.getStepX();
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
     * 处理曲线图
     */
    private void exccuteChart(Canvas canvas){
        if(datas.size() > 0){
            //整理要输出的曲线点
            Deque<ChartData> stack = reOrganizeDatas();
            //画图
            drawChart(canvas,stack);
        }
    }

    /**
     * 画图
     * @param canvas
     */
    private void drawChart(Canvas canvas,Deque<ChartData> stack){

        if(isXOutOfBound){
            drawChartReverse(canvas,stack);
        }else{
            drawChartFoward(canvas,stack);
        }
//        //刷新最大最小值
//        if(refreshText){
//            //局部刷新时考虑
//            drawMaxMinText(canvas);
//        }
    }
    /**
     * 正向画曲线
     * @param canvas
     */
    private void drawChartFoward(Canvas canvas,Deque<ChartData> stack){
        lastData = null;
        ChartData d = stack.pollFirst();
        while(d != null){
            setPaintColor(d.getColor());
            //计算真实坐标
            calcRealCoordinate(d,true);
            if(lastData != null){
                canvas.drawLine(lastData.getStartX(),lastData.getStartY(),d.getStartX(),d.getStartY(),mCurvePaint);
            }
            lastData = d;
            d = stack.pollFirst();
        }
    }
    /**
     * 反向画曲线
     * @param canvas
     */
    private void drawChartReverse(Canvas canvas,Deque<ChartData> stack) {
        lastData = null;
        ChartData d = stack.pollLast();
        while (d != null) {
            //计算真实坐标
            calcRealCoordinate(d,false);
            if (lastData != null) {
                setPaintColor(lastData.getColor());//反向的使用上一次的点的颜色
                canvas.drawLine(lastData.getStartX(), lastData.getStartY(), d.getStartX(), d.getStartY(), mCurvePaint);
            }
            lastData = d;
            d = stack.pollLast();
        }
        //最后画X0点与最后一个点的线，直线
        setPaintColor("白色");
        canvas.drawLine(lastData.getStartX(), lastData.getStartY(), x0, lastData.getStartY(), mCurvePaint);
    }

    /**
     * 设置画笔颜色
     * @param color
     */
    private  void  setPaintColor(String color){
        if("绿色".equals(color)){
            mCurvePaint.setColor(Color.GREEN);
        }else if("红色".equals(color)){
            mCurvePaint.setColor(Color.RED);
        }else if("白色".equals(color)){
            mCurvePaint.setColor(Color.WHITE);
        }
    }

    /**
     * 判断数据范围，越界使用新模板，否则使用老模板
     */
    private void judgeDatasScope(){
        for(ChartData d : datas){
            int v = d.getData();
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
                    refreshText = true;
                    return;
                }
            }
        }

        //判断后默认模板满足要求，重置
        resetTemplete(defaultTemplete);
        refreshText = true;


    }

    /**
     * 整理数据
     */
    private Deque<ChartData> reOrganizeDatas(){
        float step = 0;
        Deque<ChartData> stack = new ArrayDeque<>();
        int i =  datas.size()-1;
        int count = datas.size();
        for(;i>=0;i--){
            ChartData d = datas.get(i);
            step += d.getStepX();
            if(step > cXLength){
                //如果点数超了，从最后一个点开始画
                isXOutOfBound = true;
                break;
            }else{
                stack.push(d);
            }
        }
        //清除多余的元素
        if(i > 0){
            for(int j =0;j< i; j++){
                datas.remove(0);
            }
        }
        return stack;

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

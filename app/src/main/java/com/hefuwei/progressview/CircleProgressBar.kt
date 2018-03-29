package com.hefuwei.progressview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by hefuwei on 2018/3/29.
 */
class CircleProgressBar(context:Context,set:AttributeSet?): View(context,set){

    private val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val smallestSize:Int = 150
    private var progress:Int = 0
    private val arcWidth:Int
    private val boundRect = Rect()

    init {
        val typeArray = context.obtainStyledAttributes(set,R.styleable.CircleProgressBar)
        setBackgroundColor(typeArray.getColor(R.styleable.CircleProgressBar_backgroundColor,Color.BLACK))
        paint.color = typeArray.getColor(R.styleable.CircleProgressBar_textColor,Color.WHITE)
        arcWidth = typeArray.getInt(R.styleable.CircleProgressBar_arcWidth,3)
        paint.strokeWidth = arcWidth.toFloat()
        typeArray.recycle()
    }

    constructor(context: Context):this(context, null)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = measuredWidth - paddingLeft - paddingRight
        val height = measuredHeight - paddingBottom - paddingTop
        if(canvas != null){
            paint.style = Paint.Style.FILL
            if(progress >= 10){
                drawText("$progress%",canvas)
            }else{
                drawText("0$progress%",canvas)
            }
            paint.color = Color.RED
            paint.style = Paint.Style.STROKE
            val hd = measuredHeight - paddingTop - paddingBottom - arcWidth
            val wd = measuredWidth - paddingLeft - paddingRight - arcWidth
            if(height > width){
                canvas.drawArc(paddingLeft.toFloat()+arcWidth/2,
                        (hd-wd+arcWidth)/2.toFloat(),
                        measuredWidth.toFloat()-arcWidth/2-paddingRight,
                        (hd+wd+arcWidth)/2.toFloat(),
                        135f,2.7f*progress.toFloat(),false,paint)
            } else {
                canvas.drawArc((wd-hd)/2.toFloat(),
                        paddingTop.toFloat()+arcWidth/2,
                        (wd+hd)/2.toFloat(),
                        measuredHeight.toFloat()-arcWidth/2-paddingBottom,
                        135f,2.7f*progress.toFloat(),false,paint)
            }
        }
    }

    private fun drawText(str:String, canvas:Canvas){
        paint.getTextBounds(str,0,str.length,boundRect) //注意drawText的起点是位于第一个字的左下
        canvas.drawText(str,(measuredWidth-boundRect.right+boundRect.left+paddingLeft-paddingRight)/2.toFloat()
                ,(measuredHeight+boundRect.bottom-boundRect.top-paddingTop-paddingBottom)/2.toFloat(),paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(smallestSize,smallestSize)
        }else if(widthMeasureSpec == MeasureSpec.AT_MOST){
            setMeasuredDimension(smallestSize,heightSize)
        }else if(heightMeasureSpec == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSize,smallestSize)
        }
        chooseRightTextSize()
    }

    private fun chooseRightTextSize(){
        var lastSize = 10
        for(i in 10..100){
            paint.textSize = i.toFloat()
            paint.getTextBounds("100%",0,"100%".length,boundRect)
            if((boundRect.right - boundRect.left)*1.9 < measuredWidth-paddingLeft-paddingRight &&
                    (boundRect.bottom - boundRect.top)*1.9 < measuredHeight-paddingTop-paddingBottom){
                lastSize = i
            }else{
                paint.textSize = lastSize.toFloat()
                break
            }
        }
    }

    fun setProgress(progress:Int){
        when {
            progress < 0 -> this.progress = 0
            progress > 100 -> this.progress = 100
            else -> this.progress = progress
        }
        invalidate()
    }

    fun getProgress():Int = progress
}
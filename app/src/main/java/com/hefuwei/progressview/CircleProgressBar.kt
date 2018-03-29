package com.hefuwei.progressview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by hefuwei on 2018/3/29.
 */
class CircleProgressBar(context:Context,set:AttributeSet?): View(context,set){

    private val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val smallestSize:Int = 150
    private var progress:Int = 0
    val color:Int = Color.RED
    private val boundRect = Rect()

    init {
        paint.strokeWidth = 20f
        paint.textSize = 50f
        paint.color = Color.WHITE
        setBackgroundColor(Color.BLACK)
    }

    constructor(context: Context):this(context, null)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(canvas != null){
            paint.style = Paint.Style.FILL
            if(progress >= 10){
                drawText("$progress%",canvas)
            }else{
                drawText("0$progress%",canvas)
            }
            paint.strokeWidth = 6f
            paint.color = Color.RED
            paint.style = Paint.Style.STROKE
            if(measuredHeight > measuredWidth){
                canvas.drawArc(3f,(measuredHeight-measuredWidth+6)/2f,measuredWidth.toFloat()-3, (measuredHeight+measuredWidth-6)/2.toFloat()
                        ,135f,2.7f*progress.toFloat(),false,paint)
            } else {
                canvas.drawArc((measuredWidth-measuredHeight+6)/2f,3f,(measuredWidth+measuredHeight-6)/2f, measuredHeight.toFloat()-3,
                        135f,2.7f*progress.toFloat(),false,paint)
            }
        }
    }

    private fun drawText(str:String, canvas:Canvas){
        paint.getTextBounds(str,0,str.length,boundRect) //注意drawText的起点是位于第一个字的左下
        canvas.drawText(str,(measuredWidth-boundRect.right+boundRect.left)/2.toFloat()
                ,(measuredHeight+boundRect.bottom-boundRect.top)/2.toFloat(),paint)
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
    }

    fun setProgress(progress:Int){
        when {
            progress < 0 -> this.progress = 0
            progress > 100 -> this.progress = 100
            else -> this.progress = progress
        }
        Log.d("CP","$progress")
        invalidate()
    }

    fun getProgress():Int = progress
}
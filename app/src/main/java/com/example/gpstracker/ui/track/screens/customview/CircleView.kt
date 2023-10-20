package com.example.gpstracker.ui.track.screens.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.gpstracker.R
import com.example.gpstracker.ui.track.TrackerState


class CircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var animValue: Int = 0
    private var currentState: TrackerState = TrackerState.OFF
    private val strokeWidth = 35

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()

        val rectF = RectF()
        rectF.set(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            (width - strokeWidth).toFloat(),
            (height - strokeWidth).toFloat()
        )

        when (currentState) {
            TrackerState.OFF -> {
                paint.color = context.resources.getColor(R.color.light_grey)
                canvas.drawArc(rectF, 0f, 360f, false, paint)
            }
            TrackerState.DISCONNECTED -> {
                paint.color = context.resources.getColor(R.color.red)
                canvas.drawArc(rectF, 0f, 360f, false, paint)
            }
            TrackerState.ON -> {
                paint.color = context.resources.getColor(R.color.colorAccent)
                canvas.drawArc(rectF, animValue.toFloat(), 80f, false, paint)
            }
        }
    }

    fun setState(newState: TrackerState) {
        currentState = newState
        invalidate()
    }

    fun setValue(animatedValue: Int) {
        animValue = animatedValue
        invalidate()
    }
}

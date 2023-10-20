package com.example.gpstracker.ui.track.screens.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.gpstracker.R
import com.google.firebase.database.annotations.Nullable


class CircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var animValue: Int = 0
    private var currentState: Int = STATE_GreyCircle // Default state
    private val strokeWidth = 35

    companion object {
        const val STATE_GreyCircle = 1
        const val STATE_RedCircle = 2
        const val STATE_Spinning = 3
    }

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
            STATE_GreyCircle -> {
                paint.color = context.resources.getColor(R.color.light_grey)
                canvas.drawArc(rectF, 0f, 360f, false, paint)
            }
            STATE_RedCircle -> {
                paint.color = context.resources.getColor(R.color.red)
                canvas.drawArc(rectF, 0f, 360f, false, paint)
            }
            STATE_Spinning -> {
                // Draw spinning circle with colorAccent
                paint.color = context.resources.getColor(R.color.colorAccent)
                canvas.drawArc(rectF, animValue.toFloat(), 80f, false, paint)
            }
        }
    }

    fun setState(newState: Int) {
        currentState = newState
        invalidate()
    }

    fun setValue(animatedValue: Int) {
        animValue = animatedValue
        invalidate()
    }
}

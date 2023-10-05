package com.example.gpstracker.ui.track.screens.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.gpstracker.R

class ProgressBarView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val progressBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressBarRect = RectF()
    private var rotationAngle = 0f

    init {
        progressBarPaint.style = Paint.Style.STROKE
        progressBarPaint.color = resources.getColor(R.color.light_grey)
        progressBarPaint.strokeWidth = 30f // Adjust the progress bar width as needed
    }

    override fun onDraw(canvas: Canvas) {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY)

        progressBarRect.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        canvas.save()
        canvas.rotate(rotationAngle, centerX, centerY)
        canvas.drawArc(progressBarRect, 0f, 360f, false, progressBarPaint)
        canvas.restore()
    }

    fun startProgressBarSpin() {
        rotationAngle = 0f
        val animation = ValueAnimator.ofFloat(0f, 360f)
        animation.duration = 1000 // Adjust the duration as needed
        animation.repeatCount = ValueAnimator.INFINITE
        animation.addUpdateListener { valueAnimator ->
            rotationAngle = valueAnimator.animatedValue as Float
            invalidate()
        }
        animation.start()
    }
}
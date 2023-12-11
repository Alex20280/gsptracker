package com.example.gpstracker.ui.track.screens.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.graphics.Canvas
import android.graphics.RectF
import com.example.gpstracker.R
import com.example.gpstracker.ui.track.TrackerState

class StatefulCircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var state = TrackerState.OFF
    private val image: Bitmap

    private val badgeImageOn: Bitmap
    private val badgeImageOff: Bitmap
    private val badgeImageExclamationMark: Bitmap
    private var animValue: Int = 0
    private val strokeWidth = 40

    init {
        badgeImageOn = BitmapFactory.decodeResource(resources, R.drawable.yellow_circle)
        badgeImageOff = BitmapFactory.decodeResource(resources, R.drawable.gray_circle)
        badgeImageExclamationMark = BitmapFactory.decodeResource(resources, R.drawable.exclamation_mark)
        image = BitmapFactory.decodeResource(resources, R.drawable.track_point)
    }

    fun setState(newState: TrackerState) {
        state = newState
        invalidate()
    }

    fun setValue(animatedValue: Int) {
        animValue = animatedValue
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY)

        // Draw the image in the center
        val imageLeft = centerX - image.width / 2.4
        val imageTop = centerY - image.height / 2
        canvas.drawBitmap(image, imageLeft.toFloat(), imageTop, null)

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

        // off status image
        val offBadgeMargin = 5 // Example margin from the right and top
        val offBadgeLeft = imageLeft + image.width - badgeImageOff.width.toFloat() - offBadgeMargin
        val offBadgeTop = imageTop + offBadgeMargin
        val offBadgeRight = offBadgeLeft + badgeImageOff.width
        val offBadgeBottom = offBadgeTop + badgeImageOff.height

        //on status image
        val onBadgeMargin = 5 // Example margin from the right and top
        val onBadgeLeft = imageLeft + image.width - badgeImageOn.width.toFloat() - onBadgeMargin
        val onBadgeTop = imageTop + onBadgeMargin
        val onBadgeRight = onBadgeLeft + badgeImageOn.width
        val onBadgeBottom = onBadgeTop + badgeImageOn.height

        // disconnected status image
        val disconnectedBadgeMargin = 6 // Example margin from the right and top
        val disconnectedBadgeLeft = imageLeft + image.width - badgeImageExclamationMark.width - disconnectedBadgeMargin
        val disconnectedBadgeTop = imageTop + disconnectedBadgeMargin
        val disconnectedBadgeRight = disconnectedBadgeLeft + badgeImageExclamationMark.width
        val disconnectedBadgeBottom = disconnectedBadgeTop + badgeImageExclamationMark.height


        when (state) {
            TrackerState.OFF -> {
                canvas.drawBitmap(badgeImageOff, null, RectF(offBadgeLeft.toFloat(), offBadgeTop, offBadgeRight.toFloat(), offBadgeBottom), null)
                //progressBar
                paint.color = context.resources.getColor(R.color.light_grey)
                canvas.drawArc(rectF, 0f, 360f, false, paint)
            }

            TrackerState.ON -> {
                canvas.drawBitmap(badgeImageOn, null, RectF(onBadgeLeft.toFloat(), onBadgeTop, onBadgeRight.toFloat(), onBadgeBottom), null)
                //progressBar
                paint.color = context.resources.getColor(R.color.colorAccent)
                canvas.drawArc(rectF, animValue.toFloat(), 80f, false, paint)
            }

            TrackerState.DISCONNECTED -> {
                canvas.drawBitmap(badgeImageExclamationMark, null, RectF(disconnectedBadgeLeft.toFloat(), disconnectedBadgeTop, disconnectedBadgeRight.toFloat(), disconnectedBadgeBottom), null)
                //progressBar
                paint.color = context.resources.getColor(R.color.red)
                canvas.drawArc(rectF, 0f, 360f, false, paint)
            }
        }
    }
}
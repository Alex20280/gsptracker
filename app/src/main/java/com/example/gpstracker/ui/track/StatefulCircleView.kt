package com.example.gpstracker.ui.track

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.gpstracker.utils.TrackerState
import android.graphics.Canvas
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout.CENTER_IN_PARENT
import com.example.gpstracker.R

class StatefulCircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var state = TrackerState.OFF // Default state is OFF

    private val circlePaint = Paint()
    private val exclamationPaint = Paint()
    private val exclamationPath = Path()
    private val image: Bitmap

    init {
        circlePaint.color = resources.getColor(R.color.grey) // Default color for OFF state
        exclamationPaint.color = Color.RED // Color for ERROR state
        exclamationPaint.style = Paint.Style.STROKE
        exclamationPaint.strokeWidth = 10f

        // Load the image from resources
        image = BitmapFactory.decodeResource(resources, R.drawable.track_point)
    }

    fun setState(newState: TrackerState) {
        state = newState
        invalidate() // Redraw the view to reflect the new state
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 1.5f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY)

        // Draw the image in the center
        val imageLeft = centerX - image.width / 1
        val imageTop = centerY - image.height / 2
        canvas.drawBitmap(image, imageLeft, imageTop, null)

        when (state) {
            TrackerState.OFF -> {
                // Draw the circle (3 times smaller)
                val smallerRadius = radius / 4f
                canvas.drawCircle(centerX, centerY, smallerRadius, circlePaint)
            }
            TrackerState.ON -> {
                // Draw the circle in yellow (3 times smaller)
                val smallerRadius = radius / 3f
                circlePaint.color = resources.getColor(R.color.colorAccent)
                canvas.drawCircle(centerX, centerY, smallerRadius, circlePaint)
            }
            TrackerState.DISCONNECTED -> {
                // Draw a red exclamation mark in the center for the ERROR state
                val exclamationSize = radius / 4 // Make it 3 times smaller

                // Draw the vertical line of the exclamation mark
                val lineStartX = centerX
                val lineStartY = centerY + exclamationSize * 0.8f
                val lineEndX = centerX
                val lineEndY = centerY - exclamationSize * 0.8f
                canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, exclamationPaint)

                // Draw a slightly larger solid red circle as the point of the exclamation mark
                val dotSize = exclamationSize * 0.3f
                exclamationPaint.style = Paint.Style.FILL
                canvas.drawCircle(centerX, centerY + exclamationSize * 1.2f, dotSize, exclamationPaint)

                // Restore the paint style to stroke for any future drawing
                exclamationPaint.style = Paint.Style.STROKE
            }
        }
    }
}
package com.example.myapplication

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.withStyledAttributes
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.lifecycle.LiveData
import androidx.lifecycle.findViewTreeLifecycleOwner

private const val TAG = "DOWNLOAD_BUTTON_TAG"
private const val ANIMATION_DURATION = 500L

@BindingMethods(
    BindingMethod(
        type = DownloadButton::class,
        attribute = "loadLinePercents",
        method = "setLoadLinePercents"
    )
)
class DownloadButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var loadPercent = 0f
    private var textColor = 0
    private var buttonBackgroundColor = 0
    private var circleColor = 0
    private var loadLineColor = 0

    fun setLoadLinePercents(percents: LiveData<Float>) {
        percents.observe(findViewTreeLifecycleOwner()!!) {
            ValueAnimator().apply {
                interpolator = DecelerateInterpolator()
                duration = ANIMATION_DURATION
                setFloatValues(loadPercent, percents.value!!)
                addUpdateListener {
                    loadPercent = animatedValue as Float
                    if (loadPercent == 1f) loadPercent = 0f
                    invalidate()
                }
                start()
            }
            loadPercent = percents.value!!
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55f
    }

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.DownloadButton) {
            textColor = getColor(R.styleable.DownloadButton_android_textColor, Color.WHITE)
            loadLineColor = getColor(R.styleable.DownloadButton_loadLineColor, Color.BLUE)
            buttonBackgroundColor = getColor(R.styleable.DownloadButton_android_background,
                Color.GRAY)
            circleColor = getColor(R.styleable.DownloadButton_circleColor, Color.YELLOW)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = buttonBackgroundColor
        canvas.drawRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            paint
        )

        paint.color = loadLineColor
        canvas.drawRect(
            0f,
            0f,
            width * loadPercent,
            height.toFloat(),
            paint
        )

        paint.color = circleColor
        canvas.drawArc(
            (width - height + 24).toFloat(),
            24f,
            (width - 24).toFloat(),
            (height - 24).toFloat(),
            0f,
            loadPercent * 360,
            true,
            paint
        )

        paint.color = textColor
        canvas.drawText(
            if (loadPercent == 0f) "Download" else "We are loading",
            (width / 2).toFloat(),
            (height / 2).toFloat() - (paint.descent() + paint.ascent()) / 2,
            paint
        )
    }
}
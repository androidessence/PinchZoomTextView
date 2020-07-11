package com.adammcneilly.pinchzoomtextview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.TextView
import com.adammcneilly.pinchzoomtextview.kotlin.R
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * TextView that increases/decreases font size as it is pinched.
 */
open class PinchZoomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextView(context, attrs) {

    /**
     * The ratio of the text size compared to its original.
     */
    private var ratio: Float = 1f

    /**
     * The distance between the two pointers when they are first placed on the screen.
     */
    private var baseDistance: Int = 0

    /**
     * The ratio of the text size when the gesture is started.
     */
    private var baseRatio: Float = 0f

    /**
     * The initial text size of this view when it's inflated, so we can use
     * this to make sure we scale accordingly.
     */
    private var initialTextSizePx: Int = spToPx(DEFAULT_FONT_SIZE_SP)

    /**
     * Helper variable to convert the [initialTextSizePx] to the text size in SP so that it can be
     * set on this view accordingly.
     */
    private val initialTextSizeSp: Float
        get() = pxToSp(initialTextSizePx.toFloat())

    /**
     * Boolean flag for whether or not zoom feature is enabled. Defaults to true.
     */
    var zoomEnabled: Boolean = true

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.PinchZoomTextView,
            0,
            0
        )

        try {
            initialTextSizePx = typedArray.getDimensionPixelSize(
                R.styleable.PinchZoomTextView_android_textSize,
                this.initialTextSizePx
            )
        } finally {
            typedArray.recycle()
        }

        this.textSize = initialTextSizeSp
    }

    /**
     * Handles the touch event by the user and determines whether font size should grow,
     * and by how much.
     *
     * If the action is simply `POINTER_DOWN` it means the user is just setting their fingers down,
     * so collect base values.
     *
     * Otherwise, the user is pinching, so get the distance between the pointers, find the ratio
     * we need, and set the text size.
     *
     * Inspiration taken from: http://stackoverflow.com/a/20303367/3131147
     */
    @Suppress("MoveVariableDeclarationIntoWhen")
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // If null for some reason, return
        if (event == null) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                paintFlags = paintFlags or Paint.LINEAR_TEXT_FLAG or Paint.SUBPIXEL_TEXT_FLAG
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                paintFlags = paintFlags and (Paint.LINEAR_TEXT_FLAG or Paint.SUBPIXEL_TEXT_FLAG).inv()
            }
        }

        // Must have two gestures
        if (zoomEnabled && event.pointerCount == 2) {
            val distance = getDistance(event)
            val pureAction = event.action and MotionEvent.ACTION_MASK

            when (pureAction) {
                MotionEvent.ACTION_POINTER_DOWN -> {
                    baseDistance = distance
                    baseRatio = ratio
                }
                else -> {
                    val delta = (distance - baseDistance) / STEP
                    val multi = 2.0.pow(delta.toDouble()).toFloat()
                    ratio = min(MAX_RATIO, max(MIN_RATIO, baseRatio * multi))
                    textSize = ratio + initialTextSizeSp
                }
            }
        }

        return true
    }

    /**
     * Returns the distance between two pointers on the screen.
     */
    private fun getDistance(event: MotionEvent): Int {
        val dx = (event.getX(0) - event.getX(1))
        val dy = (event.getY(0) - event.getY(1))
        return sqrt((dx * dx).toDouble() + (dy * dy)).toInt()
    }

    private fun spToPx(sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
            .toInt()
    }

    private fun pxToSp(px: Float): Float {
        return px / resources.displayMetrics.scaledDensity
    }

    companion object {
        /**
         * Consider each "step" between the two pointers as 200px. In other words, the TV size
         * will grow every 200 px the user's finger moves.
         */
        private const val STEP: Float = 200f

        /**
         * The largest ratio supported of a text size growth compared to the original text size.
         *
         * @see [ratio]
         */
        private const val MAX_RATIO = 1024.0f

        /**
         * The minimum ratio supported of a text size scale compared to the original text size.
         *
         * @see [ratio]
         */
        private const val MIN_RATIO = 0.1f

        /**
         * The default font size if one is not supplied via XML.
         */
        private const val DEFAULT_FONT_SIZE_SP = 14F
    }
}

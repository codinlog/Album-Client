package com.codinlog.album.widget.kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.codinlog.album.listener.CommonListener
import kotlin.math.abs
import kotlin.math.atan2


@SuppressLint("AppCompatCustomView")
class AlbumImageView : ImageView, ViewTreeObserver.OnGlobalLayoutListener
        , ScaleGestureDetector.OnScaleGestureListener
        , View.OnTouchListener {
    private var disallowIntercept = false
    private var commonListener: CommonListener? = null
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var initMatrix = Matrix()
    private val rectF = RectF()
    private var isOnce = true
    private var minScale = 1F
    private var maxScale = 1F
    private var midScale = 1F
    private var firstPointF = PointF()
    private var twoPointsDown = false
    private var firstClickTime = 0L
    private val handle = Handler()
    private var lastVector = PointF()

    private val runCommonListener = Runnable {
        if (isDoubleClick)
            isDoubleClick = false
        else
            commonListener?.handleEvent(null)
    }

    companion object {
        var isDoubleClick = false
        var lock = Any()
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        scaleType = ScaleType.MATRIX
        scaleGestureDetector = ScaleGestureDetector(context, this)
        setOnTouchListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        if (isOnce) {
            drawable ?: return
            resetMatrix()
            isOnce = false
        }
    }

    fun resetMatrix() {
        drawable ?: return
        val dw = drawable.intrinsicWidth
        val dh = drawable.intrinsicHeight
        minScale = width / dw.toFloat()
        midScale = minScale * 2
        maxScale = minScale * 4
        initMatrix.reset()
        initMatrix.postTranslate(width.toFloat() / 2 - dw / 2, height.toFloat() / 2 - dh / 2)
        initMatrix.postScale(minScale, minScale, width.toFloat() / 2, height.toFloat() / 2)
        imageMatrix = initMatrix
    }

    override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean = true

    override fun onScaleEnd(p0: ScaleGestureDetector?) {}

    override fun onScale(gestureDetector: ScaleGestureDetector?): Boolean {
        drawable ?: return true
        gestureDetector?.let {
            val scaleFactor = when {
                rectF.width() > width * 3f -> width * 3f / rectF.width()
                else -> it.scaleFactor
            }
            initMatrix.postScale(scaleFactor, scaleFactor, it.focusX, it.focusY)
            checkBorderAndCenter()
//            val scale = abs(initMatrix.values()[Matrix.MSCALE_X])
//            Log.i("scale", "onScale,$scale,$minScale,$maxScale,$scaleFactor")
//            if (scale < maxScale && scaleFactor > 1 || scale > minScale && scaleFactor < 1) {
//                if (scale * scaleFactor < minScale && scaleFactor < 1)
//                    scaleFactor = minScale / scale
//                if (scale * scaleFactor > maxScale && scaleFactor > 1)
//                    scaleFactor = maxScale / scale
//                initMatrix.preScale(scaleFactor, scaleFactor, it.focusX, it.focusY)
//                checkBorderAndCenter()
//            }
        }
        return true
    }

    private fun checkBorderAndCenter() {
        var mx = 0f
        var my = 0f
        var ms = 0f
        if (drawable != null) {
            rectF.set(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
            initMatrix.mapRect(rectF)
        }
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                mx = -rectF.left
            }
            if (rectF.right < width) {
                mx = width - rectF.right
            }
        } else {
            mx = width * 0.5f - rectF.right + 0.5f * rectF.width()
            ms = width / rectF.width()
        }
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                my = -rectF.top
            }
            if (rectF.bottom < height) {
                my = height - rectF.bottom
            }
        } else
            my = height * 0.5f - rectF.bottom + 0.5f * rectF.height()
        initMatrix.postTranslate(mx, my)
        if (ms > 0f)
            initMatrix.postScale(ms, ms, rectF.centerX(), rectF.centerY())
        imageMatrix = initMatrix
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        scaleGestureDetector?.onTouchEvent(event)
        return false
    }

    fun setCommonListener(commonListener: CommonListener?) {
        this.commonListener = commonListener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(disallowIntercept)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                disallowIntercept = false
                twoPointsDown = false
                firstPointF.set(event.x, event.y)
                if (System.currentTimeMillis() - firstClickTime < 300) {
                    resetMatrix()
                    isDoubleClick = true
                }
                firstClickTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                twoPointsDown = true
                disallowIntercept = true
                val dx = event.getX(0) - event.getX(1)
                val dy = event.getY(0) - event.getY(1)
                lastVector.set(dx, dy)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                twoPointsDown = false
                backRotation()
            }
            MotionEvent.ACTION_MOVE -> {
                if (twoPointsDown) {
                    val dx = event.getX(0) - event.getX(1)
                    val dy = event.getY(0) - event.getY(1)
                    val nowDegree = Math.toDegrees(atan2(dx.toDouble(), dy.toDouble())).toFloat()
                    val oldDegree = Math.toDegrees(atan2(lastVector.x.toDouble(), lastVector.y.toDouble())).toFloat()
                    lastVector.set(dx, dy)
                    initMatrix.postRotate(oldDegree - nowDegree, rectF.centerX(), rectF.centerY())
                    rectF.set(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
                    initMatrix.mapRect(rectF)
                    imageMatrix = initMatrix
                } else {
                    val mx = event.x - firstPointF.x
                    val my = event.y - firstPointF.y
                    firstPointF.x = event.x
                    firstPointF.y = event.y
                    val canTransX = (mx < 0 && rectF.right > width || mx > 0 && rectF.left < 0)
                    val canTransY = (my < 0 && rectF.bottom > height || my > 0 && rectF.top < 0)
                    if (canTransX || canTransY) {
                        initMatrix.postTranslate(if (canTransX) mx else 0f, if (canTransY) my else 0f)
                        rectF.set(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
                        initMatrix.mapRect(rectF)
                        imageMatrix = initMatrix
                    }
                    disallowIntercept = canTransX
                    parent.requestDisallowInterceptTouchEvent(disallowIntercept)
                }
            }
            MotionEvent.ACTION_UP -> {
                checkBorderAndCenter()
                if (!disallowIntercept) {
                    handle.removeCallbacks(runCommonListener)
                    handle.postDelayed(runCommonListener, 200)
                }
            }
        }
        return true
    }

    private fun backRotation() {
        val x_vector = floatArrayOf(1.0f, 0.0f)
        initMatrix.mapVectors(x_vector)
        val totalDegree = Math.toDegrees(atan2(x_vector[1].toDouble(), x_vector[0].toDouble())).toFloat()
        var degree = totalDegree
        degree = abs(degree)
        degree = when (degree) {
            in 45f..135f -> 90f
            in 135f..225f -> 180f
            in 225f..315f -> 270f
            else -> 0f
        }
        degree = if (totalDegree < 0) -degree else degree
        initMatrix.postRotate(degree - totalDegree, rectF.centerX(), rectF.centerY())
////        imageMatrix = initMatrix
        checkBorderAndCenter()
    }
}
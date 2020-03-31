package com.codinlog.album.widget.kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.core.graphics.values
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
    private var initScale = 1F
    private var maxScale = 1F
    private var midScale = 1F
    private var firstPointF = PointF()
    private var twoPointsDown = false
    private var firstClickTime = 0L
    private val handle = Handler()
    private var lastVector: PointF? = null
    private val runCommonListener = Runnable {
        Log.d("click", "$isDoubleClick")
        if (isDoubleClick)
            isDoubleClick = false
        else
            commonListener?.handleEvent(null)
    }

    companion object {
        var isDoubleClick = false
    }

    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}
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
            initMatrix()
            isOnce = false
        }
    }

    fun initMatrix() {
        drawable ?: return
        val dw = drawable.intrinsicWidth
        val dh = drawable.intrinsicHeight
        initScale = width / dw.toFloat()
        midScale = initScale * 2
        maxScale = initScale * 4
        initMatrix.reset()
        initMatrix.postTranslate(width.toFloat() / 2 - dw / 2, height.toFloat() / 2 - dh / 2)
        initMatrix.postScale(initScale, initScale, width.toFloat() / 2, height.toFloat() / 2)
        imageMatrix = initMatrix
    }

    override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean = true

    override fun onScaleEnd(p0: ScaleGestureDetector?) {}

    override fun onScale(gestureDetector: ScaleGestureDetector?): Boolean {
        drawable ?: return true
        gestureDetector?.let {
            var scaleFactor = it.scaleFactor
            val scale = initMatrix.values()[Matrix.MSCALE_X]
            if (scale < maxScale && scaleFactor > 1 || scale > initScale && scaleFactor < 1) {
                if (scale * scaleFactor < initScale && scaleFactor < 1)
                    scaleFactor = initScale / scale
                if (scale * scaleFactor > maxScale && scaleFactor > 1)
                    scaleFactor = maxScale / scale
                initMatrix.postScale(scaleFactor, scaleFactor, it.focusX, it.focusY)
                checkBorderAndCenter()
                imageMatrix = initMatrix
            }
        }
        return true
    }

    private fun checkBorderAndCenter() {
        var mx = 0f
        var my = 0f
        if (drawable != null) {
            rectF.set(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
            initMatrix.mapRect(rectF)
        }
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                mx = -rectF.left;
            }
            if (rectF.right < width) {
                mx = width - rectF.right;
            }
        } else
            mx = width * 0.5f - rectF.right + 0.5f * rectF.width();
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                my = -rectF.top;
            }
            if (rectF.bottom < height) {
                my = height - rectF.bottom
            }
        } else
            my = height * 0.5f - rectF.bottom + 0.5f * rectF.height();
        initMatrix.postTranslate(mx, my)
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
                    initMatrix()
                    isDoubleClick = true
                }
                firstClickTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                twoPointsDown = true
                disallowIntercept = true
            }
            MotionEvent.ACTION_POINTER_UP -> {
                backRotation();
            }
            MotionEvent.ACTION_MOVE -> {
                if (!twoPointsDown) {
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
                if (!disallowIntercept) {
                    handle.removeCallbacks(runCommonListener)
                    handle.postDelayed(runCommonListener, 200)
                }
            }
        }
        return true
    }

    private fun calculateDeltaDegree(lastVector: PointF?, vector: PointF): Float {
        lastVector?.let {
            val lastDegree = atan2(it.x.toDouble(), it.x.toDouble())
            val degree = atan2(vector.y.toDouble(), vector.x.toDouble())
            val deltaDegree = degree - lastDegree
            return Math.toDegrees(deltaDegree).toFloat()
        }
        return 0f
    }

    private fun backRotation() {
        //x轴方向的单位向量，在极坐标中，角度为0
        val x_vector = floatArrayOf(1.0f, 0.0f)
        //映射向量
        initMatrix.mapVectors(x_vector)
        //计算x轴方向的单位向量转过的角度
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
        rectF.set(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
        initMatrix.mapRect(rectF)
        imageMatrix = initMatrix
    }

}
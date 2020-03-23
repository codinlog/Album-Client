package com.codinlog.album.widget.kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import com.codinlog.album.listener.CommonListener

@SuppressLint("AppCompatCustomView")
class AlbumImageView : ImageView {
    private var disallowIntercept = false
    private var commonListener: CommonListener? = null
    private val pointStart = PointF()
    private val pointMid = PointF()
    private val pointEnd = PointF()
    private val pointCenter = PointF()
    private val currentMatrix = Matrix()
    private val oldMatrix: Matrix? = null
    private var currentRotation = 0f
    private var oldRotation = 0f
    private var newRotation = 0f
    private var currentDist = 1f
    private var oldDist = 1f
    private var scale = 1f

    private enum class MODE {
        NONE, DRAG, ZOOM_ROTATE
    }

    private var currentMode = MODE.NONE

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun setCommonListener(commonListener: CommonListener?) {
        this.commonListener = commonListener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        pointCenter[width / 2.0f] = height / 2.0f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (disallowIntercept) parent.requestDisallowInterceptTouchEvent(true) else parent.requestDisallowInterceptTouchEvent(false)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                disallowIntercept = false
                pointStart[event.x] = event.y
                currentMode = MODE.NONE
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                disallowIntercept = true
                currentMode = MODE.ZOOM_ROTATE
                oldDist = pointSpacing(event)
                oldRotation = pointRotation(event)
                pointMid(pointMid, event)
            }
            MotionEvent.ACTION_MOVE -> if (currentMode == MODE.ZOOM_ROTATE) {
                currentRotation = pointRotation(event)
                currentDist = pointSpacing(event)
                newRotation = currentRotation - oldRotation
                scale = scale + currentDist / oldDist - 1
                when {
                    scale in 0.5..1.5 -> {
                        currentMatrix.postScale(scale, scale, pointMid.x, pointMid.y)
                        currentMatrix.postRotate(newRotation, pointMid.x, pointMid.y)
                        imageMatrix = currentMatrix
                    }
                    scale > 1.5 -> scale = 1.5f
                    scale < 0.5 -> scale = 0.5f
                }
            }
            MotionEvent.ACTION_POINTER_UP -> currentMode = MODE.NONE
            MotionEvent.ACTION_UP -> {
                if (commonListener != null && !disallowIntercept) commonListener!!.handleEvent(null)
                pointEnd[event.x] = event.y
            }
        }
        return true
    }

    // 触碰两点间距离
    private fun pointSpacing(event: MotionEvent): Float {
        if (event.pointerCount >= 2) {
            val x = event.getX(0) - event.getX(1)
            val y = event.getY(0) - event.getY(1)
            return Math.sqrt(x * x + y * y.toDouble()).toFloat()
        }
        return 0f
    }

    // 取手势中心点
    private fun pointMid(point: PointF, event: MotionEvent) {
        if (event.pointerCount >= 2) {
            val x = event.getX(0) + event.getX(1)
            val y = event.getY(0) + event.getY(1)
            point[x / 2] = y / 2
            return
        }
        point[0f] = 0f
    }

    // 取旋转角度
    private fun pointRotation(event: MotionEvent): Float {
        if (event.pointerCount >= 2) {
            val delta_x = (event.getX(0) - event.getX(1)).toDouble()
            val delta_y = (event.getY(0) - event.getY(1)).toDouble()
            val radians = Math.atan2(delta_y, delta_x)
            return Math.toDegrees(radians).toFloat()
        }
        return 0f
    }
}
package com.codinlog.album.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.codinlog.album.listener.CommonListener;

@SuppressLint("AppCompatCustomView")
public class AlbumImageView extends ImageView {
    private boolean disallowIntercept = false;
    private CommonListener commonListener;
    private PointF pointStart = new PointF(), pointMid = new PointF(), pointEnd = new PointF(), poinCenter = new PointF();
    private Matrix currentMatrix = new Matrix(), oldMatrix = null;
    private float currentRotation = 0f, oldRotation = 0f, rotation = 0f;
    private float currentDist = 1f, oldDist = 1f, scale = 1f;
    private enum MODE {NONE, DRAG, ZOOM_ROTATE}
    private MODE currentMode = MODE.NONE;

    public AlbumImageView(Context context) {
        super(context);
    }

    public AlbumImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCommonListener(CommonListener commonListener) {
        this.commonListener = commonListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        poinCenter.set(getWidth() / 2.0f, getHeight() / 2.0f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (disallowIntercept)
            getParent().requestDisallowInterceptTouchEvent(true);
        else
            getParent().requestDisallowInterceptTouchEvent(false);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                disallowIntercept = false;
                pointStart.set(event.getX(), event.getY());
                currentMode = MODE.NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                disallowIntercept = true;
                currentMode = MODE.ZOOM_ROTATE;
                oldDist = pointSpacing(event);
                oldRotation = pointRotation(event);
                pointMid(pointMid, event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentMode == MODE.ZOOM_ROTATE) {
                    currentRotation = pointRotation(event);
                    currentDist = pointSpacing(event);
                    rotation = currentRotation - oldRotation;
                    scale = scale + currentDist / oldDist - 1;
                    if (scale <= 1.5 && scale >= 0.5) {
                        currentMatrix.postScale(scale, scale, pointMid.x, pointMid.y);
                        currentMatrix.postRotate(rotation, pointMid.x, pointMid.y);
                        setImageMatrix(currentMatrix);
                    } else if (scale > 1.5)
                        scale = 1.5f;
                    else if (scale < 0.5)
                        scale = 0.5f;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                currentMode = MODE.NONE;
                break;
            case MotionEvent.ACTION_UP:
                if (commonListener != null && !disallowIntercept)
                    commonListener.handleEvent(null);
                pointEnd.set(event.getX(), event.getY());
                break;
        }
        return true;
    }

    // 触碰两点间距离
    private float pointSpacing(MotionEvent event) {
        if (event.getPointerCount() >= 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }
        return 0f;
    }

    // 取手势中心点
    private void pointMid(PointF point, MotionEvent event) {
        if (event.getPointerCount() >= 2) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
            return;
        }
        point.set(0f, 0f);
    }

    // 取旋转角度
    private float pointRotation(MotionEvent event) {

        if (event.getPointerCount() >= 2) {
            double delta_x = (event.getX(0) - event.getX(1));
            double delta_y = (event.getY(0) - event.getY(1));
            double radians = Math.atan2(delta_y, delta_x);
            return (float) Math.toDegrees(radians);
        }
        return 0f;
    }

}

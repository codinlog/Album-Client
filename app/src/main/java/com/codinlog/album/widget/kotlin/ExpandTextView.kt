package com.codinlog.album.widget.kotlin

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.codinlog.album.R


class ExpandTextView : LinearLayout, TypeEvaluator<Float> {
    private lateinit var tvContent: TextView
    private lateinit var tvIndicator: TextView
    private var tvMinHeight = 0
    private var tvMaxHeight = 0
    private var isFirst = true
    private var isRunningAnimation = false
    private var isExpand = false


    companion object {
        const val ANIMATE_INTERVAL = 1000L
        var limitLines = 1
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        initView(context, attrs)
    }


    private fun initView(context: Context?, attrs: AttributeSet?) {
        orientation = VERTICAL
        val customAttrs = context?.obtainStyledAttributes(attrs, R.styleable.ExpandTextView)
        customAttrs?.let {
            limitLines = it.getInteger(R.styleable.ExpandTextView_maxLines, 1)
            it.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        tvContent = findViewById(R.id.tvContent)
        tvIndicator = findViewById(R.id.tvIndicator)
    }

    fun setTvIndicatorOnClickListener(listener: () -> Unit) {
        tvIndicator.setOnClickListener {
            if (isRunningAnimation) return@setOnClickListener
            listener.invoke()
        }
    }

    fun setTvContent(text: String) {
        tvContent.text = text
        tvContent.maxLines = Int.MAX_VALUE
        tvIndicator.text = context.getText(R.string.expand)
        tvIndicator.visibility = if (tvContent.lineCount > limitLines) View.GONE else View.VISIBLE
        visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
        tvContent.viewTreeObserver.addOnGlobalLayoutListener {
            if (isFirst)
                if (tvMaxHeight == 0) {
                    tvMaxHeight = tvContent.height
                    tvContent.maxLines = limitLines
                } else if (tvMinHeight == 0) {
                    tvMinHeight = tvContent.height
                    isFirst = false

                }
        }
        requestLayout()
    }

    fun expand() {
        ValueAnimator.ofObject(this, tvMinHeight.toFloat(), tvMaxHeight.toFloat() - tvMinHeight)
                .apply {
                    duration = ANIMATE_INTERVAL
                    val oldHeight = height
                    addUpdateListener {
                        with(this@ExpandTextView) {
                            val increment = (it.animatedValue as Float).toInt()
                            this.layoutParams.height = oldHeight + increment
                            this.requestLayout()
                        }
                    }
                    doOnStart {
                        isRunningAnimation = true
                        tvContent.maxLines = Int.MAX_VALUE
                        tvIndicator.text = context.getText(R.string.collapsed)
                    }
                    doOnEnd {
                        isRunningAnimation = false
                        isExpand = true
                    }
                    start()
                }
    }

    fun collapsed() {
        ValueAnimator.ofObject(this, tvMinHeight.toFloat(), tvMaxHeight.toFloat() - tvMinHeight)
                .apply {
                    duration = ANIMATE_INTERVAL
                    val oldHeight = height
                    addUpdateListener {
                        with(this@ExpandTextView) {
                            val increment = (it.animatedValue as Float).toInt()
                            this.layoutParams.height = oldHeight - increment
                            this.requestLayout()
                        }
                    }
                    doOnStart {
                        isRunningAnimation = true
                        tvIndicator.text = context.getText(R.string.expand)
                    }
                    doOnEnd {
                        isRunningAnimation = false
                        isExpand = false
                        tvContent.maxLines = limitLines
                    }
                    start()
                }
    }

    override fun evaluate(fraction: Float, startValue: Float?, endValue: Float?): Float {
        if (startValue != null && endValue != null) {
            return startValue + (endValue - startValue) * fraction
        }
        return 0F;
    }
}
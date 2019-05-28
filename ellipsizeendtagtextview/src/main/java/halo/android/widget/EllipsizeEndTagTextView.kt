package halo.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.NonNull
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView

/**
 * Created by Lucio on 2019/5/27.
 */

class EllipsizeEndTagTextView : TextView {

    private var mEndTag: String? = null

    @ColorInt
    private var mEndTagColor: Int = Color.BLUE

    private var mEndTagClick: View.OnClickListener? = null

    private var drawEndTagFlag: Boolean = false

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EllipsizeEndTagTextView)
            mEndTag = typedArray.getString(R.styleable.EllipsizeEndTagTextView_endTag)
            mEndTagColor = typedArray.getColor(R.styleable.EllipsizeEndTagTextView_endTagColor, Color.BLUE)
            typedArray.recycle()
        }

        //避免xml中包含text不生效的问题：初始化的时候，让文本可以绘制一次
        drawEndTagFlag = true
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (drawEndTagFlag) {
            drawEndTagFlag = false
            setTextForEndTagEllipsizeImpl(this, mEndTag, mEndTagColor, mEndTagClick)
        }
    }

    /**
     * 对外的方法，用于需要重新设置标记
     */
    @JvmOverloads
    fun setTextForEndTagEllipsize(
        originalText: String?,
        endTag: String? = mEndTag,
        @ColorInt endTagColor: Int = mEndTagColor,
        endTagClick: View.OnClickListener? = mEndTagClick
    ) {

        mEndTag = endTag
        mEndTagColor = endTagColor
        mEndTagClick = endTagClick

        if (endTag == null) {
            mEndTag = null
            text = originalText
            drawEndTagFlag = false
        } else {
            text = originalText
            drawEndTagFlag = true
        }
    }

    companion object {

        /**
         * 此方法适合用于固定布局中的的textview
         * 如果需要用在适配器中，请使用[EllipsizeEndTagTextView]
         */
        @JvmStatic
        fun setEndTagForEllipsizeText(
            textView: TextView, originalText: String?,
            endTag: String?,
            @ColorInt endTagColor: Int,
            endTagClick: View.OnClickListener? = null
        ) {
            if (originalText.isNullOrEmpty() || endTag.isNullOrEmpty()) {
                textView.text = originalText
                return
            }
            textView.text = originalText

            textView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    try {
                        //先移除监听
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            textView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }
                        setTextForEndTagEllipsizeImpl(textView, endTag, endTagColor, endTagClick)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }

        @JvmStatic
        private fun setTextForEndTagEllipsizeImpl(
            textView: TextView,
            endTag: String?,
            @ColorInt endTagColor: Int,
            endTagClick: View.OnClickListener? = null
        ) {

            if (endTag.isNullOrEmpty())
                return

            val layout = textView.layout ?: return
            val lastLineIndex = layout.lineCount - 1
            val ellipsisCount = layout.getEllipsisCount(lastLineIndex)
            if (ellipsisCount <= 0) {//没有超过指定字数 不用重新显示
                return
            }
            val currentEllipsizeText = layout.text.subSequence(0, layout.getLineVisibleEnd(lastLineIndex))
            val endTagWidth = textView.paint.measureText(endTag)

            val withEndTagEllipsizeText: SpannableStringBuilder
            if (layout.getLineWidth(lastLineIndex) + endTagWidth < layout.width) {
                //如果当前行宽+标记文字宽度不足控件宽度，则
                withEndTagEllipsizeText = SpannableStringBuilder(currentEllipsizeText).append(endTag)
            } else {
                if (layout.getLineVisibleEnd(lastLineIndex) - layout.getLineStart(lastLineIndex) > endTag.length) {
                    //说明最后一行显示的字数是6个以上，可以直接移除两个字符之后再拼接末尾
                    val startRange = layout.getLineStart(lastLineIndex) + layout.getEllipsisStart(lastLineIndex)

                    withEndTagEllipsizeText = SpannableStringBuilder(
                        currentEllipsizeText.removeRange(startRange - endTag.length, startRange)
                    ).append(endTag)
                } else {
                    //说明这一行显示的文本很少，可以直接附带末尾标记
                    withEndTagEllipsizeText = SpannableStringBuilder(currentEllipsizeText).append(endTag)
                }
            }

            val endTagStartIndex = withEndTagEllipsizeText.length - endTag.length

            if (endTagClick == null) {//如果点击时间为null，则直接将tag渲染颜色即可
                textView.text =
                    toHighLight(withEndTagEllipsizeText, endTagStartIndex, withEndTagEllipsizeText.length, endTagColor)
            } else {
                // 点击事件和添加在末尾的文本效果
                withEndTagEllipsizeText.setSpan(object : ClickableSpan() {
                    override fun onClick(view: View) {
                        endTagClick.onClick(view)
                    }

                    override fun updateDrawState(@NonNull ds: TextPaint) {
                        super.updateDrawState(ds)
                        // 添加在末尾的文本颜色
                        ds.color = endTagColor
                        // 添加在末尾的文本去掉下划线
                        ds.isUnderlineText = false
                        // 清除阴影
                        ds.clearShadowLayer()
                    }
                }, endTagStartIndex, withEndTagEllipsizeText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                // 重新设置TextView文本
                textView.text = withEndTagEllipsizeText
//              // 去除默认点击后背景变色效果
                textView.highlightColor = Color.TRANSPARENT
                // 让点击事件可以生效
                textView.movementMethod = LinkMovementMethod.getInstance()
                // 取消关注，让点击事件与ListView的Item点击事件不再冲突
                textView.isFocusable = false
            }
        }

        /**
         * 高亮显示
         * @param start 渲染开始位置
         * @param end 渲染结束位置
         * @param color 颜色
         */
        @JvmStatic
        private fun toHighLight(content: CharSequence, start: Int, end: Int, @ColorInt color: Int): CharSequence {
            val style = SpannableStringBuilder(content)
            if (start >= 0 && end >= 0 && end >= start) {
                style.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            return style

        }

    }
}

/**
 * extensions for TextView
 */
@JvmOverloads
inline fun TextView.applyEndTagEllipsizeText(
    originalText: String?,
    endTag: String?,
    @ColorInt endTagColor: Int,
    endTagClick: View.OnClickListener? = null
) {
    EllipsizeEndTagTextView.setEndTagForEllipsizeText(this, originalText, endTag, endTagColor, endTagClick)
}
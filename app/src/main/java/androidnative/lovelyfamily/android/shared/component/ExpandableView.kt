package androidnative.lovelyfamily.android.shared.component

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidnative.lovelyfamily.android.utils.toPx

class ExpandableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var contentView: View? = null
    var isExpanded = false
    private var extraMargin: Int = 0
    private var expandedHeight: Int = 0
    private val animationDuration = 300L // Duration of the expand/collapse animation

    init {
        orientation = VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // Assume the first child view is the expandable content
        if (childCount > 0) {
            // the child should contain ViewGroup like LinearLayout, ConstraintLayout, RelativeLayout
            // if want to show more than 1 child
            contentView = getChildAt(0)
            contentView?.visibility = GONE
        } else {
            throw IllegalStateException("ExpandableView must have at least one child")
        }
    }

    private fun measureExpandedHeight() {
        if (expandedHeight > 0) return

        contentView?.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        expandedHeight = (contentView?.measuredHeight ?: 0) + context.toPx(extraMargin)
    }

    fun setExtraMargin(margin: Int) {
        this.extraMargin = margin
    }

    fun toggleExpanded(animate: Boolean = true) {
        isExpanded = !isExpanded

        // Measure the expanded height dynamically
        measureExpandedHeight()

        if (animate) {
            animateExpansion()
        } else {
            contentView?.visibility = if (isExpanded) VISIBLE else GONE
            contentView?.layoutParams?.height = if (isExpanded) expandedHeight else 0
            contentView?.requestLayout()
        }
    }

    private fun animateExpansion() {
        val startHeight = if (isExpanded) 0 else expandedHeight
        val endHeight = if (isExpanded) expandedHeight else 0

        val animator = ValueAnimator.ofInt(startHeight, endHeight).apply {
            duration = animationDuration
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { valueAnimator ->
                val height = valueAnimator.animatedValue as Int
                contentView?.layoutParams?.height = height
                contentView?.requestLayout()
                contentView?.visibility = if (height > 0) VISIBLE else GONE
            }
        }
        animator.start()
    }
}

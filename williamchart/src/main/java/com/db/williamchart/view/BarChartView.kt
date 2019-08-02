package com.db.williamchart.view

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import com.db.williamchart.ChartContract
import com.db.williamchart.animation.NoAnimation
import com.db.williamchart.data.DataPoint
import com.db.williamchart.data.Frame
import com.db.williamchart.data.Label
import com.db.williamchart.data.toRect
import com.db.williamchart.renderer.BarChartRenderer

class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ChartView(context, attrs, defStyleAttr), ChartContract.View {

    /**
     * API
     */

    @Suppress("MemberVisibilityCanBePrivate")
    var spacing = 10

    @ColorInt
    @Suppress("MemberVisibilityCanBePrivate")
    var barColor: Int = -0x1000000

    @Suppress("MemberVisibilityCanBePrivate")
    var barRadius: Float = 0F

    init {
        renderer = BarChartRenderer(this, painter, NoAnimation())
    }

    override fun drawData(
        innerFrame: Frame,
        entries: List<DataPoint>
    ) {

        val halfBarWidth = (innerFrame.right - innerFrame.left - (entries.size + 1) * spacing) / entries.size / 2

        painter.prepare(color = barColor, style = Paint.Style.FILL)
        entries.forEach {
            canvas.drawRoundRect(
                RectF(
                    it.screenPositionX - halfBarWidth,
                    it.screenPositionY,
                    it.screenPositionX + halfBarWidth,
                    innerFrame.bottom
                ),
                barRadius,
                barRadius,
                painter.paint
            )
        }
    }

    override fun drawLabels(xLabels: List<Label>) {

        painter.prepare(
            textSize = labelsSize,
            color = labelsColor,
            font = labelsFont
        )
        xLabels.forEach {
            canvas.drawText(
                it.label,
                it.screenPositionX,
                it.screenPositionY,
                painter.paint
            )
        }
    }

    override fun drawDebugFrame(outerFrame: Frame, innerFrame: Frame, labelsFrame: List<Frame>) {
        painter.prepare(color = -0x1000000, style = Paint.Style.STROKE)
        canvas.drawRect(outerFrame.toRect(), painter.paint)
        canvas.drawRect(innerFrame.toRect(), painter.paint)
        labelsFrame.forEach { canvas.drawRect(it.toRect(), painter.paint) }
    }
}
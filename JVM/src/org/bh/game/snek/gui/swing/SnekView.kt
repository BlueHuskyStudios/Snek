package org.bh.game.snek.gui.swing

import org.bh.game.snek.state.BaseSnekDataView
import org.bh.tools.base.func.observing
import org.bh.tools.base.math.float32Value
import org.bh.tools.base.math.geometry.Rect
import org.bh.tools.base.math.geometry.awtValue
import org.bh.tools.base.math.geometry.fractionValue
import org.bh.tools.base.math.geometry.sizeValue
import org.bh.game.snek.util.*
import org.bh.tools.base.math.int32Value
import org.bh.tools.base.struct.UIView
import org.bh.tools.ui.swing.drawLine
import org.bh.tools.ui.swing.drawRect
import org.bh.tools.ui.swing.fillOval
import org.bh.tools.ui.swing.setAntiAlias
import java.awt.*
import javax.swing.JComponent

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-05
 */
class SnekView(dataView: BaseSnekDataView) : JComponent(), UIView<BaseSnekDataView> {

    override var representedObject: BaseSnekDataView by observing(dataView, didSet = { _, _ -> update() } )

    private val fontSize = 10.float32Value


    fun update() {
        repaint()
    }

    override fun paint(g: Graphics?) {
        super.paint(g)

        if (g == null) return
        g.setAntiAlias(true)
        if (g is Graphics2D) {
            g.renderingHints[RenderingHints.KEY_ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_ON
            g.fontRenderContext
        }
        g.font = g.font.deriveFont(fontSize)

        val multiplier = (g.clipBounds.size.sizeValue.fractionValue / representedObject.boardSize.fractionValue)

//        representedObject.path.safeReduce { previous, current ->
//            g.drawLine(previous * multiplier, current * multiplier)
//            /*return*/ current
//        }
        representedObject.path.lineSegments
                .map { it.fractionValue }
                .map { Pair(it, it * multiplier.pairValue) }
                .forEach { (original, scaled) ->
                    g.color = SystemColor.controlText
                    g.drawLine(scaled)
                    g.fillOval(boundingRect = Rect(scaled.start, multiplier))
                    g.color = Color(0f, 0f, 0f, 0.3f)
                    g.drawString(original.start.stringValue, (scaled.start.x + 2).int32Value, (scaled.start.y + fontSize).int32Value)
                }

        g.drawString(representedObject.screen.name, 0, height)
    }

    override fun getPreferredSize(): Dimension {
        return (representedObject.boardSize * 8).awtValue
    }

    @Suppress("OverridingDeprecatedMember")
    override fun preferredSize(): Dimension {
        return preferredSize
    }
}

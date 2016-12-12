package org.bh.game.snek.gui.swing

import org.bh.game.snek.state.BaseSnekDataView
import org.bh.tools.base.func.observing
import org.bh.tools.base.math.float32Value
import org.bh.tools.base.math.geometry.*
import org.bh.tools.base.math.int32Value
import org.bh.tools.base.struct.UIView
import org.bh.tools.ui.swing.drawRect
import org.bh.tools.ui.swing.setAntiAlias
import java.awt.*
import java.awt.Dimension
import javax.swing.JComponent

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-05
 */
class SnekView(dataView: BaseSnekDataView) : JComponent(), UIView {

    var dataView: BaseSnekDataView by observing(dataView, didSet = { old, new -> update() } )

    fun update() {
        repaint()
    }

    private val fontSize = 10.float32Value

    override fun paint(g: Graphics?) {
        super.paint(g)

        if (g == null) return
        g.setAntiAlias(true)
        if (g is Graphics2D) {
            g.renderingHints[RenderingHints.KEY_ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_ON
            g.fontRenderContext
        }
        g.font = g.font.deriveFont(fontSize)

        val multiplier = (g.clipBounds.size.sizeValue.floatValue / dataView.boardSize.floatValue)

//        dataView.path.safeReduce { previous, current ->
//            g.drawLine(previous * multiplier, current * multiplier)
//            /*return*/ current
//        }
        dataView.path
                .map { Pair(it, it * multiplier.pairValue) }
                .forEach { (original, scaled) ->
                    g.drawRect(Rect(scaled, multiplier))
                    g.drawString(original.stringValue, scaled.x.int32Value + 1, (scaled.y + fontSize).int32Value)
                }

        g.drawString(dataView.screen.name, 0, height)
    }

    override fun getPreferredSize(): Dimension {
        return (dataView.boardSize * 8).awtValue
    }

    override fun preferredSize(): Dimension {
        return preferredSize
    }
}

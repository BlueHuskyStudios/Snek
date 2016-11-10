package org.bh.game.snek.gui.swing

import org.bh.game.snek.state.BaseSnekDataView
import org.bh.tools.base.collections.safeReduce
import org.bh.tools.base.func.observing
import org.bh.tools.base.struct.UIView
import org.bh.tools.base.struct.coord.*
import org.bh.tools.ui.swing.antiAlias
import org.bh.tools.ui.swing.drawLine
import java.awt.Dimension
import java.awt.Graphics
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

    override fun paint(g: Graphics?) {
        super.paint(g)

        if (g == null) return
        g.antiAlias = true

        val multiplier = g.clipBounds.size.sizeValue / dataView.boardSize

        dataView.snek.safeReduce { previous, current -> IntPoint
            g.drawLine(previous * multiplier, current * multiplier)
            /*return*/ current
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

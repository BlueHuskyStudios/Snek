package org.bh.game.snek.gui.swing

import org.bh.game.snek.state.BaseSnekDataView
import org.bh.game.snek.util.times
import org.bh.tools.base.func.observing
import org.bh.tools.base.math.*
import org.bh.tools.base.math.geometry.*
import org.bh.tools.base.struct.UIView
import org.bh.tools.ui.swing.*
import java.awt.*
import java.awt.Dimension
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
        g.antiAlias = true
        g.textAntiAlias = TextAntiAliasApproach.horizontalRGBStripe
        g.font = g.font.deriveFont(fontSize)

        val frame = g.clipBounds.fractionValue
        val boardSize = representedObject.boardSize
        val multiplier = (frame.size.fractionValue / boardSize.fractionValue)
        val dotSize = multiplier.minDimension / 3

        g.color = SystemColor.controlText.withAlphaComponent(0.1)

        (0..boardSize.width)
                .map { it.fractionValue * multiplier.width }
                .forEach {
                    g.drawLine(FractionLineSegment(
                            start = FractionPoint(x = it, y = 0.0),
                            end = FractionPoint(x = it, y = frame.height)
                    ))
                }
        (0..boardSize.height)
                .map { it.fractionValue * multiplier.height }
                .forEach {
                    g.drawLine(FractionLineSegment(
                            start = FractionPoint(x = 0.0, y = it),
                            end = FractionPoint(x = frame.width, y = it)
                    ))
                }

//        representedObject.path.safeReduce { previous, current ->
//            g.drawLine(previous * multiplier, current * multiplier)
//            /*return*/ current
//        }
        representedObject.path.segments
                .map { it.fractionValue }
                .map {
                    Pair(it, it * multiplier.pairValue)
                }
                .forEach { (originalLineSegment, scaledLineSegment) ->
                    g.color = SystemColor.controlText

                    g.drawLine(scaledLineSegment)
                    g.fillCircle(radius = dotSize, center = scaledLineSegment.start)
                    if (representedObject.debug) {
                        g.color = Color(0.5f, 0.5f, 0.5f, 0.3f)
                        g.drawString(originalLineSegment.start.stringValue, (scaledLineSegment.start.x + 2).int32Value, (scaledLineSegment.start.y + fontSize).int32Value)
                    }
                }

        g.color = SystemColor.controlText
        representedObject.headPosition.let {
            g.color = awtColorFromHex("#4CAF50")
            g.fillCircle(radius = dotSize, center = it * multiplier.pairValue)
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

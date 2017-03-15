package org.bh.game.snek.gui.swing

import org.bh.game.snek.state.BaseSnekDataView
import org.bh.game.snek.state.SnekScreen
import org.bh.game.snek.util.times
import org.bh.tools.base.abstraction.Fraction
import org.bh.tools.base.collections.extensions.firstOrNull
import org.bh.tools.base.func.observing
import org.bh.tools.base.math.*
import org.bh.tools.base.math.geometry.*
import org.bh.tools.base.struct.UIView
import org.bh.tools.ui.swing.*
import java.awt.*
import java.awt.Dimension
import java.awt.event.KeyEvent
import javax.swing.JComponent

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-05
 */
class SnekView(dataView: BaseSnekDataView) : JComponent(), UIView<BaseSnekDataView> {

    override var representedObject: BaseSnekDataView by observing(dataView,
            didSet = { _, _ ->
                update()
            } )

//    private val fontSize = 10.float32Value


    fun update() {
        repaint()
    }

    override fun paint(g: Graphics?) {
        _prepareToPaint(g)?.let { context ->
            _paintScreen(context, representedObject.screen)
        }
    }


    private fun _prepareToPaint(g: Graphics?): SnekViewRenderContext? {
        super.paint(g)

        if (g == null) return null
        g.antiAlias = true
//        g.textAntiAlias = TextAntiAliasApproach.horizontalRGBStripe
        g.textAntiAlias = TextAntiAliasApproach.generic

        val frame = g.clipBounds.fractionValue
        val boardSize = representedObject.boardSize
        val stretchedScale = (frame.size.fractionValue / boardSize.fractionValue)
        val nonStretchedPixelSideLength = stretchedScale.minDimension
        val fontSize = nonStretchedPixelSideLength.float32Value

        g.font = g.font.deriveFont(fontSize)

        if (representedObject.debug) {
            g.color = awtColorFromHex("#f44336")
            g.drawString("DEBUG MODE", frame.minX, frame.minY - fontSize)
        }

        return SnekViewRenderContext(
                graphics = g,
                frame = frame,
                boardSize = boardSize,
                stretchedScale = stretchedScale,
                nonStretchedPixelSideLength = nonStretchedPixelSideLength
        )
    }


    private fun _paintScreen(renderContext: SnekViewRenderContext, screen: SnekScreen) {
        when (screen) {
            SnekScreen.ready -> _paintReadyScreen(renderContext)
            SnekScreen.playing -> _paintPlayingScreen(renderContext)
            SnekScreen.settings -> TODO()
            SnekScreen.scores -> TODO()
        }
    }


    private fun _paintPlayingScreen(renderContext: SnekViewRenderContext) {

        val (context, frame, boardSize, stretchedScale, nonStretchedPixelSideLength) = renderContext
        val dotSize = nonStretchedPixelSideLength / 3

        context.color = SystemColor.controlText.withAlphaComponent(0.1)

        (0..boardSize.width)
                .map { it.fractionValue * stretchedScale.width }
                .forEach {
                    context.drawLine(FractionLineSegment(
                            start = FractionPoint(x = it, y = 0.0),
                            end = FractionPoint(x = it, y = frame.height)
                    ))
                }
        (0..boardSize.height)
                .map { it.fractionValue * stretchedScale.height }
                .forEach {
                    context.drawLine(FractionLineSegment(
                            start = FractionPoint(x = 0.0, y = it),
                            end = FractionPoint(x = frame.width, y = it)
                    ))
                }

//        representedObject.path.safeReduce { previous, current ->
//            context.drawLine(previous * stretchedScale, current * stretchedScale)
//            /*return*/ current
//        }
        representedObject.path.segments
                .map { segment -> segment.fractionValue }
                .map { segment ->
                    Pair(segment, segment * stretchedScale.pairValue)
                }
                .forEach { (originalLineSegment, scaledLineSegment) ->
                    context.color = SystemColor.controlText

                    context.drawLine(scaledLineSegment)
                    context.fillCircle(radius = dotSize, center = scaledLineSegment.start)
                    if (representedObject.debug) {
                        context.color = Color(0.5f, 0.5f, 0.5f, 0.3f)
                        context.drawString(originalLineSegment.start.stringValue,
                                (scaledLineSegment.start.x + 2),
                                (scaledLineSegment.start.y + dotSize/2))
                    }
                }

        context.color = SystemColor.controlText
        representedObject.headPosition.let {
            context.color = awtColorFromHex("#4CAF50")
            context.fillCircle(radius = dotSize, center = it * stretchedScale.pairValue)
        }
        representedObject.applePosition.let {
            context.color = awtColorFromHex("#ff1744")
            context.fillCircle(radius = dotSize, center = it * stretchedScale.pairValue)
        }

        if (representedObject.debug) {
            context.drawString(representedObject.screen.name, 0, height)
        }
    }


    private fun _paintReadyScreen(renderContext: SnekViewRenderContext) {
        val (context, frame, _, _, nonStretchedPixelSideLength) = renderContext

        val readyString = "Ready!"

        context.font = Font.decode(Font.MONOSPACED).withSize(nonStretchedPixelSideLength * 3)
        context.color = awtColorFromHex("#1976D2")
        val readyMetrics = context.fontMetrics
        val readyStringYOffset = readyMetrics.ascent.fractionValue / 2.0
        val readyStringWidth = readyMetrics.stringWidth(readyString).fractionValue

        context.drawString(readyString, frame.midX - (readyStringWidth / 2.0), frame.midY + readyStringYOffset)

        representedObject.keymap.keyCodesForAction(SnekAction.start).firstOrNull?.let { keyCode ->
            val pressKeyPrefix = "Press"
            val pressKeyInfix = KeyEvent.getKeyText(keyCode).toUpperCase()
            val pressKeySuffix = "to play"

            val keyFontSize = nonStretchedPixelSideLength * 2

            context.font = Font.decode(Font.SANS_SERIF).withSize(keyFontSize)
            val keyMetrics = context.fontMetrics
            val keyStringYOffset = readyStringYOffset + (keyMetrics.ascent.fractionValue * 2)

            val keyPrefixStringWidth = keyMetrics.stringWidth(pressKeyPrefix).fractionValue
            val keyInfixStringWidth = keyMetrics.stringWidth(pressKeyInfix).fractionValue
            val keySuffixStringWidth = keyMetrics.stringWidth(pressKeySuffix).fractionValue

            val spacing = nonStretchedPixelSideLength
            val overallWidth = keyPrefixStringWidth + spacing + keyInfixStringWidth + spacing + keySuffixStringWidth

            val halfWidth = overallWidth / 2
            val pressKeyStart = frame.midX - halfWidth
            val keyStringYPosition = frame.midY + keyStringYOffset

            context.drawString(pressKeyPrefix, pressKeyStart, keyStringYPosition)
            context.drawString(pressKeySuffix, frame.midX + halfWidth - keySuffixStringWidth, keyStringYPosition)

            val keyPrefixEndX = pressKeyStart + keyPrefixStringWidth
            val halfSpacing = spacing / 2

            context.color = awtColorFromHex("#4CAF50")
            context.fillRoundRect(
                    (keyPrefixEndX + halfSpacing).roundedInt32Value,
                    (keyStringYPosition - keyFontSize).roundedInt32Value,
                    (keyInfixStringWidth + spacing).roundedInt32Value,
                    (keyFontSize + halfSpacing).roundedInt32Value,
                    (nonStretchedPixelSideLength).roundedInt32Value,
                    (nonStretchedPixelSideLength).roundedInt32Value)
            context.color = awtColorFromHex("#E8F5E9")
            context.drawString(pressKeyInfix, keyPrefixEndX + spacing, keyStringYPosition)
        }
    }


    override fun getPreferredSize(): Dimension {
        return (representedObject.boardSize * 8).awtValue
    }

    @Suppress("OverridingDeprecatedMember")
    override fun preferredSize(): Dimension {
        return preferredSize
    }
}



data class SnekViewRenderContext(
        val graphics: GraphicsContext,
        val frame: FractionRect,
        val boardSize: IntegerSize,
        val stretchedScale: FractionSize,
        val nonStretchedPixelSideLength: Fraction
)

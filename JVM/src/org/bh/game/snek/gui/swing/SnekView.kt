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


    private fun _paintScreen(renderContext: SnekViewRenderContext, screen: SnekScreen) = when (screen) {
        SnekScreen.ready -> _paintReadyScreen(renderContext)
        SnekScreen.playing -> _paintPlayingScreen(renderContext)
        SnekScreen.settings -> TODO()
        SnekScreen.scores -> _paintScoresScreen(renderContext)
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
        val (context, frame, _, _, _) = renderContext
        val smallestSideLength = min(frame.height, frame.width)
        val largeFontSize = smallestSideLength / 5
        val mediumFontSize = smallestSideLength / 12
        val readyString = "Ready!"

        context.font = Font.decode(Font.MONOSPACED).withSize(largeFontSize)
        context.color = awtColorFromHex("#1976D2")
        val readyMetrics = context.fontMetrics
        val readyStringYOffset = 0.0
        val readyStringWidth = readyMetrics.stringWidth(readyString).fractionValue

        context.drawString(readyString, frame.midX - (readyStringWidth / 2.0), frame.midY + readyStringYOffset)

        representedObject.keymap.keyCodesForAction(SnekAction.start).firstOrNull?.let { keyCode ->
            val pressKeyPrefix = "Press"
            val pressKeyInfix = KeyEvent.getKeyText(keyCode).toUpperCase()
            val pressKeySuffix = "to play"

            context.font = Font.decode(Font.SANS_SERIF).withSize(mediumFontSize)
            val keyMetrics = context.fontMetrics
            val keyStringYOffset = readyStringYOffset + (keyMetrics.ascent.fractionValue * 2)

            val keyPrefixStringWidth = keyMetrics.stringWidth(pressKeyPrefix).fractionValue
            val keyInfixStringWidth = keyMetrics.stringWidth(pressKeyInfix).fractionValue
            val keySuffixStringWidth = keyMetrics.stringWidth(pressKeySuffix).fractionValue

            val spacing = mediumFontSize / 2
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
                    (keyStringYPosition - mediumFontSize).roundedInt32Value,
                    (keyInfixStringWidth + spacing).roundedInt32Value,
                    (mediumFontSize + halfSpacing).roundedInt32Value,

                    (mediumFontSize / 4).roundedInt32Value,
                    (mediumFontSize / 4).roundedInt32Value)
            context.color = awtColorFromHex("#E8F5E9")
            context.drawString(pressKeyInfix, keyPrefixEndX + spacing, keyStringYPosition)
        }
    }


    private fun _paintScoresScreen(renderContext: SnekViewRenderContext) {
        val (context, frame, boardSize, stretchedScale, nonStretchedPixelSideLength) = renderContext
        val smallestSideLength = min(frame.height, frame.width)
        val largeFontSize = smallestSideLength / 5
        val mediumFontSize = smallestSideLength / 12
        val titleString = "Score"

        context.font = Font.decode(Font.SANS_SERIF).withSize(largeFontSize).withWeight(FontWeight.thin)

        val titleMetrics = context.fontMetrics
        val titleStringYOffset = titleMetrics.ascent
        val titleStringWidth = titleMetrics.stringWidth(titleString).fractionValue

        context.color = awtColorFromHex("#2196F3")
        context.fillRect(0.0, 0.0, frame.maxX, titleStringYOffset + (largeFontSize / 3.0))

        context.color = awtColorFromHex("#FFF")
        context.drawString(titleString, frame.midX - (titleStringWidth / 2.0), frame.minY + titleStringYOffset)


        val scoreString = representedObject.score.toString()
        val scoreMetrics = context.fontMetrics
        val scoreStringYOffset = scoreMetrics.ascent / 2
        val scoreStringWidth = scoreMetrics.stringWidth(scoreString).fractionValue

        context.color = awtColorFromHex("#388E3C")
        context.drawString(scoreString, frame.midX - (scoreStringWidth / 2), frame.midY + scoreStringYOffset)

        _paintKey(
                renderContext = renderContext,
                textBefore = "Back to start screen", action = SnekAction.start,
                size = _UISize.small,
                anchor = _UIAnchor.maxXmaxY(
                        xOffset = -_UISize.small.fontSize(renderContext),
                        yOffset = -_UISize.small.fontSize(renderContext)
                )
        )
    }


    private fun _paintKey(
            renderContext: SnekViewRenderContext,
            textBefore: String = "", action: SnekAction, textAfter: String = "",
            size: _UISize, anchor: _UIAnchor
    ) {
        representedObject.keymap.keyCodesForAction(action).firstOrNull?.let { keyCode ->
            val (context, parentFrame, boardSize, stretchedScale, nonStretchedPixelSideLength) = renderContext

            val prefix = textBefore
            val pressKeyInfix = KeyEvent.getKeyText(keyCode).toUpperCase()
            val suffix = textAfter

            val fontSize = size.fontSize(renderContext)

            context.font = Font.decode(Font.SANS_SERIF).withSize(fontSize)
            val keyMetrics = context.fontMetrics

            val keyPrefixStringWidth = keyMetrics.stringWidth(prefix).fractionValue
            val keyInfixStringWidth = keyMetrics.stringWidth(pressKeyInfix).fractionValue
            val keySuffixStringWidth = keyMetrics.stringWidth(suffix).fractionValue

            val spacing = fontSize / 2
            val halfSpacing = fontSize
            val overallWidth = keyPrefixStringWidth + spacing + keyInfixStringWidth + spacing + keySuffixStringWidth
            val textFrame = anchor
                    .reposition(
                            FractionRect(
                                    FractionPoint.zero,
                                    FractionSize(overallWidth, fontSize + halfSpacing))
                            , withinFrame = parentFrame)

            if (representedObject.debug) {
                context.drawRect(textFrame)
            }

//            val halfWidth = overallWidth / 2

            context.drawString(prefix, textFrame.minX, textFrame.minY)
            context.drawString(suffix, textFrame.maxX - keySuffixStringWidth, textFrame.minY)

            val keyPrefixEndX = textFrame.minX + keyPrefixStringWidth

            context.color = awtColorFromHex("#4CAF50")
            context.fillRoundRect(
                    (keyPrefixEndX + halfSpacing).roundedInt32Value,
                    (textFrame.minY - fontSize).roundedInt32Value,
                    (keyInfixStringWidth + spacing).roundedInt32Value,
                    (fontSize + halfSpacing).roundedInt32Value,

                    (fontSize / 4).roundedInt32Value,
                    (fontSize / 4).roundedInt32Value)
            context.color = awtColorFromHex("#E8F5E9")
            context.drawString(pressKeyInfix, keyPrefixEndX + spacing, textFrame.minY)
        }
    }


    private fun _UISize.fontSize(renderContext: SnekViewRenderContext): Fraction {
        val smallestSideLength = min(renderContext.frame.height, renderContext.frame.width)
        return when (this) {
            _UISize.large -> smallestSideLength / 5
            _UISize.normal -> smallestSideLength / 12
            _UISize.small -> smallestSideLength / 24
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



internal enum class _UISize {
    large,
    normal,
    small
}



internal sealed class _UIAnchor(
        /**
         * The distance on the X axis by which an element is offset from its parent, relative to this anchor point on
         * both the element and its parent
         */
        val xOffset: Fraction,
        /**
         * The distance on the Y axis by which an element is offset from its parent, relative to this anchor point on
         * both the element and its parent
         */
        val yOffset: Fraction) {
    class minXminY(xOffset: Fraction, yOffset: Fraction): _UIAnchor(xOffset, yOffset)
    class midXminY(xOffset: Fraction, yOffset: Fraction): _UIAnchor(xOffset, yOffset)
    class maxXminY(xOffset: Fraction, yOffset: Fraction): _UIAnchor(xOffset, yOffset)

    class minXmidY(xOffset: Fraction, yOffset: Fraction): _UIAnchor(xOffset, yOffset)
    class midXmidY(xOffset: Fraction, yOffset: Fraction): _UIAnchor(xOffset, yOffset)
    class maxXmidY(xOffset: Fraction, yOffset: Fraction): _UIAnchor(xOffset, yOffset)

    class minXmaxY(xOffset: Fraction, yOffset: Fraction): _UIAnchor(xOffset, yOffset)
    class midXmaxY(xOffset: Fraction, yOffset: Fraction): _UIAnchor(xOffset, yOffset)
    class maxXmaxY(xOffset: Fraction, yOffset: Fraction): _UIAnchor(xOffset, yOffset)


    /**
     * Moves the given rect to a new position within the given frame, using this anchor as a reference point for both rectangles, and offsetting from that reference point
     */
    fun reposition(rect: FractionRect, withinFrame: FractionRect): FractionRect {
        return when (this) {
            is _UIAnchor.minXminY -> rect.offset(xOffset + withinFrame.minX, yOffset + withinFrame.maxY)
            is _UIAnchor.midXminY -> TODO()
            is _UIAnchor.maxXminY -> TODO()
            is _UIAnchor.minXmidY -> TODO()
            is _UIAnchor.midXmidY -> TODO()
            is _UIAnchor.maxXmidY -> TODO()
            is _UIAnchor.minXmaxY -> TODO()
            is _UIAnchor.midXmaxY -> TODO()
            is _UIAnchor.maxXmaxY -> rect.copy(newOrigin = withinFrame.maxXmaxY)
                    .offset(xOffset - rect.width.clampToPositive, yOffset - rect.height.clampToPositive)
        }
    }
}

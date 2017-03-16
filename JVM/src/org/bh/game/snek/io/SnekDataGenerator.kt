package org.bh.game.snek.io

import org.bh.game.snek.gui.swing.Keymap
import org.bh.game.snek.state.*
import org.bh.tools.base.abstraction.Integer
import org.bh.tools.base.math.geometry.*
import org.bh.tools.base.math.int32Value
import org.bh.tools.base.util.TimeInterval
import org.bh.tools.io.logging.log

/**
 * @author Ben Leggiero
 * @since 2017-02-23
 */
class SnekDataGenerator {
    fun generateDefaultData(): SnekData {
        val boardSize = defaultBoardSize
        val path = defaultSnekPath(boardSize)
        return SnekData(defaultBoardSize,
                path,
                defaultLeaderboard,
                defaultScreen,
                defaultApple(boardSize, path),
                defaultKeymap,
                defaultDelayBetweenMovements,

                defaultDebug)
    }


    companion object {
        fun generateApplePosition(boardSize: IntegerSize, snekPath: IntegerPath): IntegerPoint =
                boardSize.randomPointNotOnPath(snekPath).integerValue
    }
}

private val defaultBoardSize = IntegerSize(width = 12, height = 9)
private fun defaultSnekPath(boardSize: IntegerSize) = IntegerPath(
        boardSize.midXminY.integerValue + IntegerPoint(0, 1),
        boardSize.midXminY.integerValue + IntegerPoint(0, 2),
        boardSize.midXminY.integerValue + IntegerPoint(0, 3),
        boardSize.midXminY.integerValue + IntegerPoint(0, 4),
        isClosed = false)
private val testSnekPath = IntegerPath(IntegerPoint(10, 10), IntegerPoint(15, 10), IntegerPoint(15, 7), isClosed = true)
private val defaultLeaderboard = Leaderboard<Leader, Integer>(mapOf())
private val defaultScreen = SnekScreen.ready
private fun defaultApple(boardSize: IntegerSize, snekPath: IntegerPath): IntegerPoint = SnekDataGenerator.generateApplePosition(boardSize, snekPath)
private val defaultKeymap = Keymap()
private val defaultDelayBetweenMovements: TimeInterval = 0.5
private val defaultDebug = false


private fun <
        NumberType : Number,
        PointType : ComputablePoint<NumberType>,
        SegmentType : ComputableLineSegment<NumberType, PointType>
        >
        ComputableSize<NumberType>.randomPointNotOnPath(snekPath: ComputablePath<NumberType, PointType, SegmentType>, maxAttempts: Integer = 100)
        : Point<NumberType> {
    
    return generateSequence(randomPoint()) { randomPoint() }
            .take(maxAttempts.int32Value)
            .firstOrNull { !snekPath.contains(it) }
            ?: {
        log.severe("Ran out of places to place a point in board: $this")
        randomPoint()
    }()
}

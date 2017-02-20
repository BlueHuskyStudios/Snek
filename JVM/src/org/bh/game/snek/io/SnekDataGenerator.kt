package org.bh.game.snek.io

import org.bh.game.snek.state.*
import org.bh.tools.base.abstraction.Integer
import org.bh.tools.base.math.geometry.*

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
                defaultDebug)
    }
}

private val defaultBoardSize = IntegerSize(width = 32, height = 32)
private fun defaultSnekPath(boardSize: IntegerSize) = IntegerPath(boardSize.midXmidY.integerValue, boardSize.midXmidY.integerValue + IntegerPoint(0, 1), isClosed = false)
private val testSnekPath = IntegerPath(IntegerPoint(10, 10), IntegerPoint(15, 10), IntegerPoint(15, 7), isClosed = true)
private val defaultLeaderboard = Leaderboard<Leader, Integer>(mapOf())
private val defaultScreen = SnekScreen.ready
private fun defaultApple(boardSize: IntegerSize, snekPath: IntegerPath): IntegerPoint = boardSize.randomPointNotOnPath(snekPath).integerValue
private val defaultDebug = false


private fun <
        NumberType : Number,
        PointType : ComputablePoint<NumberType>,
        SegmentType : ComputableLineSegment<NumberType, PointType>
        >
        ComputableSize<NumberType>.randomPointNotOnPath(snekPath: ComputablePath<NumberType, PointType, SegmentType>)
        : Point<NumberType> {
    var point = randomPoint
    while (snekPath.contains(point)) {
        point = randomPoint
    }
    return point
}

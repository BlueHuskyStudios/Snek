package org.bh.game.snek.state

import org.bh.game.snek.state.SnekScreen.ready
import org.bh.tools.base.abstraction.Integer
import org.bh.tools.base.collections.a
import org.bh.tools.base.math.geometry.*
import org.bh.tools.base.struct.DataAccessor

class SnekDataAccessor : DataAccessor<SnekData, SnekDataAccessDetails?, SnekDataAccessStatus?> {

    override fun accessData(details: SnekDataAccessDetails?,
                            didAccessData: (accessedData: SnekData?, status: SnekDataAccessStatus?) -> Unit) {
        // TODO: serialize/deserialize
        didAccessData(SnekData(), null)
    }

//    companion object
}

fun SnekData(): SnekData {
    val snekPath: Array<IntegerPoint> = if (SnekMetaGameState.shared.debug) testSnekPath else defaultSnekPath
    return SnekData(defaultBoardSize,
            snekPath.asList().toTypedArray(),
            defaultLeaderboard,
            defaultScreen,
            defaultApple(defaultBoardSize, snekPath))
}

private val defaultBoardSize = IntegerSize(width = 32, height = 32)
private val defaultSnekPath: Array<IntegerPoint> = emptyArray()
private val testSnekPath = a[IntegerPoint(10, 10), IntegerPoint(15, 10), IntegerPoint(15, 7)]
private val defaultLeaderboard = Leaderboard<Leader, Integer>(mapOf())
private val defaultScreen = ready
private fun defaultApple(boardSize: IntegerSize, snekPath: Array<IntegerPoint>): IntPoint = boardSize.randomPoint.integerValue

class SnekDataAccessDetails // TODO
class SnekDataAccessStatus // TODO
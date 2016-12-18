package org.bh.game.snek.state

import org.bh.game.snek.state.SnekScreen.ready
import org.bh.tools.base.abstraction.BHInt
import org.bh.tools.base.abstraction.Int64
import org.bh.tools.base.math.geometry.*
import org.bh.tools.base.struct.DataAccessor

class SnekDataAccessor : DataAccessor<SnekData, SnekDataAccessDetails?, SnekDataAccessStatus?> {

    override fun accessData(details: SnekDataAccessDetails?,
                            didAccessData: (accessedData: SnekData?, status: SnekDataAccessStatus?) -> Unit) {
        didAccessData(SnekData_new(), null)
    }

//    companion object {
//
//    }
}

fun SnekData_new(): SnekData {
    val snekPath: Array<BHIntPoint> = if (SnekMetaGameState.shared.debug) testSnekPath else defaultSnekPath
    return SnekData(defaultBoardSize,
            snekPath.asList().toTypedArray(),
            defaultLeaderboard,
            defaultScreen,
            defaultApple(defaultBoardSize, snekPath))
}

private val defaultBoardSize = IntSize(width = 32, height = 32)
private val defaultSnekPath: Array<BHIntPoint> = emptyArray()
private val testSnekPath: Array<BHIntPoint> = arrayOf(IntPoint(10, 10), IntPoint(15, 10), IntPoint(15, 7))
private val defaultLeaderboard = Leaderboard<Leader, BHInt>(mapOf())
private val defaultScreen = ready
private fun defaultApple(boardSize: BHIntSize, snekPath: Array<BHIntPoint>): IntPoint = boardSize.randomPoint.integerValue

class SnekDataAccessDetails // TODO
class SnekDataAccessStatus // TODO
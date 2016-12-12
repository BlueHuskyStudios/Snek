package org.bh.game.snek.state

import org.bh.game.snek.state.SnekScreen.ready
import org.bh.tools.base.abstraction.BHInt
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

fun SnekData_new(): SnekData =
        SnekData(defaultBoardSize,
                if (SnekMetaGameState.shared.debug) testSnekPath else defaultSnekPath,
                defaultLeaderboard,
                defaultScreen,
                defaultApple(defaultBoardSize))

private val defaultBoardSize = IntSize(width = 32, height = 32)
private val defaultSnekPath: Array<BHIntPoint> = emptyArray()
private val testSnekPath: Array<BHIntPoint> = arrayOf(IntPoint(10, 10), IntPoint(15, 10), IntPoint(15, 7))
private val defaultLeaderboard = Leaderboard<Leader, BHInt>(mapOf())
private val defaultScreen = ready
private fun defaultApple(boardSize: BHIntSize): IntPoint = boardSize.randomPoint.integerValue

class SnekDataAccessDetails // TODO
class SnekDataAccessStatus // TODO
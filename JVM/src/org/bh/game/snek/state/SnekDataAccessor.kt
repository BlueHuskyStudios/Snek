package org.bh.game.snek.state

import org.bh.game.snek.state.SnekScreen.ready
import org.bh.tools.base.struct.DataAccessor
import org.bh.tools.base.struct.coord.IntPoint
import org.bh.tools.base.struct.coord.IntSize
import org.bh.tools.base.struct.coord.Size
import org.bh.tools.base.struct.coord.randomPoint

class SnekDataAccessor() : DataAccessor<SnekData, SnekDataAccessDetails?, SnekDataAccessStatus?> {

    override fun accessData(details: SnekDataAccessDetails?,
                            didAccessData: (accessedData: SnekData?, status: SnekDataAccessStatus?) -> Unit) {
        didAccessData(newData, null)
    }

    companion object {
        val shared = SnekDataAccessor()

        val newData: SnekData by lazy {
            SnekData(defaultBoardSize,
                    if (SnekMetaGameState.shared.debug) testSnekPath else defaultSnekPath,
                    defaultLeaderboard,
                    defaultScreen,
                    defaultApple(defaultBoardSize))
        }

        private val defaultBoardSize = Size(width = 32, height = 32)
        private val defaultSnekPath: Array<IntPoint> = emptyArray()
        private val testSnekPath: Array<IntPoint> = arrayOf(IntPoint(10, 10), IntPoint(15, 10), IntPoint(15, 7))
        private val defaultLeaderboard = Leaderboard<Leader, Int>(mapOf())
        private val defaultScreen = ready
        private fun defaultApple(boardSize: IntSize): IntPoint = boardSize.randomPoint
    }
}

class SnekDataAccessDetails // TODO
class SnekDataAccessStatus // TODO
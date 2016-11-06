@file:Suppress("unused")

package org.bh.game.snek.state

import org.bh.tools.base.collections.deepEquals
import org.bh.tools.base.struct.Data
import org.bh.tools.base.struct.DataAccessor
import org.bh.tools.base.struct.DataView
import org.bh.tools.base.struct.DataViewController
import org.bh.tools.base.struct.coord.IntPoint
import org.bh.tools.base.struct.coord.IntSize
import java.util.*

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * The state of Snek
 *
 * @author Kyli Rouge
 * @since 2016-10-30
 */
class SnekGameState(override val dataView: BaseSnekDataView): DataViewController<SnekData, BaseSnekDataView> {
    val snek: BaseSnekDataView get() = dataView
}

data class BaseSnekDataView(override val data: SnekData): DataView<SnekData> {
    val boardSize: IntSize get() = data.boardSize
    val snek: Array<IntPoint> get() = data.snekPath
    val leaderboard: Leaderboard<Leader, Int> get() = data.leaderboard
}

data class SnekData(
        val boardSize: IntSize,
        val snekPath: Array<IntPoint>,
        val leaderboard: Leaderboard<Leader, Int>
    ): Data {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SnekData) return false
        if (other.boardSize != this.boardSize) return false
        if (!other.snekPath.deepEquals(this.snekPath)) return false
        return true
    }

    override fun hashCode(): Int{
        var result = boardSize.hashCode()
        result = 31 * result + Arrays.hashCode(snekPath)
        return result
    }
}

class SnekDataAccessor(): DataAccessor<SnekData, SnekDataAccessDetails?, SnekDataAccessStatus?> {
    override fun accessData(details: SnekDataAccessDetails?,
                            didAccessData: (accessedData: SnekData?, status: SnekDataAccessStatus?) -> Unit) {
        didAccessData(newData, null)
    }

    companion object {
        val shared = SnekDataAccessor()

        val newData: SnekData get() = SnekData(defaultBoardSize, defaultSnekPath, defaultLeaderboard)

        private val defaultBoardSize = IntSize(width = 32, height = 32)
        private val defaultSnekPath: Array<IntPoint> = emptyArray()
        private val  defaultLeaderboard = Leaderboard<Leader, Int>(mapOf())
    }
}

class SnekDataAccessDetails // TODO
class SnekDataAccessStatus // TODO


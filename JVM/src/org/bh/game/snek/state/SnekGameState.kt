@file:Suppress("unused")

package org.bh.game.snek.state

import org.bh.tools.base.collections.deepEquals
import org.bh.tools.base.state.State
import org.bh.tools.base.state.StateChange
import org.bh.tools.base.struct.Data
import org.bh.tools.base.struct.DataAccessor
import org.bh.tools.base.struct.DataView
import org.bh.tools.base.struct.DataViewController
import org.bh.tools.base.struct.coord.IntPoint
import org.bh.tools.base.struct.coord.IntSize
import org.bh.tools.base.struct.coord.Size
import org.bh.tools.base.struct.coord.randomPoint
import java.util.*

// TODO: Separate into individual files

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * The state of Snek
 *
 * @author Kyli Rouge
 * @since 2016-10-30
 */
class SnekGameState(override val dataView: BaseSnekDataView) : DataViewController<SnekData, BaseSnekDataView>,
        State<SnekGameState> {
    override val changeValue: StateChange<SnekGameState>
        get() = SnekGameStateChange(BaseSnekDataViewChange(dataView))

    val snek: BaseSnekDataView get() = dataView
}

class SnekGameStateChange(val snekDataChange: BaseSnekDataViewChange): StateChange<SnekGameState> {
    constructor(snekDataChange: SnekGameState) : this(BaseSnekDataViewChange(snekDataChange.dataView))
}

data class BaseSnekDataView(override val data: SnekData) : DataView<SnekData> {
    val boardSize: IntSize get() = data.boardSize
    val snek: Array<IntPoint> get() = data.snekPath
    val leaderboard: Leaderboard<Leader, Int> get() = data.leaderboard
    val screen: SnekScreen get() = data.screen
    val apple: IntPoint get() = data.apple
}

typealias BaseSnekDataViewChange = SnekDataChange

data class SnekData(
        val boardSize: IntSize,
        val snekPath: Array<IntPoint>,
        val leaderboard: Leaderboard<Leader, Int>,
        val screen: SnekScreen,
        val apple: IntPoint
) : Data {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SnekData) return false
        if (other.boardSize != this.boardSize) return false
        if (!other.snekPath.deepEquals(this.snekPath)) return false
        if (other.leaderboard != this.leaderboard) return false
        if (other.screen != this.screen) return false
        if (other.apple != this.apple) return false
        return true
    }

    override fun hashCode(): Int {
        var result = boardSize.hashCode()
        result = 31 * result + (snekPath.let { Arrays.hashCode(it) })
        result = 31 * result + (leaderboard.hashCode())
        result = 31 * result + (screen.hashCode())
        result = 31 * result + (apple.hashCode())
        return result
    }
}

data class SnekDataChange(
        val boardSize: IntSize?,
        val snekPath: Array<IntPoint>?,
        val leaderboard: Leaderboard<Leader, Int>?,
        val screen: SnekScreen?,
        val apple: IntPoint?
) : DataView<SnekDataChange>, Data {

    constructor(exactly: BaseSnekDataView): this(
            boardSize = exactly.boardSize,
            snekPath = exactly.snek,
            leaderboard = exactly.leaderboard,
            screen = exactly.screen,
            apple = exactly.apple)

    /**
     * In this case, this change is its own data. So, this just returns `this`
     */
    override val data: SnekDataChange get() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SnekDataChange) return false
        if (other.boardSize != this.boardSize) return false
        if (other.snekPath != null) {
            if (this.snekPath != null) {
                if (!other.snekPath.deepEquals(this.snekPath)) return false
            } else {
                return false
            }
        } else if (this.snekPath != null) { // && other.snekPath == null
            return false
        }
        if (other.leaderboard != this.leaderboard) return false
        if (other.screen != this.screen) return false
        if (other.apple != this.apple) return false
        return true
    }

    override fun hashCode(): Int {
        var result = boardSize?.hashCode() ?: 0
        result = 31 * result + (snekPath?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + (leaderboard?.hashCode() ?: 0)
        result = 31 * result + (screen?.hashCode() ?: 0)
        result = 31 * result + (apple?.hashCode() ?: 0)
        return result
    }
}

/**
 * Represents the screens of a Snek game
 */
enum class SnekScreen {
    /**
     * Ready to play; may represent pause screen
     */
    ready,
    /**
     * Currently playing
     */
    playing,
    /**
     * The settings screen
     */
    settings,
    /**
     * Viewing the leaderboard
     */
    scores
}

class SnekDataAccessor() : DataAccessor<SnekData, SnekDataAccessDetails?, SnekDataAccessStatus?> {

    override fun accessData(details: SnekDataAccessDetails?,
                            didAccessData: (accessedData: SnekData?, status: SnekDataAccessStatus?) -> Unit) {
        didAccessData(newData, null)
    }

    companion object {
        val shared = SnekDataAccessor()

        val newData: SnekData
            get() = SnekData(defaultBoardSize,
                    if (SnekMetaGameState.shared.debug) testSnekPath else defaultSnekPath,
                    defaultLeaderboard,
                    defaultScreen,
                    defaultApple(defaultBoardSize))

        private val defaultBoardSize = Size(width = 32, height = 32)
        private val defaultSnekPath: Array<IntPoint> = emptyArray()
        private val testSnekPath: Array<IntPoint> = arrayOf(IntPoint(10, 10), IntPoint(15, 10), IntPoint(15, 7))
        private val defaultLeaderboard = Leaderboard<Leader, Int>(mapOf())
        private val defaultScreen = SnekScreen.ready
        private fun defaultApple(boardSize: IntSize): IntPoint = boardSize.randomPoint
    }
}

class SnekDataAccessDetails // TODO
class SnekDataAccessStatus // TODO

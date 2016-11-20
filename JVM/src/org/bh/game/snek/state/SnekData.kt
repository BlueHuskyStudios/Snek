package org.bh.game.snek.state

import org.bh.tools.base.collections.deepEquals
import org.bh.tools.base.state.ChangeableState
import org.bh.tools.base.state.StateChange
import org.bh.tools.base.struct.Data
import org.bh.tools.base.struct.DataView
import org.bh.tools.base.struct.coord.IntPoint
import org.bh.tools.base.struct.coord.IntSize
import java.util.*

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * The lowest structure of Snek
 *
 * @author Kyli Rouge
 * @since 2016-11-20
 */
data class SnekData(
        val boardSize: IntSize,
        val snekPath: Array<IntPoint>,
        val leaderboard: Leaderboard<Leader, Int>,
        val screen: SnekScreen,
        val apple: IntPoint
) : Data, ChangeableState<SnekData, SnekDataChange> {

    override val changeValue: SnekDataChange
        get() = SnekDataChange(this)

    override fun applyingChange(change: SnekDataChange): SnekData
            = SnekData(
            boardSize = change.boardSize ?: this.boardSize,
            apple = change.apple ?: this.apple,
            leaderboard = change.leaderboard ?: this.leaderboard,
            screen = change.screen ?: this.screen,
            snekPath = change.snekPath ?: this.snekPath)

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



/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * A change to the lowest structure of Snek
 *
 * @author Kyli Rouge
 * @since 2016-11-20
 */
data class SnekDataChange(
        val boardSize: IntSize? = null,
        val snekPath: Array<IntPoint>? = null,
        val leaderboard: Leaderboard<Leader, Int>? = null,
        val screen: SnekScreen? = null,
        val apple: IntPoint? = null)
    : DataView<SnekDataChange>, Data, StateChange<SnekDataChange, SnekData> {

    constructor(exactly: BaseSnekDataView) : this(
            boardSize = exactly.boardSize,
            snekPath = exactly.snek,
            leaderboard = exactly.leaderboard,
            screen = exactly.screen,
            apple = exactly.apple)

    constructor(exactly: SnekData) : this(
            boardSize = exactly.boardSize,
            snekPath = exactly.snekPath,
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

    override fun applyingChange(change: SnekDataChange): SnekDataChange
            = SnekDataChange(
            boardSize = this.boardSize ?: change.boardSize,
            apple = this.apple ?: change.apple,
            leaderboard = this.leaderboard ?: change.leaderboard,
            screen = this.screen ?: change.screen,
            snekPath = this.snekPath ?: change.snekPath)
}

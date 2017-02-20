package org.bh.game.snek.state

import org.bh.tools.base.abstraction.Integer
import org.bh.tools.base.math.geometry.*
import org.bh.tools.base.state.ChangeableState
import org.bh.tools.base.state.StateChange
import org.bh.tools.base.struct.Data
import org.bh.tools.base.struct.DataView



/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * The lowest structure of Snek
 *
 * @author Kyli Rouge
 * @since 2016-11-20
 */
data class SnekData(
        val boardSize: IntegerSize,
        val snekPath: IntegerPath,
        val leaderboard: Leaderboard<Leader, Integer>,
        val screen: SnekScreen,
        val apple: IntegerPoint,
        val debug: Boolean
) : Data, ChangeableState<SnekData, SnekDataChange> {

//    var debug: Boolean by observing(false,
//            didSet = { old, new ->
//                log.info("\r\n\r\n\r\n\t\t\tDebug mode changed from $old to $new\r\n\r\n\r\n\r\n")
//            })

    override val changeValue: SnekDataChange
        get() = SnekDataChange(this)


    override fun applyingChange(change: SnekDataChange): SnekData
            = SnekData(
            boardSize = change.boardSize ?: this.boardSize,
            apple = change.apple ?: this.apple,
            leaderboard = change.leaderboard ?: this.leaderboard,
            screen = change.screen ?: this.screen,
            snekPath = change.snekPath ?: this.snekPath,
            debug = change.debug ?: this.debug)


    @Suppress("RedundantIf")
    override fun equals(other: Any?): Boolean
            = if (this === other) true
            else if (other !is SnekData) false
            else if (other.boardSize != this.boardSize) false
            else if (other.snekPath != this.snekPath) false
            else if (other.leaderboard != this.leaderboard) false
            else if (other.screen != this.screen) false
            else if (other.apple != this.apple) false
            else if (other.debug != this.debug) false
            else true


    override fun hashCode(): Int {
        var result = boardSize.hashCode()
        result = 31 * result + (snekPath.hashCode())
        result = 31 * result + (leaderboard.hashCode())
        result = 31 * result + (screen.hashCode())
        result = 31 * result + (apple.hashCode())
        return result
    }

    companion object
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
        val boardSize: IntegerSize? = null,
        val snekPath: IntegerPath? = null,
        val leaderboard: Leaderboard<Leader, Integer>? = null,
        val screen: SnekScreen? = null,
        val apple: IntegerPoint? = null,
        val debug: Boolean? = null)
    : DataView<SnekDataChange>, Data, StateChange<SnekDataChange, SnekData> {

    constructor(exactly: BaseSnekDataView) : this(
            boardSize = exactly.boardSize,
            snekPath = exactly.path,
            leaderboard = exactly.leaderboard,
            screen = exactly.screen,
            apple = exactly.apple,
            debug = exactly.debug)

    constructor(exactly: SnekData) : this(
            boardSize = exactly.boardSize,
            snekPath = exactly.snekPath,
            leaderboard = exactly.leaderboard,
            screen = exactly.screen,
            apple = exactly.apple,
            debug = exactly.debug)

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
                if (other.snekPath != this.snekPath) return false
            } else {
                return false
            }
        } else if (this.snekPath != null) { // && other.snekPath == null
            return false
        }
        if (other.leaderboard != this.leaderboard) return false
        if (other.screen != this.screen) return false
        if (other.apple != this.apple) return false
        if (other.debug != this.debug) return false
        return true
    }

    override fun hashCode(): Int {
        var result = boardSize?.hashCode() ?: 0
        result = 31 * result + (snekPath?.hashCode() ?: 0)
        result = 31 * result + (leaderboard?.hashCode() ?: 0)
        result = 31 * result + (screen?.hashCode() ?: 0)
        result = 31 * result + (apple?.hashCode() ?: 0)
        result = 31 * result + (debug?.hashCode() ?: 0)
        return result
    }

    override fun applyingChange(change: SnekDataChange): SnekDataChange
            = SnekDataChange(
            boardSize = change.boardSize ?: this.boardSize,
            apple = change.apple ?: this.apple,
            leaderboard = change.leaderboard ?: this.leaderboard,
            screen = change.screen ?: this.screen,
            snekPath = change.snekPath ?: this.snekPath,
            debug = change.debug ?: this.debug)
}

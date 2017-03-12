package org.bh.game.snek.state

import org.bh.game.snek.gui.swing.Keymap
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
        val keymap: Keymap,

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
            keymap = change.keymap ?: this.keymap,

            debug = change.debug ?: this.debug)

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
        val keymap: Keymap? = null,

        val debug: Boolean? = null)
    : DataView<SnekDataChange>, Data, StateChange<SnekDataChange, SnekData> {

    /**
     * In this case, this change is its own data. So, this just returns `this`
     */
    override val data: SnekDataChange get() = this


    constructor(exactly: BaseSnekDataView) : this(
            boardSize = exactly.boardSize,
            snekPath = exactly.path,
            leaderboard = exactly.leaderboard,
            screen = exactly.screen,
            apple = exactly.apple,
            keymap = exactly.keymap,

            debug = exactly.debug)


    constructor(exactly: SnekData) : this(
            boardSize = exactly.boardSize,
            snekPath = exactly.snekPath,
            leaderboard = exactly.leaderboard,
            screen = exactly.screen,
            apple = exactly.apple,
            keymap = exactly.keymap,

            debug = exactly.debug)


    override fun applyingChange(change: SnekDataChange): SnekDataChange
            = SnekDataChange(
            boardSize = change.boardSize ?: this.boardSize,
            snekPath = change.snekPath ?: this.snekPath,
            leaderboard = change.leaderboard ?: this.leaderboard,
            screen = change.screen ?: this.screen,
            apple = change.apple ?: this.apple,
            keymap = change.keymap ?: this.keymap,

            debug = change.debug ?: this.debug)
}

package org.bh.game.snek.state

import org.bh.game.snek.gui.swing.Keymap
import org.bh.tools.base.abstraction.Integer
import org.bh.tools.base.math.geometry.*
import org.bh.tools.base.state.ChangeableState
import org.bh.tools.base.state.StateChange
import org.bh.tools.base.struct.DataView

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * The basic Snek data view, which represents snek data one-for-one
 *
 * @author Kyli Rouge
 * @since 2016-11-20
 */
data class BaseSnekDataView(override val data: SnekData)
: DataView<SnekData>, ChangeableState<BaseSnekDataView, BaseSnekDataViewChange> {

    inline val boardSize get() = data.boardSize
    inline val path get() = data.snekPath
    inline val leaderboard get() = data.leaderboard
    inline val screen get() = data.screen
    inline val applePosition get() = data.applePosition
    inline val keymap get() = data.keymap
    inline val delayBetweenMovements get() = data.delayBetweenMovements

    inline val debug get() = data.debug

    override val changeValue by lazy { BaseSnekDataViewChange(data.changeValue) }

    override fun applyingChange(change: BaseSnekDataViewChange): BaseSnekDataView
        = BaseSnekDataView(data.applyingChange(change.dataChange))

    val headPosition: IntegerPoint by lazy { (path.points.lastOrNull() ?: boardSize.randomPoint()).integerValue }
}



/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * A one-to-one view of the change to the basic Snek data
 *
 * @author Kyli Rouge
 * @since 2016-11-20
 */
class BaseSnekDataViewChange(val dataChange: SnekDataChange) : StateChange<BaseSnekDataViewChange, BaseSnekDataView> {
    constructor(
            boardSize: IntegerSize? = null,
            snekPath: IntegerPath? = null,
            leaderboard: Leaderboard<Leader, Integer>? = null,
            screen: SnekScreen? = null,
            applePosition: IntegerPoint? = null,
            keymap: Keymap? = null,

            debug: Boolean? = null)
        : this(SnekDataChange(
            boardSize = boardSize,
            snekPath = snekPath,
            leaderboard = leaderboard,
            screen = screen,
            applePosition = applePosition,
            keymap = keymap,

            debug = debug))

    override fun applyingChange(change: BaseSnekDataViewChange): BaseSnekDataViewChange
        = BaseSnekDataViewChange(dataChange.applyingChange(change.dataChange))
}
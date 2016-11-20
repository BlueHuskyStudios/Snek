package org.bh.game.snek.state

import org.bh.tools.base.state.ChangeableState
import org.bh.tools.base.state.StateChange
import org.bh.tools.base.struct.DataView
import org.bh.tools.base.struct.coord.IntPoint
import org.bh.tools.base.struct.coord.IntSize

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

    val boardSize: IntSize get() = data.boardSize
    val snek: Array<IntPoint> get() = data.snekPath
    val leaderboard: Leaderboard<Leader, Int> get() = data.leaderboard
    val screen: SnekScreen get() = data.screen
    val apple: IntPoint get() = data.apple

    override val changeValue: BaseSnekDataViewChange
        get() = BaseSnekDataViewChange(data.changeValue)

    override fun applyingChange(change: BaseSnekDataViewChange): BaseSnekDataView
        = BaseSnekDataView(data.applyingChange(change.dataChange))
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
            boardSize: IntSize? = null,
            snekPath: Array<IntPoint>? = null,
            leaderboard: Leaderboard<Leader, Int>? = null,
            screen: SnekScreen? = null,
            apple: IntPoint? = null)
        : this(SnekDataChange(
            boardSize = boardSize,
            snekPath = snekPath,
            leaderboard = leaderboard,
            screen = screen,
            apple = apple))

    override fun applyingChange(change: BaseSnekDataViewChange): BaseSnekDataViewChange
        = BaseSnekDataViewChange(dataChange.applyingChange(change.dataChange))
}
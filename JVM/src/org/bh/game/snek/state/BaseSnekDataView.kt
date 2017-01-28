package org.bh.game.snek.state

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

    val boardSize: IntegerSize by lazy { data.boardSize }
    val path: IntegerPath by lazy { IntegerPath(data.snekPath.asList()) }
    val leaderboard: Leaderboard<Leader, Integer> by lazy { data.leaderboard }
    val screen: SnekScreen by lazy { data.screen }
    val apple: IntegerPoint by lazy { data.apple }

    override val changeValue: BaseSnekDataViewChange by lazy { BaseSnekDataViewChange(data.changeValue) }

    override fun applyingChange(change: BaseSnekDataViewChange): BaseSnekDataView
        = BaseSnekDataView(data.applyingChange(change.dataChange))

    val headPosition: IntegerPoint by lazy { (path.points.lastOrNull() ?: boardSize.randomPoint).integerValue }
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
            apple: IntegerPoint? = null)
        : this(SnekDataChange(
            boardSize = boardSize,
            snekPath = snekPath?.points?.toTypedArray(),
            leaderboard = leaderboard,
            screen = screen,
            apple = apple))

    override fun applyingChange(change: BaseSnekDataViewChange): BaseSnekDataViewChange
        = BaseSnekDataViewChange(dataChange.applyingChange(change.dataChange))
}
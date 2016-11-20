@file:Suppress("unused")

package org.bh.game.snek.state

import org.bh.tools.base.state.ChangeableState
import org.bh.tools.base.state.StateChange
import org.bh.tools.base.struct.DataViewController
import org.bh.tools.base.struct.coord.IntPoint
import org.bh.tools.base.struct.coord.IntSize

// TODO: Separate into individual files

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * The state of Snek
 *
 * @author Kyli Rouge
 * @since 2016-10-30
 */
class SnekDataViewController(override val dataView: BaseSnekDataView)
        : DataViewController<SnekData, BaseSnekDataView>, ChangeableState<SnekDataViewController, SnekGameStateChange> {

    constructor(dataView: SnekData) : this(BaseSnekDataView(dataView))

    override val changeValue: SnekGameStateChange
        get() = SnekGameStateChange(BaseSnekDataViewChange(dataView.data.changeValue))

    val snek: BaseSnekDataView get() = dataView

    override fun applyingChange(change: SnekGameStateChange): SnekDataViewController {
        return SnekDataViewController(dataView.applyingChange(change.baseChange))
    }
}



/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * A change to the state of Snek
 *
 * @author Kyli Rouge
 * @since 2016-10-30
 */
class SnekGameStateChange(val baseChange: BaseSnekDataViewChange): StateChange<SnekGameStateChange, SnekDataViewController> {
    constructor(snekDataViewController: SnekDataViewController) : this(BaseSnekDataViewChange(snekDataViewController.dataView.data.changeValue))

    constructor(
            boardSize: IntSize? = null,
            snekPath: Array<IntPoint>? = null,
            leaderboard: Leaderboard<Leader, Int>? = null,
            screen: SnekScreen? = null,
            apple: IntPoint? = null)
        : this(BaseSnekDataViewChange(
            boardSize = boardSize,
            snekPath = snekPath,
            leaderboard = leaderboard,
            screen = screen,
            apple = apple))

    override fun applyingChange(change: SnekGameStateChange): SnekGameStateChange
            = SnekGameStateChange(this.baseChange.applyingChange(change.baseChange))
}

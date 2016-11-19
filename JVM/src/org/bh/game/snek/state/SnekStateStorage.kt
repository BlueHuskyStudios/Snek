package org.bh.game.snek.state

import org.bh.tools.base.collections.DeltaStack
import org.bh.tools.base.collections.HistoryArray
import org.bh.tools.base.collections.HistoryArraySelectionMode.SelectNewlyPushed
import org.bh.tools.base.state.StateStore

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * Stores the state of the Snek.
 *
 * sss~
 *
 * @author Kyli Rouge
 * @since 2016-10-31
 */
class SnekStateStorage: DeltaStack<SnekGameState, SnekGameStateChange>(),
        StateStore<SnekGameState, SnekGameStateChange> {
    override fun pushState(newState: SnekGameStateChange) {
        super.push(newState)
    }

    override fun popState(): SnekGameState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun currentState(): SnekGameState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun flattenState() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
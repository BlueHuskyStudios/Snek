package org.bh.game.snek.game.logic

import org.bh.game.snek.gui.swing.SnekAction
import org.bh.game.snek.state.SnekGameState
import org.bh.game.snek.state.SnekGameStateChange
import org.bh.tools.base.state.StateController
import org.bh.tools.base.state.StateMutator
import java.util.*

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * Like a Flux store of [SnekGameState]s
 *
 * @author Kyli Rouge
 * @since 2016-11-09
 */
class SnekGameStateController(initialState: SnekGameState) : StateController<SnekGameState, SnekAction> {

    val mutator = SnekGameStateMutator()
    var states = DeltaStack<SnekGameStateChange>()

    override fun currentState(): SnekGameState {
        return state
    }

    override fun mutate(action: SnekAction) {
        mutator.mutate(currentState(), action)
    }
}

class SnekGameStateMutator: StateMutator<SnekGameState, SnekAction, SnekGameStateChange> {
    override fun mutate(state: SnekGameState, action: SnekAction): SnekGameStateChange {
        return SnekGameStateChange(state)
    }
}

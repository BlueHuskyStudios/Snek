package org.bh.game.snek.game.logic

import org.bh.game.snek.gui.swing.SnekAction
import org.bh.game.snek.state.*
import org.bh.tools.base.state.StateController
import org.bh.tools.base.state.StateMutator

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-09
 */
class SnekGameStateController : StateController<SnekGameState, SnekAction> {

    val mutator = SnekGameStateMutator()

    override fun currentState(): SnekGameState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mutate(action: SnekAction) {
        mutator.mutate(currentState(), action)
    }
}

class SnekGameStateMutator: StateMutator<SnekGameState, SnekGameStateChange, SnekAction> {
    override fun mutate(state: SnekGameState, action: SnekAction): SnekGameStateChange {
        return SnekGameStateChange(state)
    }
}

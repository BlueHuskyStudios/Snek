package org.bh.game.snek.game.logic

import org.bh.game.snek.gui.swing.SnekAction
import org.bh.game.snek.state.*
import org.bh.game.snek.state.SnekScreen.playing
import org.bh.game.snek.state.SnekScreen.ready
import org.bh.tools.base.state.StateController
import org.bh.tools.base.state.StateMutator

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * Like a Flux store of [SnekDataViewController]s
 *
 * @author Kyli Rouge
 * @since 2016-11-09
 */
class SnekGameStateController(initialState: SnekDataViewController) : StateController<SnekDataViewController, SnekAction> {

    val mutator = SnekGameStateMutator()
    var states = SnekStateStorage(initialState)

    override fun currentState(): SnekDataViewController {
        return states.currentState()
    }

    override fun mutate(action: SnekAction) {
        states.pushState(mutator.mutate(currentState(), action))
    }
}

class SnekGameStateMutator: StateMutator<SnekDataViewController, SnekAction, SnekGameStateChange> {
    override fun mutate(state: SnekDataViewController, action: SnekAction): SnekGameStateChange {
        return when (action) {
            SnekAction.pause -> _pauseStateChange
            SnekAction.unpause -> _unpauseStateChange
            SnekAction.start -> TODO()
            SnekAction.moveUp -> TODO()
            SnekAction.moveDown -> TODO()
            SnekAction.moveRight -> TODO()
            SnekAction.moveLeft -> TODO()
        }
    }
}

private val _pauseStateChange = SnekGameStateChange(BaseSnekDataViewChange(SnekDataChange(screen = ready)))
private val _unpauseStateChange = SnekGameStateChange(BaseSnekDataViewChange(SnekDataChange(screen = playing)))

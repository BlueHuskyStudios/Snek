package org.bh.game.snek.game.logic

import org.bh.game.snek.gui.swing.SnekAction
import org.bh.game.snek.gui.swing.SnekAction.*
import org.bh.game.snek.state.SnekDataViewController
import org.bh.game.snek.state.SnekGameStateChange
import org.bh.game.snek.state.SnekScreen.*
import org.bh.game.snek.state.SnekStateStorage
import org.bh.tools.base.abstraction.Integer
import org.bh.tools.base.collections.firstOrNull
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
    var store = SnekStateStorage(initialState)

    override fun currentState(): SnekDataViewController {
        return store.currentState()
    }

    override fun mutate(action: SnekAction) {
        store.pushState(mutator.mutating(currentState(), action))
    }

    /**
     * Given the list of possible actions (either concurrent or vague), returns the appropriate one for the current
     * state
     */
    fun appropriateAction(actions: List<SnekAction>): SnekAction? {
        return when (currentState().dataView.screen) {
            playing -> actions.filter { it != unpause }.firstOrNull
            ready, settings, scores -> actions.firstOrNull
        }

    }
}


/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 *
 *
 * @author Kyli Rouge
 * @since 2016-11-09
 */
class SnekGameStateMutator : StateMutator<SnekDataViewController, SnekAction, SnekGameStateChange> {
    override fun mutating(state: SnekDataViewController, action: SnekAction): SnekGameStateChange {
        return when (action) {
            pause -> _pauseStateChange
            unpause -> _unpauseStateChange
            start -> TODO()
            moveUp -> movingSnek(state, dx = 0, dy = -1)
            moveDown -> movingSnek(state, dx = 0, dy = 1)
            moveRight -> movingSnek(state, dx = 1, dy = 0)
            moveLeft -> movingSnek(state, dx = -1, dy = 0)
        }
    }

    private fun movingSnek(oldState: SnekDataViewController, dx: Integer, dy: Integer): SnekGameStateChange {
        val headPosition = oldState.snek.headPosition
        val nextPosition = headPosition + Pair(dx, dy)
        val newPath = oldState.snek.path + nextPosition
        if (newPath.intersectsSelf) {
            return _loseStateChange
        }
        return SnekGameStateChange(snekPath = newPath)
    }
}

private val _loseStateChange = SnekGameStateChange(screen = ready)
private val _pauseStateChange = SnekGameStateChange(screen = ready)
private val _unpauseStateChange = SnekGameStateChange(screen = playing)

package org.bh.game.snek.game.logic

import org.bh.game.snek.gui.swing.SnekAction
import org.bh.game.snek.gui.swing.SnekAction.*
import org.bh.game.snek.io.SnekDataGenerator
import org.bh.game.snek.state.*
import org.bh.game.snek.state.SnekScreen.*
import org.bh.tools.base.abstraction.Integer
import org.bh.tools.base.collections.DeltaStack
import org.bh.tools.base.collections.extensions.count
import org.bh.tools.base.collections.extensions.firstOrNull
import org.bh.tools.base.func.observing
import org.bh.tools.base.math.geometry.IntegerPath
import org.bh.tools.base.math.geometry.IntegerPoint
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
class SnekGameStateController(initialState: SnekDataViewController)
    : StateController<SnekDataViewController, SnekAction>,
        SnekStateStorageDelegate {

    /** The largest size the stack can be before it is flattened */
    val stackSizeLimit by observing(16,
            didSet = { _, new ->
                if (store.size > new) {
                    store.flattenState()
                }
            })
    val mutator = SnekGameStateMutator()
    var store = SnekStateStorage(initialState, this)


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
            playing -> actions.firstOrNull { it != unpause }
            ready, settings, scores -> actions.firstOrNull
        }
    }



    // MARK: SnekStateStorageDelegate

    override fun deltaStackShouldFlattenState(stack: DeltaStack<SnekDataViewController, SnekGameStateChange>): Boolean {
        return stack.size > stackSizeLimit
    }
}


/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * That which mutates Snek's game state
 *
 * @author Kyli Rouge
 * @since 2016-11-09
 */
class SnekGameStateMutator : StateMutator<SnekDataViewController, SnekAction, SnekGameStateChange> {
    override fun mutating(state: SnekDataViewController, action: SnekAction): SnekGameStateChange {
        return when (action) {
            is pause -> _pauseStateChange
            is unpause,
            is start -> _continuePlayingStateChange
            is moveUp -> movingSnek(state, dx = 0, dy = -1)
            is moveDown -> movingSnek(state, dx = 0, dy = 1)
            is moveRight -> movingSnek(state, dx = 1, dy = 0)
            is moveLeft -> movingSnek(state, dx = -1, dy = 0)

            is setDebugMode -> settingDebugMode(action.newMode)
        }
    }
}


private fun movingSnek(oldState: SnekDataViewController, dx: Integer, dy: Integer): SnekGameStateChange {
    when (oldState.snek.screen) {
        SnekScreen.ready,
        SnekScreen.settings,
        SnekScreen.scores -> return _noChange
        SnekScreen.playing -> {
            val headPosition = oldState.snek.headPosition
            val nextHeadPosition = headPosition + Pair(dx, dy)
            val oldPoints = oldState.snek.path.points
            val newApplePosition = checkAppleHit(oldState, nextHeadPosition)
            val newBaseList = if (newApplePosition != null) oldPoints else oldPoints.subList(1, oldPoints.count)
            val newPath = IntegerPath(points = newBaseList + nextHeadPosition, isClosed = false)
            if (newPath.intersectsSelf) {
                return _loseStateChange
            }
            return SnekGameStateChange(snekPath = newPath, applePosition = newApplePosition)
        }
    }
}

private fun checkAppleHit(oldState: SnekDataViewController, nextHeadPosition: IntegerPoint): IntegerPoint? =
        when (nextHeadPosition) {
            oldState.snek.applePosition -> SnekDataGenerator.generateApplePosition(
                    boardSize = oldState.snek.boardSize,
                    snekPath = oldState.snek.path
            )
            else -> null
        }


private fun changingScreen(newScreen: SnekScreen): SnekGameStateChange = SnekGameStateChange(screen = newScreen)


private fun settingDebugMode(newMode: Boolean): SnekGameStateChange = SnekGameStateChange(debug = newMode)


private val _noChange by lazy { SnekGameStateChange() }
private val _loseStateChange by lazy { changingScreen(scores) }
private val _pauseStateChange by lazy { changingScreen(ready) }
private val _continuePlayingStateChange by lazy { changingScreen(playing) }

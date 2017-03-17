package org.bh.game.snek.game.logic

import org.bh.game.snek.gui.swing.SnekAction
import org.bh.game.snek.gui.swing.SnekAction.*
import org.bh.game.snek.io.SnekDataGenerator
import org.bh.game.snek.state.*
import org.bh.game.snek.state.SnekScreen.*
import org.bh.tools.base.abstraction.Integer
import org.bh.tools.base.async.Timer
import org.bh.tools.base.collections.DeltaStack
import org.bh.tools.base.collections.extensions.*
import org.bh.tools.base.func.*
import org.bh.tools.base.math.geometry.IntegerPath
import org.bh.tools.base.math.geometry.IntegerPoint
import org.bh.tools.base.math.geometry.LineSegmentDirection.*
import org.bh.tools.base.state.*

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
                if (_store.size > new) {
                    _store.flattenState()
                }
            })
    private val _mutator = SnekGameStateMutator()
    private val _store = SnekStateStorage(initialState, this)
    private val _timer = Timer(initialState.snek.delayBetweenMovements, { timerDidTick() })
    private val _mutationListeners = mutableListOf<StateMutationListener<SnekDataViewController>>()
    private var _nextAction: SnekAction? = null


    init {
        _timer.start()
    }


    override fun currentState(): SnekDataViewController {
        return _store.currentState()
    }


    override fun mutate(action: SnekAction) {
        val oldState = currentState()
        _store.pushState(_mutator.mutating(currentState(), action))
        _mutationListeners.forEach { it.stateDidMutate(oldState, currentState()) }
    }


    private fun timerDidTick() {
        when (currentState().snek.screen) {
            playing -> {
                if (!_consumeNextAction()) {
                    _moveForwardUnconditionally()
                }
            }
            ready, settings, scores -> return
        }
    }


    private fun _consumeNextAction(): Boolean {
        synchronized(this) {
            val nextAction = _nextAction
            return if (nextAction != null) {
                _translateActionToMovement(nextAction)?.let {
                    if (_nextHeadPositionIfValidMovement(currentState(), it.first, it.second) == null) {
                        return false
                    }
                }
                mutate(nextAction)
                _nextAction = null

                /* return */ true
            } else {
                /* return */ false
            }
        }
    }


    private fun _moveForwardUnconditionally() {
        val currentPath = currentState().snek.path
        val neckSegment = currentPath.segments.last
        val action = when (neckSegment.direction) {
            is yDecreasesMost -> moveUp
            is yIncreasesMost -> moveDown
            is xDecreasesMost -> moveLeft
            is xIncreasesMost -> moveRight
        }
        mutate(action)
    }


    /**
     * Queues the given action to be performed at the next timer tick, or replaces the previously-queued action
     *
     * @param action The next action to perform
     */
    fun setQueuedAction(action: SnekAction) {
        when (currentState().snek.screen) {
            playing -> _nextAction = action
            ready, settings, scores -> mutate(action)
        }
    }


    /**
     * Given the list of possible actions (either concurrent or vague), returns the appropriate one for the current
     * state
     */
    fun appropriateAction(actions: List<SnekAction>): SnekAction? {
        return when (currentState().snek.screen) {
            playing -> actions.firstOrNull { it != start }
            ready, settings, scores -> actions.firstOrNull
        }
    }


    fun reset() {
        _store.reset(SnekDataViewController(SnekDataGenerator.generateDefaultData()))
    }



    // MARK: SnekStateStorageDelegate

    override fun deltaStackShouldFlattenState(stack: DeltaStack<SnekDataViewController, SnekGameStateChange>): Boolean {
        return stack.size > stackSizeLimit
    }


    override fun addStateMutationListener(stateMutationListener: StateMutationListener<SnekDataViewController>) {
        _mutationListeners.add(stateMutationListener)
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
            is moveUp,
            is moveDown,
            is moveRight,
            is moveLeft -> _translateActionToMovement(action)?.let { movingSnek(state, dx = it.first, dy = it.second) } ?: _noChange

            is setDebugMode -> settingDebugMode(action.newMode)
        }
    }
}


/**
 * Turns the given action into a movement of Snek (as the pair `(dx, dy)`), or `null` if it can't be done
 */
private fun _translateActionToMovement(action: SnekAction): Tuple2<Integer, Integer>? {
    return when (action) {
        is pause,
        is unpause,
        is start,
        is setDebugMode -> null
        is moveUp -> tuple(0, -1)
        is moveDown -> tuple(0, 1)
        is moveRight -> tuple(1, 0)
        is moveLeft -> tuple(-1, 0)
    }
}


private fun movingSnek(oldState: SnekDataViewController, dx: Integer, dy: Integer): SnekGameStateChange {
    when (oldState.snek.screen) {
        SnekScreen.ready,
        SnekScreen.settings,
        SnekScreen.scores -> return _noChange
        SnekScreen.playing -> {
            val nextHeadPosition = _nextHeadPositionIfValidMovement(oldState, dx = dx, dy = dy) ?: return _noChange

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


private fun _nextHeadPositionIfValidMovement(currentState: SnekDataViewController, dx: Integer, dy: Integer): IntegerPoint? {
    val oldHeadPosition = currentState.snek.headPosition
    val oldPathPoints = currentState.snek.path.points
    val pointBehindHead = oldPathPoints[oldPathPoints.length - 2]
    val nextHeadPosition = oldHeadPosition + Pair(dx, dy)

    if (nextHeadPosition.equals(pointBehindHead)) { // No moving backwards
        return null
    } else {
        return nextHeadPosition
    }
}


private fun checkAppleHit(oldState: SnekDataViewController, nextHeadPosition: IntegerPoint): IntegerPoint? =
        when (nextHeadPosition) {
            oldState.snek.applePosition -> SnekDataGenerator.generateApplePosition(
                    boardSize = oldState.snek.boardSize,
                    snekPath = oldState.snek.path + nextHeadPosition
            )
            else -> null
        }


private fun changingScreen(newScreen: SnekScreen): SnekGameStateChange = SnekGameStateChange(screen = newScreen)


private fun settingDebugMode(newMode: Boolean): SnekGameStateChange = SnekGameStateChange(debug = newMode)


private val _noChange by lazy { SnekGameStateChange() }
private val _loseStateChange by lazy { changingScreen(scores) }
private val _pauseStateChange by lazy { changingScreen(ready) }
private val _continuePlayingStateChange by lazy { changingScreen(playing) }

package org.bh.game.snek.gui.swing

import org.bh.game.snek.game.logic.SnekGameStateController
import org.bh.game.snek.state.*
import org.bh.tools.base.state.StateMutationListener
import org.bh.tools.base.struct.UIViewController
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-05
 */
class SnekViewController(override val view: SnekView, val controller: SnekGameStateController):
//        KeyListener,
        UIViewController<SnekView>,
        StateMutationListener<SnekDataViewController> {

    constructor(snekDataView: BaseSnekDataView, mutator: SnekGameStateController): this(SnekView(snekDataView), mutator)
    constructor(snekData: SnekData, mutator: SnekGameStateController): this(BaseSnekDataView(snekData), mutator)

    init {
//        view.addKeyListener(this)
        view.isFocusCycleRoot = true
        view.grabFocus()

        controller.addStateMutationListener(this)

        controller.currentState().snek.keymap.registerAll(this) {
            object: AbstractAction() {
                override fun actionPerformed(e: ActionEvent?) {
                    performAction(it)
                }
            }
        }
    }

//    override fun keyTyped(e: KeyEvent?) {
//        if (e != null) performActionForKeyEvent(e, onKeyTyped)
//    }
//
//    override fun keyPressed(e: KeyEvent?) {
//        if (e != null) performActionForKeyEvent(e, onKeyDown)
//    }
//
//    override fun keyReleased(e: KeyEvent?) {
//        if (e != null) performActionForKeyEvent(e, onKeyUp)
//    }
//
//    private fun performActionForKeyEvent(e: KeyEvent, trigger: KeyActionTrigger) {
////        val action = keymap.map.entries.safeFirst { 0 != (it.value and e.extendedKeyCode) }?.key
//        val actions = keymap.actionsForKeyEvent(e, trigger)
//        val appropriateAction = controller.appropriateAction(actions)
//        if (appropriateAction != null) performAction(appropriateAction)
//    }


    private fun performAction(action: SnekAction) {
        controller.setQueuedAction(action)
    }


    override fun stateDidMutate(oldState: SnekDataViewController, newState: SnekDataViewController) {
        view.representedObject = newState.snek
    }
}
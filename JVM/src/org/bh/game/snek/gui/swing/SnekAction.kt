package org.bh.game.snek.gui.swing

import org.bh.game.snek.gui.swing.KeyActionTrigger.onKeyDown
import org.bh.game.snek.gui.swing.KeyActionTrigger.onKeyUp

enum class SnekAction(val trigger: KeyActionTrigger) {
    pause(onKeyDown),
    unpause(onKeyUp),
    start(onKeyDown),
    moveUp(onKeyDown),
    moveDown(onKeyDown),
    moveRight(onKeyDown),
    moveLeft(onKeyDown);

    companion object {
        val allOnKeyUp: List<SnekAction> by lazy { values().filter { it.trigger == onKeyUp } }
        val allOnKeyDown: List<SnekAction> by lazy { values().filter { it.trigger == onKeyDown } }
    }
}
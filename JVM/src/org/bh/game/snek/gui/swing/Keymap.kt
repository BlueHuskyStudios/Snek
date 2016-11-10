package org.bh.game.snek.gui.swing

import org.bh.game.snek.gui.swing.KeyActionTrigger.onKeyDown
import org.bh.game.snek.gui.swing.KeyActionTrigger.onKeyUp
import org.bh.game.snek.gui.swing.SnekAction.*
import java.awt.event.KeyEvent.*

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-08
 */
data class Keymap(val map: Map<SnekAction, Int>) {
    companion object {
        val default = Keymap(linkedMapOf(
                Pair(pause, VK_ESCAPE),
                Pair(unpause, VK_ESCAPE),
                Pair(start, VK_ENTER),
                Pair(moveUp, VK_UP),
                Pair(moveDown, VK_DOWN),
                Pair(moveRight, VK_RIGHT),
                Pair(moveLeft, VK_LEFT)
        ))
    }
}

enum class SnekAction(val trigger: KeyActionTrigger) {
    pause(onKeyDown),
    unpause(onKeyUp),
    start(onKeyDown),
    moveUp(onKeyDown),
    moveDown(onKeyDown),
    moveRight(onKeyDown),
    moveLeft(onKeyDown);

    companion object {
        val allOnKeyUp: List<SnekAction> by lazy { SnekAction.values().filter { it.trigger == onKeyUp } }
        val allOnKeyDown: List<SnekAction> by lazy { SnekAction.values().filter { it.trigger == onKeyDown } }
    }
}

enum class KeyActionTrigger {
    onKeyUp,
    onKeyDown,
    onKeyTyped;
}

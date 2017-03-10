package org.bh.game.snek.gui.swing

import org.bh.game.snek.gui.swing.KeyActionTrigger.onKeyDown
import org.bh.game.snek.gui.swing.KeyActionTrigger.onKeyUp

sealed class SnekAction(val trigger: KeyActionTrigger?) {
    object pause: SnekAction(onKeyUp)
    object unpause: SnekAction(onKeyUp)
    object start: SnekAction(onKeyUp)
    object moveUp: SnekAction(onKeyDown)
    object moveDown: SnekAction(onKeyDown)
    object moveRight: SnekAction(onKeyDown)
    object moveLeft: SnekAction(onKeyDown)

    class setDebugMode(val newMode: Boolean): SnekAction(null)

//    companion object {
//        val allOnKeyUp: List<SnekAction> by lazy { values().filter { it.trigger == onKeyUp } }
//        val allOnKeyDown: List<SnekAction> by lazy { values().filter { it.trigger == onKeyDown } }
//    }

    override fun toString(): String {
        return "${super.toString()}@$trigger"
    }
}
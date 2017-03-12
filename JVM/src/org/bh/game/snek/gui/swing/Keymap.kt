package org.bh.game.snek.gui.swing

import org.bh.game.snek.gui.swing.SnekAction.*
import org.bh.tools.base.func.transform
import org.bh.tools.base.struct.UIView
import org.bh.tools.base.struct.UIViewController
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import javax.swing.*

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-08
 */
data class Keymap(val map: Map<SnekAction, Int>) {
    constructor(): this(linkedMapOf(
                pause to VK_ESCAPE,
                unpause to VK_ESCAPE,
                start to VK_ENTER,
                moveUp to VK_UP,
                moveDown to VK_DOWN,
                moveRight to VK_RIGHT,
                moveLeft to VK_LEFT
        ))

    /**
     * Registers all the keys in this map to the given view controller, where each one corresponds to an action
     * returned in the given block
     */
    fun <ViewType> registerAll(viewController: UIViewController<ViewType>, actionCallback: KeymapActionCallback)
            where ViewType : UIView<*>, ViewType : JComponent
            = map.forEach { snekAction: SnekAction, keyCode: Int ->
                viewController.view.inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), snekAction)
                viewController.view.actionMap.put(snekAction, actionCallback(snekAction))
            }


    fun actionsForKeyEvent(e: KeyEvent, trigger: KeyActionTrigger): List<SnekAction> {
        val keyCode = e.extendedKeyCode
        val actions =
                map.entries.filter {
                    it.key.trigger == trigger
                        && it.value == keyCode
                }.map(Map.Entry<SnekAction, Int>::key)
        return actions
    }


    fun keyCodesForAction(action: SnekAction): List<Int> {
        return map[action]?.transform { listOf(it) } ?: listOf()
    }
}

typealias KeymapActionCallback = (action: SnekAction) -> Action?

enum class KeyActionTrigger {
    onKeyUp,
    onKeyDown,
    onKeyTyped;
}

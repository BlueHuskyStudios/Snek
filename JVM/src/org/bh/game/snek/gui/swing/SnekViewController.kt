package org.bh.game.snek.gui.swing

import org.bh.game.snek.game.logic.SnekGameStateController
import org.bh.game.snek.state.BaseSnekDataView
import org.bh.game.snek.state.SnekData
import org.bh.tools.base.collections.safeFirst
import org.bh.tools.base.struct.UIViewController
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-05
 */
class SnekViewController(override val view: SnekView, val controller: SnekGameStateController, val keymap: Keymap):
        KeyListener,
        UIViewController<SnekView> {
    constructor(snekDataView: BaseSnekDataView, mutator: SnekGameStateController, keymap: Keymap): this(SnekView(snekDataView), mutator, keymap)
    constructor(snekData: SnekData, mutator: SnekGameStateController, keymap: Keymap): this(BaseSnekDataView(snekData), mutator, keymap)

    init {
        view.addKeyListener(this)
    }

    override fun keyTyped(e: KeyEvent?) {
        print("Key typed: ${e?.extendedKeyCode}")
    }

    override fun keyPressed(e: KeyEvent?) {
        print("Key pressed: ${e?.extendedKeyCode}")
    }

    override fun keyReleased(e: KeyEvent?) {
        print("Key released: ${e?.extendedKeyCode}")
        if (e == null) return
        val action = keymap.map.entries.safeFirst { 0 != (it.value and e.extendedKeyCode) }?.key
        if (action != null) {
            controller.mutate(action)
            view.dataView = controller.currentState().dataView
        }
    }
}
package org.bh.game.snek.game

import org.bh.game.snek.game.logic.SnekGameStateController
import org.bh.game.snek.gui.swing.Keymap
import org.bh.game.snek.gui.swing.SnekViewController
import org.bh.game.snek.gui.swing.SnekWindow
import org.bh.game.snek.state.*

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-02
 */
class SnekGame(args: Array<String>) {

    init {
        SnekArgsProcessor_shared.process(args)
    }

    fun start() {
        SnekDataAccessor().accessData(null) { data, status ->
            val initialState = SnekDataViewController(data ?: SnekData_new())
            val stateController = SnekGameStateController(initialState)
            val snekViewController = SnekViewController(
                    initialState.dataView,
                    stateController,
                    Keymap())
            SnekWindow(snekViewController).isVisible = true
        }
    }
}

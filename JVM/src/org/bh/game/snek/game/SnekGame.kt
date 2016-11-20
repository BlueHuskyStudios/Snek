package org.bh.game.snek.game

import org.bh.game.snek.game.logic.SnekGameStateController
import org.bh.game.snek.gui.swing.Keymap
import org.bh.game.snek.gui.swing.SnekViewController
import org.bh.game.snek.gui.swing.SnekWindow
import org.bh.game.snek.state.SnekArgsProcessor
import org.bh.game.snek.state.SnekDataAccessor
import org.bh.game.snek.state.SnekDataViewController

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-02
 */
class SnekGame(args: Array<String>) {

    init {
        SnekArgsProcessor.shared.process(args)
    }

    fun start() {
        SnekDataAccessor.shared.accessData(null) { data, status ->
            val initialState = SnekDataViewController(data ?: SnekDataAccessor.newData)
            val stateController = SnekGameStateController(initialState)
            val snekViewController = SnekViewController(
                    initialState.dataView,
                    stateController,
                    Keymap.default)
            SnekWindow(snekViewController).isVisible = true
        }
    }
}

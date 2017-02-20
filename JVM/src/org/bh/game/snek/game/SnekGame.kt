package org.bh.game.snek.game

import org.bh.game.snek.game.logic.SnekGameStateController
import org.bh.game.snek.gui.swing.*
import org.bh.game.snek.io.SnekDataAccessDetails
import org.bh.game.snek.io.SnekDataAccessor
import org.bh.game.snek.state.*
import org.bh.tools.base.math.int32Value
import org.bh.tools.ui.alert.NativeAlert

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-02
 */
class SnekGame(args: Array<String>) {
    lateinit var initialState: SnekDataViewController
    lateinit var stateController: SnekGameStateController
    lateinit var argsProcessor: SnekArgsProcessor

    var safeToStart = false
    var onDidAccessData: OnDidAccessData? = null


    init {
        synchronized(this) {
            SnekDataAccessor().accessData(SnekDataAccessDetails.generateNewData) { data, status ->
                if (data == null) {
                    NativeAlert.showOptionlessConfirmation("Failed to create or access a Snek",
                            status?.message ?: "And I don't know why") {
                        System.exit(status?.code?.int32Value ?: 1)
                    }
                } else {
                    initialState = SnekDataViewController(data)
                    stateController = SnekGameStateController(initialState)
                    argsProcessor = SnekArgsProcessor(SnekArgs(stateController))
                    argsProcessor.process(args)
                    safeToStart = true
                    onDidAccessData?.invoke()
                }
            }
        }
    }


    fun start() {
        startIfSafe()
    }


    private fun startIfSafe() = synchronized(this) {
        if (safeToStart) {
            startImmediately()
        } else {
            onDidAccessData = { startImmediately() }
        }
    }


    private fun startImmediately() {
        val snekViewController = SnekViewController(
                initialState.dataView,
                stateController,
                Keymap())
        SnekWindow(snekViewController).isVisible = true
    }
}



private typealias OnDidAccessData = () -> Unit

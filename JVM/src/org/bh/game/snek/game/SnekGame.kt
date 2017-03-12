package org.bh.game.snek.game

import org.bh.game.snek.game.logic.SnekGameStateController
import org.bh.game.snek.gui.swing.SnekViewController
import org.bh.game.snek.gui.swing.SnekWindow
import org.bh.game.snek.io.SnekDataAccessDetails
import org.bh.game.snek.io.SnekDataAccessor
import org.bh.game.snek.state.*
import org.bh.tools.base.math.int32Value
import org.bh.tools.io.logging.log
import org.bh.tools.wag.alert.NativeAlert

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

    private var safeToStart = false
    private var onDidAccessData: OnDidAccessData? = null


    init {
        log.fine("Accessing data...")
        synchronized(this) {
            SnekDataAccessor().accessData(SnekDataAccessDetails.generateNewData) { data, status ->
                log.fine("Accessed data")
                log.fine(status)
                if (data == null) {
                    log.fine("Data was null; could not start")
                    NativeAlert.showOptionlessConfirmation(
                            title = "Snek couldn't load",
                            detail = "Failed to create or access a Snek\r\n  \t" +
                                    (status?.message ?: "(And I don't know why)")) {
                        System.exit(status?.code?.int32Value ?: 1)
                    }
                    return@accessData
                } else {
                    log.fine("Successfully got data; initializing Snek")
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


    fun start(onDidStart: OnDidStart = {}) {
        startIfSafe(onDidStart)
    }


    private fun startIfSafe(onDidStart: OnDidStart) = synchronized(this) {
        if (safeToStart) {
            startImmediately(onDidStart)
        } else {
            onDidAccessData = { startImmediately(onDidStart) }
        }
    }


    private fun startImmediately(onDidStart: OnDidStart) {
        val snekViewController = SnekViewController(
                initialState.dataView,
                stateController)
        SnekWindow(snekViewController).isVisible = true
        onDidStart()
    }
}



private typealias OnDidAccessData = () -> Unit
private typealias OnDidStart = () -> Unit

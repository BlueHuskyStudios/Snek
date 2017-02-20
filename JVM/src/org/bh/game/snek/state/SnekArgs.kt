package org.bh.game.snek.state

import org.bh.game.snek.Constants
import org.bh.game.snek.game.logic.SnekGameStateController
import org.bh.game.snek.gui.swing.SnekAction
import org.bh.tools.io.setup.*

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-02
 */
class SnekArgs(val gameState: SnekGameStateController): CommandlineArgCollection() {
    val debug = CompleteCommandLineArg(
            singleCharacterArgument = 'd',
            fullTextArgument = "debug",
            description = "Enable debug mode",
            action = { gameState.mutate(SnekAction.setDebugMode(true)) }
    )

    val help = CommandLineArg.Defaults.HelpArg(
            executableName = Constants.executableName,
            allArgs = listOf(debug),
            stream = System.out
    )

    override val args: Array<CommandLineArg<*>> = arrayOf(debug, help)



//    companion object {
//        val shared = SnekArgs(SnekGameStateController.shared)
//    }
}



class SnekArgsProcessor(expectedArgs: SnekArgs): CommandLineArgProcessor(expectedArgs) {
//    companion object {
//        val shared = SnekArgsProcessor(SnekArgs.shared)
//    }
}

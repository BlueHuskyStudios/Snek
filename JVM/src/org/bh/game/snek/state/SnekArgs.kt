package org.bh.game.snek.state

import org.bh.tools.io.setup.*

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-02
 */
class SnekArgs(val metaGameState: SnekMetaGameState): CommandlineArgCollection() {
    val debug = CompleteCommandLineArg(
            singleCharacterArgument = 'd',
            fullTextArgument = "debug",
            description = "Enable debug mode",
            action = { metaGameState.debug = true }
    )

    override val args: Array<CommandLineArg<*>> = arrayOf(debug)
}

val SnekArgs_shared = SnekArgs(SnekMetaGameState.shared)
//val SnekArgs.Companion.shared: SnekArgs get() = _shared

class SnekArgsProcessor(expectedArgs: SnekArgs): CommandLineArgProcessor(expectedArgs) {
    companion object {
        val shared = SnekArgsProcessor(SnekArgs_shared)
    }
}

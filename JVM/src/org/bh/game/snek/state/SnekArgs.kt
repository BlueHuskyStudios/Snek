package org.bh.game.snek.state

import org.bh.game.snek.state.SnekArgs.Companion
import org.bh.tools.game.CommandLineArg
import org.bh.tools.game.CommandlineArgCollection
import org.bh.tools.game.CompleteCommandLineArg

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

    companion object
}

private val _shared = SnekArgs(SnekMetaGameState.shared)

val Companion.shared: SnekArgs get() = _shared

class SnekArgsProcessor {
    fun process(args: Array<String>) {
        SnekArgs.shared.parse(args).forEach { it.action(emptyArray()) }
    }
}

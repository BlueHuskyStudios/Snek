package org.bh.game.snek.state

import org.bh.tools.base.func.observing
import org.bh.tools.io.logging.Log

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-02
 */
class SnekMetaGameState {
    var debug: Boolean by observing(false,
            didSet = { old, new ->
                Log.i("\r\n\r\n\r\n\t\t\tDebug mode changed from $old to $new\r\n\r\n\r\n\r\n")
            })

    companion object {
        private val dummy = Unit
    }
}

private val _shared = SnekMetaGameState()

val SnekMetaGameState.Companion.shared: SnekMetaGameState get() = _shared

package org.bh.game.snek.state

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-02
 */
data class SnekMetaGameState(
        var debug: Boolean = false
) {
    companion object
}

private val _shared = SnekMetaGameState()

val SnekMetaGameState.Companion.shared: SnekMetaGameState get() = _shared

package org.bh.game.snek.game

import org.bh.game.snek.state.SnekArgsProcessor

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author ben_s
 * @since 002 2016-11-02
 */
class SnekGame {
    constructor(args: Array<String>) {
        SnekArgsProcessor.shared.process(args)
    }

    fun start() {

    }
}
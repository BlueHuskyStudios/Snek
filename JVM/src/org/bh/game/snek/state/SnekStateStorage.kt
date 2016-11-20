package org.bh.game.snek.state

import org.bh.tools.base.collections.DeltaStack
import org.bh.tools.base.state.StateStore

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * Stores the state of the Snek.
 *
 * sss~
 *
 * @author Kyli Rouge
 * @since 2016-10-31
 */
class SnekStateStorage(initialState: SnekDataViewController)
        : DeltaStack<SnekDataViewController, SnekGameStateChange>(initialState),
            StateStore<SnekDataViewController, SnekGameStateChange>
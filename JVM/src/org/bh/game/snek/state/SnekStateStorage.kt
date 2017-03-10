package org.bh.game.snek.state

import org.bh.tools.base.collections.DeltaStack
import org.bh.tools.base.collections.DeltaStackDelegate
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
class SnekStateStorage(initialState: SnekDataViewController, delegate: SnekStateStorageDelegate)
        : DeltaStack<SnekDataViewController, SnekGameStateChange>(initialState, delegate),
            StateStore<SnekDataViewController, SnekGameStateChange>

interface SnekStateStorageDelegate: DeltaStackDelegate<SnekDataViewController, SnekGameStateChange>

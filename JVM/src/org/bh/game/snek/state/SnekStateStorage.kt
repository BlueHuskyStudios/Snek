package org.bh.game.snek.state

import org.bh.tools.base.collections.HistoryArray
import org.bh.tools.base.collections.HistoryArraySelectionMode.SelectNewlyPushed

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
class SnekStateStorage: HistoryArray<SnekGameState>(selectionMode = SelectNewlyPushed) {

}
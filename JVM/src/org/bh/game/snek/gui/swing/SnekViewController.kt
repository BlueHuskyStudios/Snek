package org.bh.game.snek.gui.swing

import org.bh.game.snek.state.BaseSnekDataView
import org.bh.game.snek.state.SnekData

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-05
 */
class SnekViewController(val view: SnekView) {
    constructor(snekDataView: BaseSnekDataView): this(SnekView(snekDataView))
    constructor(snekData: SnekData): this(BaseSnekDataView(snekData))
}
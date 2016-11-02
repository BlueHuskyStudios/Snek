package org.bh.game.snek.state

import org.bh.tools.base.collections.deepEquals
import org.bh.tools.base.struct.coord.IntPoint
import org.bh.tools.base.struct.coord.IntSize
import java.util.*

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * The state of Snek
 *
 * @author Kyli Rouge
 * @since 2016-10-30
 */
class SnekGameState(initialData: SnekData) {
    val snek = BaseSnekDataView(initialData)
}

data class BaseSnekDataView(private val initialData: SnekData) {
    val boardSize: IntSize get() = initialData.boardSize
    val snek: Array<IntPoint> get() = initialData.snek
}

data class SnekData(val boardSize: IntSize, val snek: Array<IntPoint>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SnekData) return false
        if (other.boardSize != this.boardSize) return false
        if (!other.snek.deepEquals(this.snek)) return false
        return true
    }

    override fun hashCode(): Int{
        var result = boardSize.hashCode()
        result = 31 * result + Arrays.hashCode(snek)
        return result
    }
}


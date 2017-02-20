package org.bh.game.snek.io

import org.bh.game.snek.state.SnekData
import org.bh.tools.base.abstraction.Integer
import org.bh.tools.base.struct.DataAccessor

class SnekDataAccessor : DataAccessor<SnekData, SnekDataAccessDetails?, SnekDataAccessStatus?> {

    override fun accessData(details: SnekDataAccessDetails?,
                            didAccessData: (accessedData: SnekData?, status: SnekDataAccessStatus?) -> Unit) {
        val unused = when (details) {
            SnekDataAccessDetails.generateNewData -> didAccessData(SnekDataGenerator().generateDefaultData(), null)
            null -> throw UnsupportedOperationException("Details must be provided")
            // TODO: serialize/deserialize
        }
    }
}



sealed class SnekDataAccessDetails {
    object generateNewData: SnekDataAccessDetails()

    // TODO: More
}



data class SnekDataAccessStatus(
        val message: String,
        val code: Integer
)

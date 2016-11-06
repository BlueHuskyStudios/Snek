package org.bh.game.snek.state

import java.util.*

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * All them scores
 *
 * @author Kyli Rouge
 * @since 2016-11-02
 */
data class Leaderboard<KeyType, out ScoreType>(val scores: Map<KeyType, ScoreType>)


/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * One who leads
 *
 * @author Kyli Rouge
 * @since 2016-11-02
 */
data class Leader(val name: String, val uuid: UUID = UUID.randomUUID())

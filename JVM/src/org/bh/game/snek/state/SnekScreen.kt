package org.bh.game.snek.state

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * Represents the screens of a Snek game
 *
 * @author Kyli Rouge
 * @since 2016-11-20
 */
enum class SnekScreen {
    /**
     * Ready to play; may represent pause screen
     */
    ready,
    /**
     * Currently playing
     */
    playing,
    /**
     * The settings screen
     */
    settings,
    /**
     * Viewing the leaderboard
     */
    scores
}

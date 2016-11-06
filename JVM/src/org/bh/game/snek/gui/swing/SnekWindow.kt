package org.bh.game.snek.gui.swing

import java.awt.BorderLayout
import javax.swing.JApplet
import javax.swing.JFrame

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-05
 */
class SnekWindow(snekViewController: SnekViewController): JFrame() {
    init {
        contentPane = SnekApplet(snekViewController)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
    }
}

class SnekApplet(snekViewController: SnekViewController) : JApplet() {
    init {
        initGUI(snekViewController)
    }

    private fun initGUI(snekViewController: SnekViewController) {
        layout = BorderLayout()
        add(snekViewController.view, BorderLayout.CENTER)
    }
}


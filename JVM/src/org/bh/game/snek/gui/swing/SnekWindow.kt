package org.bh.game.snek.gui.swing

import java.awt.BorderLayout
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.JApplet
import javax.swing.JFrame

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * @author Kyli Rouge
 * @since 2016-11-05
 */
class SnekWindow(snekViewController: SnekViewController): JFrame(), WindowListener {
    init {
        addWindowListener(this)
        title = "Snek"
        if (snekViewController.controller.store.currentState().snek.debug) {
            title += " - DEBUG"
        }
        contentPane = SnekApplet(snekViewController)
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    override fun pack() {
        super.pack()
        setLocationRelativeTo(null)
    }

    // MARK: - WindowListener

    var hasShown = false

    override fun windowIconified(e: WindowEvent?) { }

    override fun windowDeiconified(e: WindowEvent?) { }

    override fun windowClosing(e: WindowEvent?) { }

    override fun windowClosed(e: WindowEvent?) {
        System.exit(0)
    }

    override fun windowActivated(e: WindowEvent?) {
        if (!hasShown) {
            pack()
        }
    }

    override fun windowDeactivated(e: WindowEvent?) { }

    override fun windowOpened(e: WindowEvent?) {
        hasShown = true
        pack()
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


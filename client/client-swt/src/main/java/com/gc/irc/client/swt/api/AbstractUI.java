/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gc.irc.client.swt.api;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.gc.irc.common.AbstractLoggable;

/**
 * The Class AbstractUI.
 *
 * @version 0.0.4
 * @author x472511
 */
public abstract class AbstractUI extends AbstractLoggable {

    /** The shell. */
    private Shell shell;

    /**
     * Instantiates a new abstract ui.
     *
     * @param display
     *            the display
     */
    public AbstractUI(final Display display) {
        manageDisplay(display);
    }

    /**
     * Gets the shell.
     *
     * @return the shell
     */
    protected Shell getShell() {
        return shell;
    }

    /**
     * Inits the shell.
     *
     * @param shell
     *            the shell
     */
    protected abstract void initShell(Shell shell);

    /**
     * Manage display.
     *
     * @param display
     *            the display
     */
    private void manageDisplay(final Display display) {
        shell = new Shell(display);

        initShell(shell);

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}

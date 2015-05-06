/* Copyright (C) 2015 charva-lanterna
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package charva.demo;

import java.io.IOException;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charva.showcase.Tutorial;
import charva.toolkit.lanterna.LanternaToolkit;
import charvax.swing.SwingUtilities;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

/**
 * Defines entry point for the charva-lanterna demo application.
 */
public class CharvaDemoApp {

    public static void main(String[] args) throws IOException {
        final DefaultTerminalFactory factory = new DefaultTerminalFactory();
        //factory.setSuppressSwingTerminalFrame(true);

        final LanternaToolkit toolkit = new LanternaToolkit(
                factory.createTerminal());

        toolkit.startEventThread();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final Tutorial tutorial = new Tutorial();
                tutorial.setExitActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent ae) {
                        toolkit.stopEventThread();
                    }
                });
                tutorial.show();
            }
        });
    }
}

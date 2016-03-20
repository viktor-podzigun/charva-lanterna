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

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;
import javax.swing.WindowConstants;
import charva.awt.ColorScheme;
import charva.awt.Toolkit;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charva.showcase.Tutorial;
import charva.toolkit.lanterna.LanternaToolkit;
import charvax.swing.SwingUtilities;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.ansi.CygwinTerminal;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorColorConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorDeviceConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

/**
 * Defines entry point for the charva-lanterna demo application.
 */
public final class CharvaDemoApp {

    private CharvaDemoApp() {
    }

    public static void main(String[] args) throws IOException {
        new CharvaDemoApp().run();
    }

    private void run() throws IOException {
        //final DefaultTerminalFactory factory = new DefaultTerminalFactory();
        //factory.setSuppressSwingTerminalFrame(true);

        final LanternaToolkit toolkit = new LanternaToolkit(createTerminal());
        toolkit.startEventThread();

        setColors();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
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

    private void setColors() throws IOException {
        final ColorScheme normalColors = loadColors("/blue-colors.properties");
        if (normalColors != null) {
            Toolkit.setNormalColors(normalColors);
        }
    }

    private ColorScheme loadColors(final String colorsFileName) throws IOException {
        InputStream colorsStream = null;
        try {
            colorsStream = getClass().getResourceAsStream(colorsFileName);
            if (colorsStream == null) {
                return null;
            }

            final Properties properties = new Properties();
            properties.load(colorsStream);
            return new ColorScheme(properties);

        } finally {
            if (colorsStream != null) {
                colorsStream.close();
            }
        }
    }

    private Terminal createTerminal() throws IOException {
        final OutputStream outputStream = System.out;
        final InputStream inputStream = System.in;
        final Charset charset = Charset.forName(
                System.getProperty("file.encoding"));

        if (GraphicsEnvironment.isHeadless()) {
            if (isOperatingSystemWindows()) {
                return new WinTerminal(inputStream, outputStream, charset);
            }

            return new UnixTerminal(inputStream, outputStream, charset);
        }

        SwingTerminalFrame swingTerminalFrame = new SwingTerminalFrame(
                "charva-lanterna Demo",
                TerminalEmulatorDeviceConfiguration.getDefault(),
                SwingTerminalFontConfiguration.getDefault(),
                TerminalEmulatorColorConfiguration.getDefault(),
                TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode);

        swingTerminalFrame.setDefaultCloseOperation(
                WindowConstants.EXIT_ON_CLOSE);
        swingTerminalFrame.setVisible(true);
        return swingTerminalFrame;
    }

    /**
     * Detects whether the running platform is Windows* by looking at the
     * operating system name system property
     */
    private static boolean isOperatingSystemWindows() {
        return System.getProperty("os.name", "").toLowerCase()
                .startsWith("windows");
    }

    private static class WinTerminal extends CygwinTerminal {

        public WinTerminal(InputStream terminalInput,
                OutputStream terminalOutput,
                Charset terminalCharset) throws IOException {

            super(terminalInput, terminalOutput, terminalCharset);
        }

        private static String findProgram(String programName) {
            String[] paths = System.getProperty("java.library.path").split(";");
            for (String path : paths) {
                final File shBin = new File(path, programName);
                if (shBin.exists()) {
                    return shBin.getAbsolutePath();
                }
            }

            return programName;
        }
    }
}

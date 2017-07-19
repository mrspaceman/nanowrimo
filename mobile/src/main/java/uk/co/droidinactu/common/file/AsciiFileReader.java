/*<p>
 * Copyright 2012 Andy Aspell-Clark
 *</p><p>
 * This file is part of eBookLauncher.
 * </p><p>
 * eBookLauncher is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * </p><p>
 * eBookLauncher is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 * </p><p>
 * You should have received a copy of the GNU General Public License along
 * with eBookLauncher. If not, see http://www.gnu.org/licenses/.
 *</p>
 */
package uk.co.droidinactu.common.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AsciiFileReader extends AsciiFile {

    protected BufferedReader buffrdRdr;
    protected FileInputStream fIn;
    protected InputStreamReader isr;

    /**
     * reads the first line of the file.
     *
     * @return a string containing the first line of the file
     */
    public String firstLineFromFile() {
        String sFileLine = null;
        try {
            this.closeFile();
            this.openFile();
            sFileLine = this.buffrdRdr.readLine();
            if (sFileLine != null) {
                this.m_lPosition += sFileLine.length();
                sFileLine.trim();
            } else {
                sFileLine = "";
            }
        } catch (final IOException ex) {
            // Logger.getLogger(AsciiFileReader.class.getName()).log(Level.SEVERE,
            // null,
            // ex);
        }
        return sFileLine;
    }

    /**
     * close the ascii file so that it can no longer be read.
     */
    public void closeFile() {
        try {
            this.buffrdRdr.close();
            this.isr.close();
        } catch (final Exception ex) {
            // Logger.getLogger(AsciiFileReader.class.getName()).log(Level.SEVERE,
            // null, ex);
        }
    }

    /**
     * opens the file ready for reading.
     */
    public void openFile() {
        try {
            final File gpxfile = new File(this.m_sFilepath + this.m_sFilename);
            this.isr = new FileReader(gpxfile);
            this.buffrdRdr = new BufferedReader(this.isr, 2048);
        } catch (final FileNotFoundException ex) {
            // Logger.getLogger(AsciiFileReader.class.getName()).log(Level.SEVERE,
            // null,
            // ex);
        }
    }

    public void mark() {
        try {
            this.buffrdRdr.mark(100);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads in the next line from the file.
     */
    public String read() {
        return this.nextLineFromFile().trim();
    }

    /**
     * reads the first line of the file.
     *
     * @return a string containing a line of the file
     */
    public String nextLineFromFile() {
        String sFileLine = null;
        try {
            sFileLine = this.buffrdRdr.readLine();
            if (sFileLine != null) {
                this.m_lPosition += sFileLine.length();
                sFileLine.trim();
            } else {
                sFileLine = "";
            }
        } catch (final IOException ex) {
            // Logger.getLogger(AsciiFileReader.class.getName()).log(Level.SEVERE,
            // null,
            // ex);
        }
        return sFileLine.trim();
    }

    public void reset() {
        try {
            this.buffrdRdr.reset();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "File: " + this.m_sFilepath + this.m_sFilename;
    }
} // class()

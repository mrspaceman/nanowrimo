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

public class AsciiFile {

    protected BufferedReader br;

    protected int m_lPosition;
    /**
     * the name of the file to read
     */
    protected String m_sFilename = "";
    protected String m_sFilepath = File.separatorChar + "sdcard" + java.io.File.separatorChar;

    public final int getCurrentPosition() {
        return this.m_lPosition;
    }

    public final void setCurrentPosition(final int value) {
        this.m_lPosition = value;
    }

    public String getFilename() {
        return this.m_sFilename;
    }

    public void setFilename(final String aFilename) {
        int indexOf = aFilename.lastIndexOf(java.io.File.separatorChar);
        if (indexOf == -1) {
            indexOf = aFilename.lastIndexOf(java.io.File.separatorChar);
        }
        this.m_sFilename = aFilename.substring(indexOf + 1);
        this.m_sFilepath = aFilename.substring(0, indexOf + 1);
    }

}

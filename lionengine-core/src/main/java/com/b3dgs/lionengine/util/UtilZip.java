/*
 * Copyright (C) 2013-2016 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Verbose;

/**
 * Tools related to ZIP handling.
 * <p>
 * This class is Thread-Safe.
 * </p>
 */
public final class UtilZip
{
    /** Error opening ZIP. */
    private static final String ERROR_OPEN_ZIP = "Unable to open ZIP : ";

    /**
     * Get all entries existing in the path considering the extension.
     * 
     * @param jar The JAR to check.
     * @param path The path to check.
     * @param extension The extension (without dot; eg: xml).
     * @return The entries list.
     * @throws LionEngineException If unable to open ZIP.
     */
    public static Collection<ZipEntry> getEntriesByExtension(File jar, String path, String extension)
    {
        try
        {
            ZipFile zip = null;
            try
            {
                zip = new ZipFile(jar);
                return checkEntries(zip, path, extension);
            }
            finally
            {
                closeZip(zip);
            }
        }
        catch (final IOException exception)
        {
            throw new LionEngineException(exception, ERROR_OPEN_ZIP, jar.getAbsolutePath());
        }
    }

    /**
     * Check all entries existing in the ZIP considering the extension.
     * 
     * @param zip The ZIP to check.
     * @param path The path to check.
     * @param extension The extension (without dot; eg: xml).
     * @return The entries list.
     */
    private static Collection<ZipEntry> checkEntries(ZipFile zip, String path, String extension)
    {
        final Collection<ZipEntry> entries = new ArrayList<ZipEntry>();
        final Enumeration<? extends ZipEntry> zipEntries = zip.entries();
        while (zipEntries.hasMoreElements())
        {
            final ZipEntry entry = zipEntries.nextElement();
            final String name = entry.getName();
            if (name.startsWith(path) && UtilFile.getExtension(name).equals(extension))
            {
                entries.add(entry);
            }
        }
        return entries;
    }

    /**
     * Close ZIP file.
     * 
     * @param zip The ZIP to close.
     */
    private static void closeZip(ZipFile zip)
    {
        try
        {
            if (zip != null)
            {
                zip.close();
            }
        }
        catch (final IOException exception)
        {
            Verbose.exception(exception);
        }
    }

    /**
     * Private constructor.
     */
    private UtilZip()
    {
        throw new LionEngineException(LionEngineException.ERROR_PRIVATE_CONSTRUCTOR);
    }
}

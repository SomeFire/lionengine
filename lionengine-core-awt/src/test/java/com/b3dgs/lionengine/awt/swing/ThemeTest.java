/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.awt.swing;

import org.junit.Assert;
import org.junit.Test;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.util.UtilEnum;
import com.b3dgs.lionengine.util.UtilTests;

/**
 * Test the theme class.
 */
public class ThemeTest
{
    /**
     * Test the enum.
     * 
     * @throws Exception If error.
     */
    @Test
    public void testEnum() throws Exception
    {
        UtilTests.testEnum(Theme.class);
    }

    /**
     * Test the theme.
     */
    @Test
    public void testTheme()
    {
        Theme.set(Theme.MOTIF);
        Theme.set(Theme.SYSTEM);
        Theme.set(Theme.METAL);
        try
        {
            Theme.set(Theme.GTK);
        }
        catch (final LionEngineException exception)
        {
            Assert.assertNotNull(exception);
            Verbose.info("Theme GTK not supported on platform - Skipped");
        }
    }

    /**
     * Test the fail case.
     */
    @Test(expected = LionEngineException.class)
    public void testFail()
    {
        Theme.set(null);
    }

    /**
     * Test the unknown enum.
     */
    @Test
    public void testAnEnumUnknown()
    {
        Verbose.info("*********************************** EXPECTED VERBOSE ***********************************");
        try
        {
            final UtilEnum<Theme> hack = new UtilEnum<Theme>(Theme.class, Theme.class);
            final Theme fail = hack.make("FAIL");
            hack.addByValue(fail);

            Theme.set(fail);
        }
        finally
        {
            Verbose.info("****************************************************************************************");
        }
    }
}
/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.core;

import org.junit.Assert;
import org.junit.Test;

import com.b3dgs.lionengine.Config;
import com.b3dgs.lionengine.Filter;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Ratio;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.mock.AppletMock;
import com.b3dgs.lionengine.mock.MediaMock;

/**
 * Test the config class.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class ConfigTest
{
    /** Resolution. */
    private static final Resolution OUTPUT = new Resolution(320, 240, 60);
    /** Config. */
    private static final Config CONFIG = new Config(ConfigTest.OUTPUT, 32, true);

    /**
     * Test the config failure resolution.
     */
    @Test(expected = LionEngineException.class)
    public void testFailureResolution()
    {
        Assert.assertNotNull(new Config(null, 1, true, Filter.NONE));
    }

    /**
     * Test the config failure depth.
     */
    @Test(expected = LionEngineException.class)
    public void testFailureDepth()
    {
        Assert.assertNotNull(new Config(ConfigTest.OUTPUT, 0, true, Filter.NONE));
    }

    /**
     * Test the config failure filter.
     */
    @Test(expected = LionEngineException.class)
    public void testFailureFilter()
    {
        Assert.assertNotNull(new Config(ConfigTest.OUTPUT, 1, true, null));
    }

    /**
     * Test the config failure ratio.
     */
    @Test(expected = LionEngineException.class)
    public void testFailureRatio()
    {
        ConfigTest.CONFIG.setRatio(0);
    }

    /**
     * Test the config getter.
     */
    @Test
    public void testGetter()
    {
        Assert.assertEquals(32, ConfigTest.CONFIG.getDepth());
        Assert.assertTrue(ConfigTest.CONFIG.isWindowed());
        Assert.assertEquals(Filter.NONE, ConfigTest.CONFIG.getFilter());
        Assert.assertEquals(ConfigTest.OUTPUT, ConfigTest.CONFIG.getOutput());
    }

    /**
     * Test the config source.
     */
    @Test
    public void testSource()
    {
        ConfigTest.CONFIG.setSource(ConfigTest.OUTPUT);
        Assert.assertEquals(ConfigTest.OUTPUT.getWidth(), ConfigTest.CONFIG.getSource().getWidth());
        Assert.assertEquals(ConfigTest.OUTPUT.getHeight(), ConfigTest.CONFIG.getSource().getHeight());
        Assert.assertEquals(ConfigTest.OUTPUT.getRate(), ConfigTest.CONFIG.getSource().getRate());
    }

    /**
     * Test the config ratio.
     */
    @Test
    public void testRatio()
    {
        ConfigTest.CONFIG.setRatio(Ratio.R16_10);
        Assert.assertEquals(384, ConfigTest.CONFIG.getOutput().getWidth());
        Assert.assertEquals(240, ConfigTest.CONFIG.getOutput().getHeight());
        Assert.assertEquals(Ratio.R16_10, ConfigTest.CONFIG.getOutput().getRatio(), 0.00000000001);
    }

    /**
     * Test the config applet.
     */
    @Test
    public void testApplet()
    {
        ConfigTest.CONFIG.setApplet(null);
        Assert.assertNull(ConfigTest.CONFIG.getApplet(null));
        Assert.assertNull(ConfigTest.CONFIG.getApplet(AppletMock.class));

        ConfigTest.CONFIG.setApplet(new AppletMock());
        Assert.assertNull(ConfigTest.CONFIG.getApplet(null));
        Assert.assertNotNull(ConfigTest.CONFIG.getApplet(AppletMock.class));
    }

    /**
     * Test the config icon.
     */
    @Test
    public void testIcon()
    {
        final Media icon = new MediaMock("icon");
        ConfigTest.CONFIG.setIcon(icon);
        Assert.assertEquals(icon, ConfigTest.CONFIG.getIcon());
    }
}
/*
 * Copyright (C) 2013-2019 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.b3dgs.lionengine.game.feature.rasterable;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;
import static com.b3dgs.lionengine.UtilAssert.assertFalse;
import static com.b3dgs.lionengine.UtilAssert.assertThrows;
import static com.b3dgs.lionengine.UtilAssert.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.UtilFolder;
import com.b3dgs.lionengine.graphic.FactoryGraphicMock;
import com.b3dgs.lionengine.graphic.Graphics;
import com.b3dgs.lionengine.graphic.ImageBuffer;

/**
 * Test {@link SetupSurfaceRastered}.
 */
public final class SetupSurfaceRasteredTest
{
    /** Object configuration file name. */
    private static final String OBJECT_XML = "object_raster.xml";
    /** Object configuration file name. */
    private static final String OBJECT_SMOOTH_XML = "object_raster_smooth.xml";
    /** Raster configuration file name. */
    private static final String RASTER_XML = "raster.xml";

    /**
     * Prepare test.
     */
    @BeforeAll
    public static void beforeTests()
    {
        Medias.setLoadFromJar(SetupSurfaceRasteredTest.class);
        Graphics.setFactoryGraphic(new FactoryGraphicMock());
    }

    /**
     * Clean up test.
     */
    @AfterAll
    public static void afterTests()
    {
        Medias.setLoadFromJar(null);
        Graphics.setFactoryGraphic(null);
    }

    /**
     * Test the setup surface rastered config.
     */
    @Test
    public void testConfig()
    {
        final Media raster = Medias.create(RASTER_XML);
        final SetupSurfaceRastered setup = new SetupSurfaceRastered(Medias.create(OBJECT_XML), raster);

        assertEquals(raster, setup.getFile());
        assertFalse(setup.hasSmooth());

        for (final ImageBuffer buffer : setup.getRasters())
        {
            assertEquals(setup.getSurface().getWidth(), buffer.getWidth());
            assertEquals(setup.getSurface().getHeight(), buffer.getHeight());
        }

        UtilFolder.deleteDirectory(Medias.create("void").getFile().getParentFile());
    }

    /**
     * Test the setup surface rastered config.
     */
    @Test
    public void testConfigSmooth()
    {
        final Media raster = Medias.create(RASTER_XML);
        final SetupSurfaceRastered setup = new SetupSurfaceRastered(Medias.create(OBJECT_SMOOTH_XML), raster);

        assertEquals(raster, setup.getFile());
        assertTrue(setup.hasSmooth());
        for (final ImageBuffer buffer : setup.getRasters())
        {
            assertEquals(setup.getSurface().getWidth(), buffer.getWidth());
            assertEquals(setup.getSurface().getHeight(), buffer.getHeight());
        }
        UtilFolder.deleteDirectory(Medias.create("void").getFile().getParentFile());
    }

    /**
     * Test the setup surface rastered without raster.
     */
    @Test
    public void testConfigNoRaster()
    {
        assertThrows(() -> new SetupSurfaceRastered(Medias.create(OBJECT_XML), null),
                     "The following attribute does not exist: file");
    }
}

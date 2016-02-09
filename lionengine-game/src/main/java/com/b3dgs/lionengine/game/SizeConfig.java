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
package com.b3dgs.lionengine.game;

import com.b3dgs.lionengine.LionEngineException;

/**
 * Represents the size data from a configurer.
 * 
 * @see Configurer
 */
public final class SizeConfig
{
    /** Size node name. */
    public static final String SIZE = Configurer.PREFIX + "size";
    /** Size width node. */
    public static final String SIZE_WIDTH = "width";
    /** Size height node. */
    public static final String SIZE_HEIGHT = "height";

    /**
     * Create the size data from node.
     * 
     * @param configurer The configurer reference.
     * @return The size data.
     * @throws LionEngineException If unable to read node.
     */
    public static SizeConfig create(Configurer configurer)
    {
        final int width = configurer.getInteger(SizeConfig.SIZE_WIDTH, SizeConfig.SIZE);
        final int height = configurer.getInteger(SizeConfig.SIZE_HEIGHT, SizeConfig.SIZE);

        return new SizeConfig(width, height);
    }

    /** The width value. */
    private final int width;
    /** The height value. */
    private final int height;

    /**
     * Create a size configuration.
     * 
     * @param width The width value.
     * @param height The height value.
     */
    public SizeConfig(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    /**
     * Get the width value.
     * 
     * @return The width value.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Get the height value.
     * 
     * @return The height value.
     */
    public int getHeight()
    {
        return height;
    }
}
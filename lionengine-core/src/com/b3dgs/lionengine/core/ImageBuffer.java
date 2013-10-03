/*
 * Copyright (C) 2013 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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

/**
 * Represents the image buffer in memory.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public interface ImageBuffer
{
    /**
     * Create the image graphic context.
     * 
     * @return The graphic context.
     */
    Graphic createGraphic();

    /**
     * Sets a pixel color.
     * 
     * @param x the X coordinate of the pixel to set
     * @param y the Y coordinate of the pixel to set
     * @param rgb the RGB value
     */
    void setRgb(int x, int y, int rgb);

    /**
     * Sets an array of integer pixels, into a portion of the image data.
     * 
     * @param startX the starting X coordinate
     * @param startY the starting Y coordinate
     * @param w width of the region
     * @param h height of the region
     * @param rgbArray the rgb pixels
     * @param offset offset into the <code>rgbArray</code>
     * @param scansize scanline stride for the <code>rgbArray</code>
     */
    void setRgb(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize);

    /**
     * Returns an array of integer pixels representing the image data.
     * 
     * @param x the starting X coordinate
     * @param y the starting Y coordinate
     * @return The pixel color.
     */
    int getRgb(int x, int y);

    /**
     * Returns an array of integer pixels representing the image data.
     * 
     * @param startX the starting X coordinate
     * @param startY the starting Y coordinate
     * @param w width of region
     * @param h height of region
     * @param rgbArray if not <code>null</code>, the rgb pixels are written here
     * @param offset offset into the <code>rgbArray</code>
     * @param scansize scanline stride for the <code>rgbArray</code>
     * @return array of RGB pixels.
     */
    int[] getRgb(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize);

    /**
     * Get the surface width.
     * 
     * @return The surface width.
     */
    int getWidth();

    /**
     * Get the surface height.
     * 
     * @return The surface height.
     */
    int getHeight();

    /**
     * Get the image transparency type.
     * 
     * @return The image transparency type.
     */
    Transparency getTransparency();
}

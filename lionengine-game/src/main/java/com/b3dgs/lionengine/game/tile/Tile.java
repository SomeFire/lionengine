/*
 * Copyright (C) 2013-2015 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.tile;

import com.b3dgs.lionengine.game.Featurable;

/**
 * Tile representation with the following data:
 * <ul>
 * <li><code>sheet</code> : tile sheet number</li>
 * <li><code>number</code> : tile number inside tilesheet</li>
 * <li><code>x and y</code> : real location on map</li>
 * </ul>
 * <p>
 * A tile represents a surface reference, localized on a {@link com.b3dgs.lionengine.game.map.MapTile}.
 * </p>
 * <p>
 * Services can be extended by using the {@link TileFeature} layer.
 * </p>
 * 
 * @see com.b3dgs.lionengine.game.map.MapTile
 */
public interface Tile extends Featurable<TileFeature>
{
    /**
     * Set sheet number.
     * 
     * @param sheet The sheet number (must not be <code>null</code>).
     */
    void setSheet(Integer sheet);

    /**
     * Set the group name.
     * 
     * @param name The group name.
     */
    void setGroup(String name);

    /**
     * Set tile index inside sheet.
     * 
     * @param number The tile index.
     */
    void setNumber(int number);

    /**
     * Set tile location x.
     * 
     * @param x The tile location x.
     */
    void setX(int x);

    /**
     * Set tile location y.
     * 
     * @param y The tile location y.
     */
    void setY(int y);

    /**
     * Get the width.
     * 
     * @return The tile width.
     */
    int getWidth();

    /**
     * Get the height.
     * 
     * @return The tile height.
     */
    int getHeight();

    /**
     * Get sheet number.
     * 
     * @return The sheet number.
     */
    Integer getSheet();

    /**
     * Get the group name.
     * 
     * @return The group name.
     */
    String getGroup();

    /**
     * Get tile index number.
     * 
     * @return The tile index number.
     */
    int getNumber();

    /**
     * Get tile location x.
     * 
     * @return The tile location x.
     */
    int getX();

    /**
     * Get tile location y.
     * 
     * @return The tile location y.
     */
    int getY();
}
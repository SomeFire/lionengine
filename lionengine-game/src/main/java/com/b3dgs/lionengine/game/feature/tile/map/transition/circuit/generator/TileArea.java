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
package com.b3dgs.lionengine.game.feature.tile.map.transition.circuit.generator;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.Tiled;

/**
 * Represents the tile area.
 */
public class TileArea implements Tiled
{
    /** The horizontal location in tile. */
    private final int tx;
    /** The vertical location in tile. */
    private final int ty;
    /** The width in tile. */
    private final int tw;
    /** The height in tile. */
    private final int th;

    /**
     * Create the tile area.
     * 
     * @param tx The horizontal location in tile (superior or equal to 0).
     * @param ty The vertical location in tile (superior or equal to 0).
     * @param tw The width in tile (strictly superior to 0).
     * @param th The height in tile (strictly superior to 0).
     * @throws LionEngineException If invalid arguments.
     */
    public TileArea(int tx, int ty, int tw, int th)
    {
        super();

        Check.superiorOrEqual(tx, 0);
        Check.superiorOrEqual(ty, 0);
        Check.superiorStrict(tw, 0);
        Check.superiorStrict(th, 0);

        this.tx = tx;
        this.ty = ty;
        this.tw = tw;
        this.th = th;
    }

    /*
     * Tiled
     */

    @Override
    public int getInTileX()
    {
        return tx;
    }

    @Override
    public int getInTileY()
    {
        return ty;
    }

    @Override
    public int getInTileWidth()
    {
        return tw;
    }

    @Override
    public int getInTileHeight()
    {
        return th;
    }
}

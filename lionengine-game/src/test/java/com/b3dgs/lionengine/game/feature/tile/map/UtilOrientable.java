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
package com.b3dgs.lionengine.game.feature.tile.map;

import com.b3dgs.lionengine.game.Tiled;

/**
 * Utilities dedicated to orientable test.
 */
final class UtilOrientable
{
    /**
     * Point to a tiled.
     * 
     * @param orientable The orientable.
     * @param tx The horizontal location.
     * @param ty The vertical location.
     */
    public static void pointToTiled(Orientable orientable, final int tx, final int ty)
    {
        orientable.pointTo(new Tiled()
        {
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
                return 0;
            }

            @Override
            public int getInTileHeight()
            {
                return 0;
            }
        });
    }
}

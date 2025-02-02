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

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.Orientation;
import com.b3dgs.lionengine.game.Tiled;
import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Recyclable;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Transformable;

/**
 * Orientable model implementation.
 */
public class OrientableModel extends FeatureModel implements Orientable, Recyclable
{
    /** Map reference. */
    private final MapTile map;
    /** Localizable reference. */
    private Transformable transformable;
    /** Current orientation. */
    private Orientation orientation = Orientation.NORTH;

    /**
     * Create feature.
     * <p>
     * The {@link Services} must provide:
     * </p>
     * <ul>
     * <li>{@link MapTile}</li>
     * </ul>
     * <p>
     * The {@link Featurable} must have:
     * </p>
     * <ul>
     * <li>{@link Transformable}</li>
     * </ul>
     * 
     * @param services The services reference (must not be <code>null</code>).
     * @throws LionEngineException If invalid argument.
     */
    public OrientableModel(Services services)
    {
        super();

        Check.notNull(services);

        map = services.get(MapTile.class);
    }

    /*
     * Orientable
     */

    @Override
    public void prepare(FeatureProvider provider)
    {
        transformable = provider.getFeature(Transformable.class);
    }

    @Override
    public void pointTo(int dtx, int dty)
    {
        final int tx = map.getInTileX(transformable);
        final int ty = map.getInTileY(transformable);
        final Orientation next = Orientation.get(tx, ty, dtx, dty);
        if (next != null)
        {
            orientation = next;
        }
    }

    @Override
    public void pointTo(Tiled tiled)
    {
        pointTo(tiled.getInTileX(), tiled.getInTileY());
    }

    @Override
    public void setOrientation(Orientation orientation)
    {
        Check.notNull(orientation);

        this.orientation = orientation;
    }

    @Override
    public Orientation getOrientation()
    {
        return orientation;
    }

    /*
     * Recyclable
     */

    @Override
    public void recycle()
    {
        orientation = Orientation.NORTH;
    }
}

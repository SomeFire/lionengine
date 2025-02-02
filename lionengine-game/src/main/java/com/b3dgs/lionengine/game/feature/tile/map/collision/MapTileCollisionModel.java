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
package com.b3dgs.lionengine.game.feature.tile.map.collision;

import java.util.Collection;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Xml;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGroup;

/**
 * Map tile collision model implementation.
 * <p>
 * The {@link Services} must provide:
 * </p>
 * <ul>
 * <li>{@link MapTile}</li>
 * <li>{@link MapTileGroup}</li>
 * </ul>
 */
public class MapTileCollisionModel extends FeatureModel implements MapTileCollision
{
    /** Map collision loader. */
    private final MapTileCollisionLoader loader;
    /** Map collision computer. */
    private final MapTileCollisionComputer computer;
    /** Map reference. */
    private final MapTile map;
    /** Map tile group. */
    private final MapTileGroup mapGroup;

    /**
     * Create feature.
     * 
     * @param services The services reference (must not be <code>null</code>).
     * @throws LionEngineException If invalid argument.
     */
    public MapTileCollisionModel(Services services)
    {
        super();

        Check.notNull(services);

        map = services.get(MapTile.class);
        mapGroup = map.getFeature(MapTileGroup.class);
        loader = new MapTileCollisionLoader(map, mapGroup);
        computer = new MapTileCollisionComputer(map);
    }

    /*
     * MapTileCollision
     */

    @Override
    public void loadCollisions(Media collisionFormulas, Media collisionGroups)
    {
        loader.loadCollisions(this, collisionFormulas, collisionGroups);
    }

    @Override
    public void loadCollisions(CollisionFormulaConfig formulasConfig, CollisionGroupConfig groupsConfig)
    {
        loader.loadCollisions(this, formulasConfig, groupsConfig);
    }

    @Override
    public void saveCollisions()
    {
        final Media formulasConfig = loader.getFormulasConfig();
        if (formulasConfig != null)
        {
            final Xml formulasRoot = new Xml(CollisionFormulaConfig.NODE_FORMULAS);
            for (final CollisionFormula formula : getCollisionFormulas())
            {
                CollisionFormulaConfig.exports(formulasRoot, formula);
            }
            formulasRoot.save(formulasConfig);
        }

        final Media groupsConfig = loader.getCollisionsConfig();
        if (groupsConfig != null)
        {
            final Xml groupsNode = new Xml(CollisionGroupConfig.NODE_COLLISIONS);
            for (final CollisionGroup group : getCollisionGroups())
            {
                CollisionGroupConfig.exports(groupsNode, group);
            }
            groupsNode.save(groupsConfig);
        }
    }

    /*
     * MapTileCollision
     */

    @Override
    public CollisionResult computeCollision(Transformable transformable, CollisionCategory category)
    {
        return computer.computeCollision(transformable, category);
    }

    @Override
    public CollisionFormula getCollisionFormula(String name)
    {
        return loader.getCollisionFormula(name);
    }

    @Override
    public CollisionGroup getCollisionGroup(String name)
    {
        return loader.getCollisionGroup(name);
    }

    @Override
    public Collection<CollisionFormula> getCollisionFormulas()
    {
        return loader.getCollisionFormulas();
    }

    @Override
    public Collection<CollisionGroup> getCollisionGroups()
    {
        return loader.getCollisionGroups();
    }

    @Override
    public Media getFormulasConfig()
    {
        return loader.getFormulasConfig();
    }

    @Override
    public Media getCollisionsConfig()
    {
        return loader.getCollisionsConfig();
    }
}

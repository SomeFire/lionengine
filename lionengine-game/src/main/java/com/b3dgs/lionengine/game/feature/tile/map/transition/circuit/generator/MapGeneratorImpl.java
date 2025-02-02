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

import java.util.Collection;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGame;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGroup;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGroupModel;
import com.b3dgs.lionengine.game.feature.tile.map.transition.MapTileTransition;
import com.b3dgs.lionengine.game.feature.tile.map.transition.MapTileTransitionModel;
import com.b3dgs.lionengine.game.feature.tile.map.transition.circuit.MapTileCircuit;
import com.b3dgs.lionengine.game.feature.tile.map.transition.circuit.MapTileCircuitModel;

/**
 * Default map generator implementation.
 */
public class MapGeneratorImpl implements MapGenerator
{
    /**
     * Create map generator.
     */
    public MapGeneratorImpl()
    {
        super();
    }

    /*
     * MapGenerator
     */

    @Override
    public MapTile generateMap(GeneratorParameter parameters,
                               Collection<Media> levels,
                               Media sheetsConfig,
                               Media groupsConfig)
    {
        final Services services = new Services();
        final MapTile map = services.create(MapTileGame.class);
        map.loadSheets(sheetsConfig);

        final MapTileGroup mapGroup = map.addFeatureAndGet(new MapTileGroupModel());
        final MapTileTransition mapTransition = map.addFeatureAndGet(new MapTileTransitionModel(services));
        final MapTileCircuit mapCircuit = map.addFeatureAndGet(new MapTileCircuitModel(services));

        mapGroup.loadGroups(groupsConfig);
        mapTransition.loadTransitions(levels, sheetsConfig, groupsConfig);
        mapCircuit.loadCircuits(levels, sheetsConfig, groupsConfig);

        parameters.apply(map);

        return map;
    }
}

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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.tile.Tile;
import com.b3dgs.lionengine.game.feature.tile.TileGroup;
import com.b3dgs.lionengine.game.feature.tile.TileGroupType;
import com.b3dgs.lionengine.game.feature.tile.TileGroupsConfig;
import com.b3dgs.lionengine.game.feature.tile.TileRef;

/**
 * Map tile group model implementation.
 */
public class MapTileGroupModel extends FeatureModel implements MapTileGroup
{
    /** No group name. */
    public static final String NO_GROUP_NAME = "none";

    /** Group tiles mapping. */
    private final Map<String, Collection<TileRef>> groupTiles = new HashMap<>();
    /** Group types mapping. */
    private final Map<String, TileGroupType> groupTypes = new HashMap<>();
    /** Tiles group mapping. */
    private final Map<TileRef, String> tilesGroup = new HashMap<>();
    /** Groups configuration file. */
    private Media groupsConfig;

    /**
     * Create feature.
     */
    public MapTileGroupModel()
    {
        super();

        groupTiles.put(NO_GROUP_NAME, new HashSet<TileRef>());
    }

    /*
     * MapTileGroup
     */

    @Override
    public void loadGroups(Collection<TileGroup> groups)
    {
        groupTiles.clear();
        tilesGroup.clear();
        groupTypes.clear();

        for (final TileGroup group : groups)
        {
            final String name = group.getName();
            groupTiles.put(name, group.getTiles());
            groupTypes.put(name, group.getType());
            for (final TileRef tile : group.getTiles())
            {
                tilesGroup.put(tile, name);
            }
        }
    }

    @Override
    public void loadGroups(Media groupsConfig)
    {
        this.groupsConfig = groupsConfig;
        loadGroups(TileGroupsConfig.imports(groupsConfig));
    }

    @Override
    public void changeGroup(Tile tile, String group)
    {
        final TileRef ref = new TileRef(tile);
        final String oldGroup = getGroup(tile);

        if (groupTiles.containsKey(oldGroup))
        {
            groupTiles.get(oldGroup).remove(ref);
        }
        if (group != null)
        {
            tilesGroup.put(ref, group);
            if (!groupTiles.containsKey(group))
            {
                groupTiles.put(group, new HashSet<TileRef>());
            }
            groupTiles.get(group).add(ref);
        }
        else
        {
            tilesGroup.remove(ref);
        }
    }

    @Override
    public Media getGroupsConfig()
    {
        return groupsConfig;
    }

    @Override
    public Collection<TileRef> getGroup(String name)
    {
        if (groupTiles.containsKey(name))
        {
            return groupTiles.get(name);
        }
        return groupTiles.get(NO_GROUP_NAME);
    }

    @Override
    public String getGroup(TileRef tile)
    {
        if (tilesGroup.containsKey(tile))
        {
            return tilesGroup.get(tile);
        }
        return NO_GROUP_NAME;
    }

    @Override
    public String getGroup(Tile tile)
    {
        return getGroup(new TileRef(tile));
    }

    @Override
    public String getGroup(Integer sheet, int number)
    {
        final TileRef ref = new TileRef(sheet, number);
        for (final Entry<TileRef, String> tile : tilesGroup.entrySet())
        {
            if (tile.getKey().equals(ref))
            {
                return tile.getValue();
            }
        }
        return NO_GROUP_NAME;
    }

    @Override
    public TileGroupType getType(String name)
    {
        if (groupTypes.containsKey(name))
        {
            return groupTypes.get(name);
        }
        return TileGroupType.NONE;
    }

    @Override
    public TileGroupType getType(Tile tile)
    {
        return getType(getGroup(tile));
    }

    @Override
    public Collection<String> getGroups()
    {
        return groupTiles.keySet();
    }
}

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

import java.util.ArrayList;
import java.util.Collection;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Xml;
import com.b3dgs.lionengine.game.Configurer;
import com.b3dgs.lionengine.game.feature.tile.TileGroupsConfig;

/**
 * Represents the collision category configuration.
 * <p>
 * This class is Thread-Safe.
 * </p>
 * 
 * @see CollisionCategory
 */
public final class CollisionCategoryConfig
{
    /** Category node name. */
    public static final String NODE_CATEGORY = Constant.XML_PREFIX + "category";
    /** Category attribute name. */
    public static final String ATT_NAME = "name";
    /** Category attribute axis. */
    public static final String ATT_AXIS = "axis";
    /** Category attribute horizontal offset. */
    public static final String ATT_X = "x";
    /** Category attribute vertical offset. */
    public static final String ATT_Y = "y";
    /** Category attribute glue flag. */
    public static final String ATT_GLUE = "glue";
    /** Unknown axis error. */
    private static final String ERROR_AXIS = "Unknown axis: ";

    /**
     * Create the collision category data from node (should only be used to display names, as real content is
     * <code>null</code>, mainly UI specific to not have dependency on {@link MapTileCollision}).
     * 
     * @param root The node root reference (must not be <code>null</code>).
     * @return The collisions category data.
     * @throws LionEngineException If unable to read node.
     */
    public static Collection<CollisionCategory> imports(Xml root)
    {
        Check.notNull(root);

        final Collection<Xml> childrenCategory = root.getChildren(NODE_CATEGORY);
        final Collection<CollisionCategory> categories = new ArrayList<>(childrenCategory.size());

        for (final Xml node : childrenCategory)
        {
            final Collection<Xml> childrenGroup = node.getChildren(TileGroupsConfig.NODE_GROUP);
            final Collection<CollisionGroup> groups = new ArrayList<>(childrenGroup.size());

            for (final Xml group : childrenGroup)
            {
                final String name = group.getText();
                groups.add(new CollisionGroup(name, new ArrayList<CollisionFormula>(0)));
            }

            final String name = node.readString(ATT_NAME);
            final Axis axis = Axis.valueOf(node.readString(ATT_AXIS));
            final int x = node.readInteger(ATT_X);
            final int y = node.readInteger(ATT_Y);
            final boolean glue = node.readBoolean(true, ATT_GLUE);

            final CollisionCategory category = new CollisionCategory(name, axis, x, y, glue, groups);
            categories.add(category);
        }

        return categories;
    }

    /**
     * Create the categories data from nodes.
     * 
     * @param configurer The configurer reference (must not be <code>null</code>).
     * @param map The map reference (must not be <code>null</code>).
     * @return The category collisions data.
     * @throws LionEngineException If unable to read node.
     */
    public static Collection<CollisionCategory> imports(Configurer configurer, MapTileCollision map)
    {
        Check.notNull(configurer);
        Check.notNull(map);

        final Collection<Xml> children = configurer.getRoot().getChildren(NODE_CATEGORY);
        final Collection<CollisionCategory> categories = new ArrayList<>(children.size());

        for (final Xml node : children)
        {
            final CollisionCategory category = imports(node, map);
            categories.add(category);
        }

        return categories;
    }

    /**
     * Create the category data from node.
     * 
     * @param root The root reference (must not be <code>null</code>).
     * @param map The map reference (must not be <code>null</code>).
     * @return The category node instance.
     * @throws LionEngineException If unable to read node.
     */
    public static CollisionCategory imports(Xml root, MapTileCollision map)
    {
        Check.notNull(root);
        Check.notNull(map);

        final Collection<Xml> children = root.getChildren(TileGroupsConfig.NODE_GROUP);
        final Collection<CollisionGroup> groups = new ArrayList<>(children.size());

        for (final Xml groupNode : children)
        {
            final String groupName = groupNode.getText();
            final CollisionGroup group = map.getCollisionGroup(groupName);
            groups.add(group);
        }

        final String axisName = root.readString(ATT_AXIS);
        final Axis axis;
        try
        {
            axis = Axis.valueOf(axisName);
        }
        catch (final IllegalArgumentException exception)
        {
            throw new LionEngineException(exception, ERROR_AXIS + axisName);
        }

        final int x = root.readInteger(ATT_X);
        final int y = root.readInteger(ATT_Y);
        final boolean glue = root.readBoolean(true, ATT_GLUE);
        final String name = root.readString(ATT_NAME);

        return new CollisionCategory(name, axis, x, y, glue, groups);
    }

    /**
     * Export the collision category data as a node.
     * 
     * @param root The node root (must not be <code>null</code>).
     * @param category The collision category to export (must not be <code>null</code>).
     * @throws LionEngineException If error on writing.
     */
    public static void exports(Xml root, CollisionCategory category)
    {
        Check.notNull(root);
        Check.notNull(category);

        final Xml node = root.createChild(NODE_CATEGORY);
        node.writeString(ATT_NAME, category.getName());
        node.writeString(ATT_AXIS, category.getAxis().name());
        node.writeInteger(ATT_X, category.getOffsetX());
        node.writeInteger(ATT_Y, category.getOffsetY());
        node.writeBoolean(ATT_GLUE, category.isGlue());

        for (final CollisionGroup group : category.getGroups())
        {
            final Xml groupNode = node.createChild(TileGroupsConfig.NODE_GROUP);
            groupNode.setText(group.getName());
        }
    }

    /**
     * Disabled constructor.
     */
    private CollisionCategoryConfig()
    {
        throw new LionEngineException(LionEngineException.ERROR_PRIVATE_CONSTRUCTOR);
    }
}

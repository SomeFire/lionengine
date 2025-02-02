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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Xml;

/**
 * Represents the collision group data.
 * <p>
 * This class is Thread-Safe.
 * </p>
 * 
 * @see CollisionGroup
 */
public final class CollisionGroupConfig
{
    /** Configuration file name. */
    public static final String FILENAME = "collisions.xml";
    /** Collision group root node. */
    public static final String NODE_COLLISIONS = Constant.XML_PREFIX + "collisions";
    /** Collision group node. */
    public static final String NODE_COLLISION = Constant.XML_PREFIX + "collision";
    /** Group name attribute. */
    public static final String ATT_GROUP = "group";

    /**
     * Create the collision group data from node (should only be used to display names, as real content is
     * <code>null</code>, mainly UI specific to not have dependency on {@link MapTileCollision}).
     * 
     * @param config The tile collision groups descriptor (must not be <code>null</code>).
     * @return The collisions group data.
     * @throws LionEngineException If unable to read node.
     */
    public static CollisionGroupConfig imports(Media config)
    {
        final Xml root = new Xml(config);
        final Collection<Xml> childrenCollision = root.getChildren(NODE_COLLISION);
        final Map<String, CollisionGroup> groups = new HashMap<>(childrenCollision.size());

        for (final Xml node : childrenCollision)
        {
            final Collection<Xml> childrenFormula = node.getChildren(CollisionFormulaConfig.NODE_FORMULA);
            final Collection<CollisionFormula> formulas = new ArrayList<>(childrenFormula.size());

            for (final Xml formula : childrenFormula)
            {
                final String formulaName = formula.getText();
                formulas.add(new CollisionFormula(formulaName, null, null, null));
            }

            final String groupName = node.readString(ATT_GROUP);
            final CollisionGroup collision = new CollisionGroup(groupName, formulas);
            groups.put(groupName, collision);
        }

        return new CollisionGroupConfig(groups);
    }

    /**
     * Create the collision group data from node.
     * 
     * @param root The node root reference (must not be <code>null</code>).
     * @param map The map reference (must not be <code>null</code>).
     * @return The collisions group data.
     * @throws LionEngineException If unable to read node.
     */
    public static CollisionGroupConfig imports(Xml root, MapTileCollision map)
    {
        Check.notNull(root);
        Check.notNull(map);

        final Collection<Xml> childrenCollision = root.getChildren(NODE_COLLISION);
        final Map<String, CollisionGroup> groups = new HashMap<>(childrenCollision.size());

        for (final Xml node : childrenCollision)
        {
            final Collection<Xml> childrenFormula = node.getChildren(CollisionFormulaConfig.NODE_FORMULA);
            final Collection<CollisionFormula> formulas = new ArrayList<>(childrenFormula.size());

            for (final Xml formula : childrenFormula)
            {
                final String formulaName = formula.getText();
                formulas.add(map.getCollisionFormula(formulaName));
            }

            final String groupName = node.readString(ATT_GROUP);
            final CollisionGroup collision = new CollisionGroup(groupName, formulas);
            groups.put(groupName, collision);
        }

        return new CollisionGroupConfig(groups);
    }

    /**
     * Export the collision group data as a node.
     * 
     * @param root The node root (must not be <code>null</code>).
     * @param group The collision group to export (must not be <code>null</code>).
     * @throws LionEngineException If error on writing.
     */
    public static void exports(Xml root, CollisionGroup group)
    {
        Check.notNull(root);
        Check.notNull(group);

        final Xml node = root.createChild(NODE_COLLISION);
        node.writeString(ATT_GROUP, group.getName());

        for (final CollisionFormula formula : group.getFormulas())
        {
            final Xml nodeFormula = node.createChild(CollisionFormulaConfig.NODE_FORMULA);
            nodeFormula.setText(formula.getName());
        }
    }

    /**
     * Remove the group node.
     * 
     * @param root The root node (must not be <code>null</code>).
     * @param group The group name to remove (must not be <code>null</code>).
     * @throws LionEngineException If invalid argument.
     */
    public static void remove(Xml root, String group)
    {
        Check.notNull(root);
        Check.notNull(group);

        for (final Xml node : root.getChildren(NODE_COLLISION))
        {
            if (node.readString(ATT_GROUP).equals(group))
            {
                root.removeChild(node);
            }
        }
    }

    /**
     * Check if node has group node.
     * 
     * @param root The root node (must not be <code>null</code>).
     * @param group The group name to check (must not be <code>null</code>).
     * @return <code>true</code> if has group, <code>false</code> else.
     * @throws LionEngineException If invalid argument.
     */
    public static boolean has(Xml root, String group)
    {
        Check.notNull(root);
        Check.notNull(group);

        for (final Xml node : root.getChildren(NODE_COLLISION))
        {
            if (node.readString(ATT_GROUP).equals(group))
            {
                return true;
            }
        }
        return false;
    }

    /** Collision groups list. */
    private final Map<String, CollisionGroup> groups;

    /**
     * Create a collision groups config map.
     * 
     * @param groups The collisions groups mapping (must not be <code>null</code>).
     * @throws LionEngineException If invalid argument.
     */
    public CollisionGroupConfig(Map<String, CollisionGroup> groups)
    {
        super();

        Check.notNull(groups);

        this.groups = new HashMap<>(groups);
    }

    /**
     * Get a collision formula data from its name.
     * 
     * @param name The formula name (must not be <code>null</code>).
     * @return The formula reference.
     * @throws LionEngineException If the formula with the specified name is not found.
     */
    public CollisionGroup getGroup(String name)
    {
        Check.notNull(name);

        final CollisionGroup group = groups.get(name);
        Check.notNull(group);

        return group;
    }

    /**
     * Get all groups as read only.
     * 
     * @return The groups map, where key is the group name.
     */
    public Map<String, CollisionGroup> getGroups()
    {
        return Collections.unmodifiableMap(groups);
    }
}

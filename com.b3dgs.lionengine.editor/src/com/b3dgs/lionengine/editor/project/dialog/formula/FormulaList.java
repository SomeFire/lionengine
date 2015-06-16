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
package com.b3dgs.lionengine.editor.project.dialog.formula;

import java.util.ArrayList;
import java.util.Collection;

import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.editor.ObjectList;
import com.b3dgs.lionengine.editor.ObjectListListener;
import com.b3dgs.lionengine.editor.world.WorldViewModel;
import com.b3dgs.lionengine.editor.world.WorldViewRenderer;
import com.b3dgs.lionengine.game.Axis;
import com.b3dgs.lionengine.game.collision.CollisionConstraint;
import com.b3dgs.lionengine.game.collision.CollisionFormula;
import com.b3dgs.lionengine.game.collision.CollisionFunctionLinear;
import com.b3dgs.lionengine.game.collision.CollisionRange;
import com.b3dgs.lionengine.game.configurer.ConfigCollisionFormula;
import com.b3dgs.lionengine.game.configurer.ConfigTileGroup;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.map.MapTileCollision;
import com.b3dgs.lionengine.stream.Stream;
import com.b3dgs.lionengine.stream.XmlNode;

/**
 * Represents the formulas list, allowing to add and remove {@link CollisionFormula}.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class FormulaList
        extends ObjectList<CollisionFormula>
        implements ObjectListListener<CollisionFormula>
{
    /**
     * Remove the formula from configuration.
     * 
     * @param formulasConfig The formula config file.
     * @param formula The formula to remove.
     */
    private static void removeFormula(Media formulasConfig, CollisionFormula formula)
    {
        final XmlNode node = Stream.loadXml(formulasConfig);
        final Collection<XmlNode> toRemove = new ArrayList<>();
        for (final XmlNode nodeFormula : node.getChildren(ConfigCollisionFormula.FORMULA))
        {
            if (WorldViewRenderer.groupEquals(nodeFormula.readString(ConfigTileGroup.NAME), formula.getName()))
            {
                toRemove.add(nodeFormula);
            }
        }
        for (final XmlNode remove : toRemove)
        {
            node.removeChild(remove);
        }
        toRemove.clear();
        Stream.saveXml(node, formulasConfig);
    }

    /** Last config used. */
    private Media config;

    /**
     * Create the group list.
     */
    public FormulaList()
    {
        super(CollisionFormula.class);
    }

    /**
     * Load the existing formulas from the object configurer.
     * 
     * @param config The config file.
     */
    public void loadFormulas(Media config)
    {
        this.config = config;
        final ConfigCollisionFormula configCollisionFormula = ConfigCollisionFormula.create(Stream.loadXml(config));
        final Collection<CollisionFormula> formulas = configCollisionFormula.getFormulas().values();
        loadObjects(formulas);
    }

    /*
     * ObjectList
     */

    @Override
    protected CollisionFormula copyObject(CollisionFormula formula)
    {
        return new CollisionFormula(formula.getName(), formula.getRange(), formula.getFunction(),
                formula.getConstraint());
    }

    @Override
    protected CollisionFormula createObject(String name)
    {
        return new CollisionFormula(name, new CollisionRange(Axis.Y, 0, 0, 0, 0), new CollisionFunctionLinear(0, 0),
                new CollisionConstraint(null, null, null, null));
    }

    /*
     * ObjectListListener
     */

    @Override
    public void notifyObjectSelected(CollisionFormula formula)
    {
        // Nothing to do
    }

    @Override
    public void notifyObjectDeleted(CollisionFormula formula)
    {
        final MapTile map = WorldViewModel.INSTANCE.getMap();
        if (map.hasFeature(MapTileCollision.class))
        {
            final MapTileCollision mapCollision = map.getFeature(MapTileCollision.class);
            final Media formulasConfig = mapCollision.getFormulasConfig();
            removeFormula(formulasConfig, formula);
            mapCollision.loadCollisions(formulasConfig, map.getGroupsConfig());
        }
        else if (config != null)
        {
            removeFormula(config, formula);
        }
    }
}
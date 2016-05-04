/*
 * Copyright (C) 2013-2016 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.collision.tile;

import org.junit.Assert;
import org.junit.Test;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.Orientation;
import com.b3dgs.lionengine.stream.Xml;
import com.b3dgs.lionengine.stream.XmlNode;
import com.b3dgs.lionengine.test.UtilTests;

/**
 * Test the collision constraint configuration class.
 */
public class CollisionConstraintConfigTest
{
    /** Constraint test. */
    private final CollisionConstraint constraint = new CollisionConstraint();

    /**
     * Test the constructor.
     * 
     * @throws Exception If error.
     */
    @Test(expected = LionEngineException.class)
    public void testConstructor() throws Exception
    {
        UtilTests.testPrivateConstructor(CollisionConstraintConfig.class);
    }

    /**
     * Test the import export with empty constraint.
     */
    @Test
    public void testEmpty()
    {
        final XmlNode root = Xml.create("constraint");
        CollisionConstraintConfig.exports(root, constraint);
        final CollisionConstraint imported = CollisionConstraintConfig.imports(root);

        Assert.assertEquals(constraint, imported);
    }

    /**
     * Test the import export.
     */
    @Test
    public void testConstraint()
    {
        final XmlNode root = Xml.create("constraint");
        constraint.add(Orientation.EAST, "group");
        CollisionConstraintConfig.exports(root, constraint);
        final CollisionConstraint imported = CollisionConstraintConfig.imports(root);

        Assert.assertEquals(constraint, imported);
    }
}

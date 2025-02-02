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
package com.b3dgs.lionengine.awt.graphic;

import java.awt.image.AffineTransformOp;

import org.junit.jupiter.api.BeforeAll;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.graphic.Graphics;
import com.b3dgs.lionengine.graphic.TransformTest;

/**
 * Test {@link TransformAwt}.
 */
public final class TransformAwtTest extends TransformTest
{
    /**
     * Prepare tests.
     */
    @BeforeAll
    public static void beforeTests()
    {
        Graphics.setFactoryGraphic(new FactoryGraphicAwt());
    }

    /*
     * TransformTest
     */

    @Override
    protected int getInterpolation(int value)
    {
        if (value == 1)
        {
            return AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
        }
        else if (value == 2)
        {
            return AffineTransformOp.TYPE_BILINEAR;
        }
        throw new LionEngineException("Unknown interpolation: " + String.valueOf(value));
    }
}

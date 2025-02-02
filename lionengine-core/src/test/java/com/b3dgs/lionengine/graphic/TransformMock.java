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
package com.b3dgs.lionengine.graphic;

import com.b3dgs.lionengine.UtilConversion;

/**
 * Mock transform.
 */
final class TransformMock implements Transform
{
    /** Horizontal scale. */
    private double sx;
    /** Vertical scale. */
    private double sy;
    /** Interpolation. */
    private int interpolation;

    /**
     * Create mock.
     */
    TransformMock()
    {
        super();
    }

    /*
     * Transform
     */

    @Override
    public void scale(double sx, double sy)
    {
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    public void setInterpolation(boolean bilinear)
    {
        interpolation = UtilConversion.boolToInt(bilinear);
    }

    @Override
    public double getScaleX()
    {
        return sx;
    }

    @Override
    public double getScaleY()
    {
        return sy;
    }

    @Override
    public int getInterpolation()
    {
        return interpolation;
    }
}

/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine;

/**
 * Describes a display resolution. It allows to define different parameters:
 * <ul>
 * <li><code>width & height</code> : represent the screen size</li>
 * <li><code>ratio</code>, which is computed by using the <code>width & height</code>, allows to know the screen ratio.</li>
 * <li><code>rate</code> : represents the screen refresh rate (in frames per seconds)</li>
 * </ul>
 * This class is mainly used to describe the display resolution chosen.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public final class Resolution
{
    /** Resolution width. */
    private int width;
    /** Resolution height. */
    private int height;
    /** Display rate. */
    private int rate;
    /** Resolution ratio. */
    private double ratio;

    /**
     * Create a resolution.
     * 
     * @param width The resolution width (in pixel) [> 0].
     * @param height The resolution height (in pixel) [> 0].
     * @param rate The refresh rate (usually 50 or 60) [>= 0].
     * @throws LionEngineException If arguments are invalid.
     */
    public Resolution(int width, int height, int rate) throws LionEngineException
    {
        set(width, height, rate);
    }

    /**
     * Set the resolution.
     * 
     * @param width The resolution width (in pixel).
     * @param height The resolution height (in pixel).
     * @throws LionEngineException If arguments are invalid.
     */
    public void set(int width, int height) throws LionEngineException
    {
        set(width, height, rate);
    }

    /**
     * Set the resolution.
     * 
     * @param width The resolution width (in pixel) [> 0].
     * @param height The resolution height (in pixel) [> 0].
     * @param rate The refresh rate in hertz (usually 50 or 60) [>= 0].
     * @throws LionEngineException If arguments are invalid.
     */
    public void set(int width, int height, int rate) throws LionEngineException
    {
        Check.superiorStrict(width, 0);
        Check.superiorStrict(height, 0);
        Check.superiorOrEqual(rate, 0);

        this.width = width;
        this.height = height;
        this.rate = rate;
        ratio = width / (double) height;
    }

    /**
     * Set the ratio and adapt the resolution to the new ratio (based on the height value).
     * 
     * @param ratio The new ratio [> 0].
     * @throws LionEngineException If ratio is not strictly positive.
     */
    public void setRatio(double ratio) throws LionEngineException
    {
        Check.superiorStrict(ratio, 0);

        if (!Ratio.equals(this.ratio, ratio))
        {
            width = (int) Math.ceil(height * ratio);
            width = (int) Math.floor(width / 2.0) * 2;
            this.ratio = ratio;
        }
    }

    /**
     * Set the refresh rate value in hertz.
     * 
     * @param rate The refresh rate value [>= 0].
     * @throws LionEngineException If ratio is not strictly positive.
     */
    public void setRate(int rate) throws LionEngineException
    {
        Check.superiorOrEqual(rate, 0);

        this.rate = rate;
    }

    /**
     * Get the resolution width.
     * 
     * @return The resolution width.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Get the resolution height.
     * 
     * @return The resolution height.
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Get the resolution ratio.
     * 
     * @return The resolution ratio.
     */
    public double getRatio()
    {
        return ratio;
    }

    /**
     * Get the display rate.
     * 
     * @return The display rate.
     */
    public int getRate()
    {
        return rate;
    }
}

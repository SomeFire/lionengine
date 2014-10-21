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
package com.b3dgs.lionengine.anim;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the anim state.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class AnimStateTest
{
    /**
     * Test the anim state enum.
     */
    @Test
    public void testAnimState()
    {
        Assert.assertNotNull(AnimState.values());
        Assert.assertEquals(AnimState.valueOf(AnimState.FINISHED.name()), AnimState.FINISHED);
    }

    /**
     * Test the anim state enum switch.
     */
    @Test
    public void testEnumSwitch()
    {
        for (final AnimState animState : AnimState.values())
        {
            switch (animState)
            {
                case FINISHED:
                    // Success
                    break;
                case PLAYING:
                    // Success
                    break;
                case REVERSING:
                    // Success
                    break;
                case STOPPED:
                    // Success
                    break;
                default:
                    Assert.fail();
            }
        }
    }
}
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
package com.b3dgs.lionengine.game;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;
import static com.b3dgs.lionengine.UtilAssert.assertThrows;
import static com.b3dgs.lionengine.UtilAssert.assertTrue;

import org.junit.jupiter.api.Test;

import com.b3dgs.lionengine.ListenableModel;

/**
 * Test {@link ListenableModel}.
 */
public final class ListenableModelTest
{
    private final ListenableModel<Object> model = new ListenableModel<>();

    /**
     * Test constructor.
     */
    @Test
    public void testNull()
    {
        assertThrows(() -> model.addListener(null), "Unexpected null argument !");
        assertThrows(() -> model.removeListener(null), "Unexpected null argument !");
    }

    /**
     * Test listenable.
     */
    @Test
    public void testListenable()
    {
        assertTrue(model.get().isEmpty());
        assertEquals(0, model.size());

        final Object object = new Object();
        model.addListener(object);

        assertEquals(1, model.size());
        assertEquals(object, model.get(0));

        model.removeListener(object);

        assertTrue(model.get().isEmpty());
        assertEquals(0, model.size());
    }
}

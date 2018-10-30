/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.feature;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;
import static com.b3dgs.lionengine.UtilAssert.assertNotNull;
import static com.b3dgs.lionengine.UtilAssert.assertNull;
import static com.b3dgs.lionengine.UtilAssert.assertThrows;
import static com.b3dgs.lionengine.UtilAssert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import com.b3dgs.lionengine.UtilReflection;

/**
 * Test {@link IdentifiableModel}.
 */
public final class IdentifiableModelTest
{
    /**
     * Test the id.
     * 
     * @throws ReflectiveOperationException If error.
     */
    @Test
    public void testId() throws ReflectiveOperationException
    {
        final Collection<Integer> ids = UtilReflection.getField(IdentifiableModel.class, "IDS");
        ids.clear();

        final Collection<Integer> recycle = UtilReflection.getField(IdentifiableModel.class, "RECYCLE");
        recycle.clear();

        final Field field = IdentifiableModel.class.getDeclaredField("lastId");
        UtilReflection.setAccessible(field, true);
        field.set(IdentifiableModel.class, Integer.valueOf(0));

        final Collection<Identifiable> identifiables = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            final Featurable featurable = new FeaturableModel();
            final Identifiable identifiable = featurable.getFeature(Identifiable.class);
            identifiable.prepare(featurable);
            identifiables.add(identifiable);

            assertEquals(Integer.valueOf(i), identifiable.getId());
        }

        for (final Identifiable identifiable : identifiables)
        {
            identifiable.destroy();
            identifiable.notifyDestroyed();

            assertNull(identifiable.getId());
        }

        final Featurable featurable = new FeaturableModel();
        final IdentifiableModel identifiable = featurable.getFeature(IdentifiableModel.class);
        featurable.addFeature(new Recycler());
        identifiable.prepare(featurable);
        assertEquals(Integer.valueOf(0), identifiable.getId());

        identifiable.destroy();
        identifiable.notifyDestroyed();

        assertNull(identifiable.getId());

        identifiable.destroy();
        identifiable.notifyDestroyed();

        assertNull(identifiable.getId());

        identifiable.recycle();

        assertNotNull(identifiable.getId());
    }

    /**
     * Test the maximum id.
     * 
     * @throws ReflectiveOperationException If error.
     */
    @Test
    public void testMaxId() throws ReflectiveOperationException
    {
        final HashSet<?> ids = UtilReflection.getField(IdentifiableModel.class, "IDS");
        final HashMap<?, ?> map = UtilReflection.getField(ids, "map");
        final Field size = map.getClass().getDeclaredField("size");
        UtilReflection.setAccessible(size, true);
        ids.clear();
        size.setInt(map, Integer.MAX_VALUE);

        final Field lastId = IdentifiableModel.class.getDeclaredField("lastId");
        UtilReflection.setAccessible(lastId, true);
        lastId.setInt(lastId, 0);

        final Queue<?> recycle = UtilReflection.getField(IdentifiableModel.class, "RECYCLE");
        recycle.clear();

        try
        {
            assertThrows(() -> new IdentifiableModel(), IdentifiableModel.ERROR_FREE_ID);
        }
        finally
        {
            lastId.setInt(lastId, 0);
            size.setInt(map, 0);
            ids.clear();
        }
    }

    /**
     * Test the listener.
     */
    @Test
    public void testListener()
    {
        final Identifiable identifiable = new IdentifiableModel();
        final AtomicBoolean destroyed = new AtomicBoolean();
        final IdentifiableListener listener = id -> destroyed.set(true);
        identifiable.prepare(new FeaturableModel());
        identifiable.addListener(listener);
        identifiable.destroy();
        identifiable.removeListener(listener);
        identifiable.notifyDestroyed();

        assertTrue(destroyed.get());
    }
}

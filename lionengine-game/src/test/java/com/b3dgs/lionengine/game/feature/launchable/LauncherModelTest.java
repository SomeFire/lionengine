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
package com.b3dgs.lionengine.game.feature.launchable;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;
import static com.b3dgs.lionengine.UtilAssert.assertFalse;
import static com.b3dgs.lionengine.UtilAssert.assertNotNull;
import static com.b3dgs.lionengine.UtilAssert.assertNull;
import static com.b3dgs.lionengine.UtilAssert.assertThrows;
import static com.b3dgs.lionengine.UtilAssert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.b3dgs.lionengine.Localizable;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.UtilTests;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.Factory;
import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.Handler;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.Setup;
import com.b3dgs.lionengine.game.feature.Transformable;
import com.b3dgs.lionengine.game.feature.TransformableModel;
import com.b3dgs.lionengine.game.feature.UtilSetup;

/**
 * Test {@link LauncherModel}.
 */
public final class LauncherModelTest
{
    /**
     * Prepare test.
     */
    @BeforeAll
    public static void beforeTests()
    {
        Medias.setResourcesDirectory(System.getProperty("java.io.tmpdir"));
    }

    /**
     * Clean up test.
     */
    @AfterAll
    public static void afterTests()
    {
        Medias.setResourcesDirectory(null);
    }

    private final Media launchableMedia = UtilSetup.createMedia(LaunchableObject.class);
    private final Media launcherMedia = UtilLaunchable.createLauncherMedia(launchableMedia);
    private final Services services = new Services();
    private final Setup setup = new Setup(launcherMedia);
    private final Featurable featurable = new FeaturableModel();
    private final Launcher launcher = UtilLaunchable.createLauncher(services, setup, featurable);

    /**
     * Clean test.
     */
    @AfterEach
    public void clean()
    {
        assertTrue(launchableMedia.getFile().delete());
        assertTrue(launcherMedia.getFile().delete());
    }

    /**
     * Test the config.
     */
    @Test
    public void testConfig()
    {
        assertEquals(0, launcher.getLevel());
        assertEquals(10, launcher.getRate());
        assertEquals(1.0, launcher.getOffsetX());
        assertEquals(2.0, launcher.getOffsetY());
    }

    /**
     * Test constructor with null services.
     */
    @Test
    public void testConstructorNullServices()
    {
        assertThrows(() -> new LauncherModel(null, null), "Unexpected null argument !");
    }

    /**
     * Test the launcher.
     * 
     * @throws InterruptedException If error.
     */
    @Test
    public void testLauncher() throws InterruptedException
    {
        final AtomicBoolean fired = new AtomicBoolean();
        final AtomicReference<Launchable> firedLaunchable = new AtomicReference<>();
        launcher.addListener(UtilLaunchable.createListener(fired));
        launcher.addListener(UtilLaunchable.createListener(firedLaunchable));

        while (!launcher.fire())
        {
            continue;
        }

        assertTrue(fired.get());
        assertNotNull(firedLaunchable.get());
        firedLaunchable.set(null);

        final Handler handler = services.get(Handler.class);
        handler.update(1.0);

        assertEquals(1, handler.size());

        final Transformable transformable = new TransformableModel();
        while (!launcher.fire(transformable))
        {
            continue;
        }

        assertNotNull(firedLaunchable.get());
        firedLaunchable.set(null);

        handler.update(1.0);

        assertEquals(2, handler.size());

        final Localizable localizable = UtilLaunchable.createLocalizable();
        while (!launcher.fire(localizable))
        {
            continue;
        }

        assertNotNull(firedLaunchable.get());

        handler.update(1.0);

        assertEquals(3, handler.size());

        handler.removeAll();
        handler.update(1.0);

        assertEquals(0, handler.size());
    }

    /**
     * Test the launcher with initial speed.
     * 
     * @throws InterruptedException If error.
     */
    @Test
    public void testLauncherInitial() throws InterruptedException
    {
        final AtomicBoolean fired = new AtomicBoolean();
        final AtomicReference<Launchable> firedLaunchable = new AtomicReference<>();
        launcher.addListener(UtilLaunchable.createListener(fired));
        launcher.addListener(UtilLaunchable.createListener(firedLaunchable));

        final Force force = new Force(1.0, 2.0);
        while (!launcher.fire(force))
        {
            continue;
        }

        assertTrue(fired.get());
        assertNotNull(firedLaunchable.get());

        final Transformable transformable = firedLaunchable.get().getFeature(Transformable.class);

        assertEquals(2.0, transformable.getX());
        assertEquals(4.0, transformable.getY());

        final Handler handler = services.get(Handler.class);
        handler.update(1.0);

        assertEquals(1, handler.size());

        handler.removeAll();
        handler.update(1.0);

        assertEquals(0, handler.size());
    }

    /**
     * Test the launcher with delay.
     * 
     * @throws InterruptedException If error.
     */
    @Test
    public void testLauncherDelay() throws InterruptedException
    {
        final Media launcherMedia = UtilLaunchable.createLauncherMedia(launchableMedia, 10);
        final Setup setup = new Setup(launcherMedia);
        final Launcher launcher = UtilLaunchable.createLauncher(services, setup, featurable);

        final AtomicBoolean fired = new AtomicBoolean();
        final AtomicReference<Launchable> firedLaunchable = new AtomicReference<>();
        launcher.addListener(UtilLaunchable.createListener(fired));
        launcher.addListener(UtilLaunchable.createListener(firedLaunchable));

        while (!launcher.fire())
        {
            continue;
        }

        final Handler handler = services.get(Handler.class);
        launcher.update(1.0);
        handler.update(1.0);

        assertEquals(0, handler.size());
        assertTrue(fired.get());
        assertNull(firedLaunchable.get());

        UtilTests.pause(50L);
        launcher.update(1.0);
        handler.update(1.0);

        assertNotNull(firedLaunchable.get());
        assertEquals(1, handler.size());

        firedLaunchable.set(null);
        handler.removeAll();
        handler.update(1.0);

        assertEquals(0, handler.size());
    }

    /**
     * Test the launcher level.
     * 
     * @throws InterruptedException If error.
     */
    @Test
    public void testLauncherLevel() throws InterruptedException
    {
        final AtomicBoolean fired = new AtomicBoolean();
        final AtomicReference<Launchable> firedLaunchable = new AtomicReference<>();
        launcher.addListener(UtilLaunchable.createListener(fired));
        launcher.addListener(UtilLaunchable.createListener(firedLaunchable));

        while (!launcher.fire())
        {
            continue;
        }

        assertTrue(fired.get());
        assertNotNull(firedLaunchable.get());
        firedLaunchable.set(null);

        final Handler handler = services.get(Handler.class);
        handler.update(1.0);

        assertEquals(1, handler.size());

        launcher.setLevel(1);

        assertNull(firedLaunchable.get());

        while (!launcher.fire())
        {
            continue;
        }
        while (!launcher.fire())
        {
            continue;
        }

        assertNotNull(firedLaunchable.get());

        handler.update(1.0);

        assertEquals(3, handler.size());

        handler.removeAll();
        handler.update(1.0);

        assertEquals(0, handler.size());
    }

    /**
     * Test the launcher with listener itself.
     * 
     * @throws InterruptedException If error.
     */
    @Test
    public void testLauncherSelfListener() throws InterruptedException
    {
        final LaunchableObjectSelf object = new LaunchableObjectSelf();
        final Launcher launcher = UtilLaunchable.createLauncher(services, setup, object);
        launcher.addListener((LauncherListener) object);
        launcher.addListener((LaunchableListener) object);

        assertFalse(object.fired.get());
        assertNull(object.firedLaunchable.get());

        while (!launcher.fire())
        {
            continue;
        }
        final Handler handler = services.get(Handler.class);
        handler.update(1.0);

        assertTrue(object.fired.get());
        assertNotNull(object.firedLaunchable.get());

        handler.removeAll();
        handler.update(1.0);

        assertEquals(0, handler.size());
    }

    /**
     * Test the launcher with listener auto add.
     * 
     * @throws InterruptedException If error.
     */
    @Test
    public void testListenerAutoAdd() throws InterruptedException
    {
        final LaunchableObjectSelf object = new LaunchableObjectSelf();
        final Launcher launcher = UtilLaunchable.createLauncher(services, setup, object);
        launcher.checkListener(object);

        assertFalse(object.fired.get());
        assertNull(object.firedLaunchable.get());

        while (!launcher.fire())
        {
            continue;
        }
        final Handler handler = services.get(Handler.class);
        handler.update(1.0);

        assertTrue(object.fired.get());
        assertNotNull(object.firedLaunchable.get());

        handler.removeAll();
        handler.update(1.0);

        assertEquals(0, handler.size());
    }

    /**
     * Test the launcher failure.
     */
    @Test
    public void testLauncherFailure()
    {
        final Media launchableMedia = UtilSetup.createMedia(Featurable.class);
        final Media launcherMedia = UtilLaunchable.createLauncherMedia(launchableMedia);
        final Setup setup = new Setup(launcherMedia);
        final Launcher launcher = UtilLaunchable.createLauncher(services, setup, featurable);

        try
        {
            assertThrows(() ->
            {
                while (!launcher.fire())
                {
                    continue;
                }
            }, "No recognized constructor found for: Featurable.xml");
        }
        finally
        {
            final Handler handler = services.get(Handler.class);
            handler.removeAll();
            handler.update(1.0);

            assertEquals(0, handler.size());
            assertTrue(launchableMedia.getFile().delete());
        }
    }

    /**
     * Test the launcher failure.
     */
    @Test
    public void testLauncherException()
    {
        final Media launchableMedia = UtilSetup.createMedia(LaunchableObjectException.class);
        final Media launcherMedia = UtilLaunchable.createLauncherMedia(launchableMedia);
        final Setup setup = new Setup(launcherMedia);
        final Launcher launcher = UtilLaunchable.createLauncher(services, setup, featurable);

        try
        {
            assertThrows(() ->
            {
                while (!launcher.fire())
                {
                    continue;
                }
            }, "Feature not found: " + Launchable.class.getName());
        }
        finally
        {
            final Handler handler = services.get(Handler.class);
            handler.removeAll();
            handler.update(1.0);

            assertEquals(0, handler.size());
            assertTrue(launchableMedia.getFile().delete());
        }
    }

    /**
     * Test check listener conditions.
     */
    @Test
    public void testCheckListener()
    {
        final Media launchableMedia = UtilSetup.createMedia(LaunchableObjectException.class);
        final Media launcherMedia = UtilLaunchable.createLauncherMedia(launchableMedia);
        final Setup setup = new Setup(launcherMedia);
        services.add(new Factory(services));
        services.add(new Handler(services));

        final AtomicBoolean launchableListener = new AtomicBoolean();
        final AtomicBoolean launcherListener = new AtomicBoolean();
        final Launcher launcher = new LauncherModel(services, setup)
        {
            @Override
            public void addListener(LaunchableListener listener)
            {
                launchableListener.set(true);
            }

            @Override
            public void addListener(LauncherListener listener)
            {
                launcherListener.set(true);
            }
        };

        assertFalse(launchableListener.get());
        assertFalse(launcherListener.get());

        launcher.checkListener(launcher);

        assertFalse(launchableListener.get());
        assertFalse(launcherListener.get());

        launcher.checkListener((LaunchableListener) l -> l.update(1.0));

        assertTrue(launchableListener.get());
        assertFalse(launcherListener.get());

        launcher.checkListener((LauncherListener) () -> launcher.update(1.0));

        launchableListener.set(false);

        assertFalse(launchableListener.get());
        assertTrue(launcherListener.get());
    }
}

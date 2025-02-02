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
package com.b3dgs.lionengine.game.it.feature.tile.map.pathfinding;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;
import static com.b3dgs.lionengine.UtilAssert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.Context;
import com.b3dgs.lionengine.Engine;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.Tick;
import com.b3dgs.lionengine.awt.Mouse;
import com.b3dgs.lionengine.game.Orientation;
import com.b3dgs.lionengine.game.TextGame;
import com.b3dgs.lionengine.game.feature.Camera;
import com.b3dgs.lionengine.game.feature.ComponentDisplayable;
import com.b3dgs.lionengine.game.feature.ComponentRefreshable;
import com.b3dgs.lionengine.game.feature.Factory;
import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.Handler;
import com.b3dgs.lionengine.game.feature.Identifiable;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGame;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGroupModel;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.MapTilePathModel;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.Pathfindable;
import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.PathfindableListener;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewerModel;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.TextStyle;
import com.b3dgs.lionengine.graphic.engine.Sequence;

/**
 * Game loop designed to handle our little world.
 */
class Scene extends Sequence
{
    private static final Resolution NATIVE = new Resolution(320, 240, 60);

    private final TextGame text = new TextGame(Constant.FONT_SANS_SERIF, 10, TextStyle.NORMAL);
    private final Services services = new Services();
    private final Handler handler = services.create(Handler.class);
    private final Camera camera = services.create(Camera.class);
    private final MapTile map = services.create(MapTileGame.class);
    private final Mouse mouse = getInputDevice(Mouse.class);
    private final Factory factory = services.create(Factory.class);
    private final Tick tick = new Tick();
    private final AtomicBoolean listenerStartMove = new AtomicBoolean();
    private final AtomicBoolean listenerMove = new AtomicBoolean();
    private final AtomicBoolean listenerArrived = new AtomicBoolean();
    private final PathfindableListener listener = new PathfindableListener()
    {
        @Override
        public void notifyStartMove()
        {
            listenerStartMove.set(true);
        }

        @Override
        public void notifyMoving()
        {
            listenerMove.set(true);
        }

        @Override
        public void notifyArrived()
        {
            listenerArrived.set(true);
        }
    };
    private Featurable peon2;
    private boolean changed;

    /**
     * Constructor.
     * 
     * @param context The context reference.
     */
    public Scene(Context context)
    {
        super(context, NATIVE);

        services.add(context);
        handler.addComponent(new ComponentRefreshable());
        handler.addComponent(new ComponentDisplayable());

        setSystemCursorVisible(false);
    }

    @Override
    public void load()
    {
        map.create(Medias.create("level.png"));

        camera.setView(0, 0, getWidth(), getHeight(), getHeight());
        camera.setLocation(160, 96);

        map.create(Medias.create("level.png"));
        map.addFeature(new MapTileViewerModel(services));
        map.addFeatureAndGet(new MapTileGroupModel()).loadGroups(Medias.create("groups.xml"));
        map.addFeatureAndGet(new MapTilePathModel(services)).loadPathfinding(Medias.create("pathfinding.xml"));
        camera.setLimits(map);
        handler.add(map);

        final Featurable peon1 = factory.create(Peon.MEDIA);
        peon1.getFeature(Pathfindable.class).setLocation(20, 16);
        peon1.getFeature(Pathfindable.class).setDestination(23, 12);
        peon1.getFeature(Pathfindable.class).pointTo(1, 2);
        handler.add(peon1);

        peon2 = factory.create(Peon.MEDIA);
        peon2.getFeature(Pathfindable.class).setLocation(22, 8);
        handler.add(peon2);

        final Featurable peon3 = factory.create(Peon.MEDIA);
        peon3.getFeature(Pathfindable.class).setLocation(25, 10);

        assertTrue(peon3.getFeature(Pathfindable.class).isPathAvailable(23, 12));

        peon3.getFeature(Pathfindable.class).setDestination(23, 12);
        handler.add(peon3);

        peon2.getFeature(Pathfindable.class).setIgnoreId(peon3.getFeature(Identifiable.class).getId(), true);
        peon2.getFeature(Pathfindable.class).pointTo(map.getTile(25, 25));

        assertEquals(Orientation.NORTH_EAST, peon2.getFeature(Pathfindable.class).getOrientation());

        peon2.getFeature(Pathfindable.class).setOrientation(Orientation.NORTH_WEST);

        assertEquals(Orientation.NORTH_WEST, peon2.getFeature(Pathfindable.class).getOrientation());

        peon2.getFeature(Pathfindable.class).addListener(listener);

        tick.start();
    }

    @Override
    public void update(double extrp)
    {
        mouse.update(extrp);
        handler.update(extrp);
        text.update(camera);
        tick.update(extrp);
        if (!changed && tick.elapsed(15L))
        {
            peon2.getFeature(Pathfindable.class).setDestination(23, 14);
            changed = true;
        }
        if (tick.elapsed(40L))
        {
            end();
        }

        if (!peon2.getFeature(Pathfindable.class).isMoving())
        {
            assertEquals(0.0, peon2.getFeature(Pathfindable.class).getMoveX());
            assertEquals(0.0, peon2.getFeature(Pathfindable.class).getMoveY());
        }

        assertEquals(6.0, peon2.getFeature(Pathfindable.class).getSpeedX());
        assertEquals(6.0, peon2.getFeature(Pathfindable.class).getSpeedY());
    }

    @Override
    public void render(Graphic g)
    {
        handler.render(g);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        assertTrue(listenerStartMove.get());
        assertTrue(listenerMove.get());
        assertTrue(listenerArrived.get());
        assertTrue(peon2.getFeature(Pathfindable.class).isDestinationReached());

        Engine.terminate();
    }
}

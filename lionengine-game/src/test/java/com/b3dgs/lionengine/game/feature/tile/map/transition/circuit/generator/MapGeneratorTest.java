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
package com.b3dgs.lionengine.game.feature.tile.map.transition.circuit.generator;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;
import static com.b3dgs.lionengine.UtilAssert.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.UtilFile;
import com.b3dgs.lionengine.game.feature.Camera;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.tile.TileRef;
import com.b3dgs.lionengine.game.feature.tile.map.MapTile;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileAppender;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileAppenderModel;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGame;
import com.b3dgs.lionengine.game.feature.tile.map.MapTileGroupModel;
import com.b3dgs.lionengine.game.feature.tile.map.transition.MapTileTransitionModel;
import com.b3dgs.lionengine.game.feature.tile.map.transition.TransitionsConfig;
import com.b3dgs.lionengine.game.feature.tile.map.transition.circuit.MapTileCircuitModel;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewer;
import com.b3dgs.lionengine.game.feature.tile.map.viewer.MapTileViewerModel;
import com.b3dgs.lionengine.graphic.FactoryGraphicMock;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.GraphicMock;
import com.b3dgs.lionengine.graphic.Graphics;

/**
 * Test {@link MapGenerator}.
 */
public final class MapGeneratorTest
{
    private final Services services = new Services();
    private final Camera camera = services.create(Camera.class);
    private final MapTile map = services.create(MapTileGame.class);
    private final MapTileViewer mapViewer = map.addFeatureAndGet(new MapTileViewerModel(services));
    private final MapTileAppender append = map.addFeatureAndGet(new MapTileAppenderModel(services));
    private final GeneratorParameter parameters = new GeneratorParameter();
    private final MapGenerator generator = new MapGeneratorImpl();
    private final Graphic g = new GraphicMock();

    /**
     * Prepare tests.
     */
    @BeforeAll
    public static void beforeTests()
    {
        Graphics.setFactoryGraphic(new FactoryGraphicMock());
        Medias.setLoadFromJar(MapGeneratorTest.class);
    }

    /**
     * Clean up tests.
     */
    @AfterAll
    public static void afterTests()
    {
        Graphics.setFactoryGraphic(null);
        Medias.setLoadFromJar(null);
    }

    /**
     * Prepare test.
     */
    @BeforeEach
    public void prepare()
    {
        map.create(Medias.create("forest.png"));
        mapViewer.prepare(map);

        final Media media = Medias.create("transitions.xml");
        TransitionsConfig.exports(media,
                                  Arrays.asList(Medias.create("forest.png")),
                                  Medias.create("sheets.xml"),
                                  Medias.create("groups.xml"));

        map.addFeatureAndGet(new MapTileGroupModel()).loadGroups(Medias.create("groups.xml"));
        map.addFeatureAndGet(new MapTileTransitionModel(services)).loadTransitions(Medias.create("transitions.xml"));
        map.addFeatureAndGet(new MapTileCircuitModel(services)).loadCircuits(Medias.create("circuits.xml"));

        UtilFile.deleteFile(media.getFile());

        camera.setView(0, 0, 640, 480, 480);
        camera.setLimits(map);

        parameters.add(new PrefMapSize(16, 16, 64, 48))
                  .add(new PrefMapFill(new TileRef(0, 0)))
                  .add(new PrefMapRegion(new TileRef(0, 12), new TileArea(0, 0, 8, 48), 4, 60))
                  .add(new PrefMapRegion(new TileRef(0, 12), new TileArea(56, 0, 64, 48), 4, 60))
                  .add(new PrefMapRegion(new TileRef(0, 12), new TileArea(0, 0, 64, 8), 4, 60))
                  .add(new PrefMapRegion(new TileRef(0, 12), new TileArea(0, 40, 64, 48), 4, 60))
                  .add(new PrefMapRegion(new TileRef(0, 29), new TileArea(12, 12, 56, 42), 2, 250))
                  .add(new PrefMapRegion(new TileRef(0, 12), new TileArea(24, 24, 40, 40), 2, 80))
                  .add(new PrefMapRegion(new TileRef(0, 0), new TileArea(4, 4, 60, 40), 1, 100));
    }

    /**
     * Test the map generator.
     */
    @Test
    public void testGenerator()
    {
        final MapTile generated = generator.generateMap(parameters,
                                                        Arrays.asList(Medias.create("forest.png")),
                                                        Medias.create("sheets.xml"),
                                                        Medias.create("groups.xml"));
        append.append(generated, 0, 0);
        mapViewer.render(g);

        assertEquals(16, map.getTileWidth());
        assertEquals(16, map.getTileHeight());
        assertEquals(64, map.getInTileWidth());
        assertEquals(48, map.getInTileHeight());
        for (int tx = 0; tx < map.getInTileWidth(); tx++)
        {
            for (int ty = 0; ty < map.getInTileHeight(); ty++)
            {
                assertNotNull(map.getTile(tx, ty));
            }
        }
    }
}

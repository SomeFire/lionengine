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
package com.b3dgs.lionengine.game.feature;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Medias;
import com.b3dgs.lionengine.Xml;
import com.b3dgs.lionengine.game.SizeConfig;

/**
 * Setup configuration utility.
 */
public final class UtilSetup
{
    /**
     * Create a test configuration.
     * 
     * @return The test configuration media.
     */
    public static Media createConfig()
    {
        final Xml root = new Xml("test");
        root.add(FeaturableConfig.exportClass("class"));
        root.add(FeaturableConfig.exportSetup("setup"));

        final Media media = Medias.create("object.xml");
        root.save(media);

        return media;
    }

    /**
     * Create the object media.
     * 
     * @param clazz The class type.
     * @return The object media.
     */
    public static Media createMedia(Class<?> clazz)
    {
        final Xml root = new Xml("test");
        root.add(FeaturableConfig.exportClass(clazz.getName()));
        root.add(FeaturableConfig.exportSetup(Setup.class.getName()));
        root.add(SizeConfig.exports(new SizeConfig(16, 32)));

        final Media media = Medias.create(clazz.getSimpleName() + ".xml");
        root.save(media);

        return media;
    }
}

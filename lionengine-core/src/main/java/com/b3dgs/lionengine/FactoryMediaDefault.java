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
package com.b3dgs.lionengine;

/**
 * Default media factory implementation.
 * <p>
 * This class is Thread-Safe.
 * </p>
 */
public class FactoryMediaDefault implements FactoryMedia
{
    /**
     * Create factory.
     */
    public FactoryMediaDefault()
    {
        super();
    }

    /*
     * FactoryMedia
     */

    @Override
    public Media create(String separator, String resourcesDir, String... path)
    {
        return new MediaDefault(separator, resourcesDir, UtilFolder.getPathSeparator(separator, path));
    }

    @Override
    public Media create(String separator, Class<?> loader, String... path)
    {
        return new MediaDefault(separator, loader, UtilFolder.getPathSeparator(separator, path));
    }
}

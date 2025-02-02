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
package com.b3dgs.lionengine.game.feature.collidable.framed;

import java.util.Collection;
import java.util.Collections;

import com.b3dgs.lionengine.AnimatorFrameListener;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.Configurer;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.feature.Animatable;
import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.collidable.Collidable;
import com.b3dgs.lionengine.game.feature.collidable.Collision;

/**
 * Collidable framed model implementation.
 */
public class CollidableFramedModel extends FeatureModel implements CollidableFramed
{
    /** Loaded collisions framed. */
    private final CollidableFramedConfig config;

    /** Last collision found. */
    private Collection<Collision> last = Collections.emptyList();

    /**
     * Create feature.
     * <p>
     * The {@link Featurable} must have:
     * </p>
     * <ul>
     * <li>{@link Collidable}</li>
     * <li>{@link Animatable}</li>
     * </ul>
     * <p>
     * The {@link Configurer} can provide a valid {@link CollidableFramedConfig}.
     * </p>
     * 
     * @param configurer The configurer reference (must not be <code>null</code>).
     * @throws LionEngineException If invalid arguments.
     */
    public CollidableFramedModel(Configurer configurer)
    {
        super();

        config = CollidableFramedConfig.imports(configurer);
    }

    /*
     * CollidableFramed
     */

    @Override
    public void prepare(FeatureProvider provider)
    {
        super.prepare(provider);

        final Collidable collidable = provider.getFeature(Collidable.class);
        final Animatable animatable = provider.getFeature(Animatable.class);

        for (final Collision collision : config.getCollisions())
        {
            collidable.addCollision(collision);
            collidable.setEnabled(false, collision);
        }

        animatable.addListener((AnimatorFrameListener) frame ->
        {
            for (final Collision collision : last)
            {
                collidable.setEnabled(false, collision);
            }
            last = config.getCollision(Integer.valueOf(frame));
            for (final Collision collision : last)
            {
                collidable.setEnabled(true, collision);
                collidable.forceUpdate();
            }
        });
    }
}

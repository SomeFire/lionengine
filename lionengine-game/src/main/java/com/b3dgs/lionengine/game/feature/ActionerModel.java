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

import java.util.List;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.Configurer;

/**
 * Actioner model implementation.
 */
@FeatureInterface
public class ActionerModel extends FeatureModel implements Actioner
{
    /** Allowed actions name. */
    private final List<ActionRef> actions;

    /**
     * Create feature.
     * <p>
     * The {@link Configurer} must provide a valid {@link ActionsConfig}.
     * </p>
     * 
     * @param configurer The configurer reference (must not be <code>null</code>).
     * @throws LionEngineException If invalid argument.
     */
    public ActionerModel(Configurer configurer)
    {
        super();

        actions = ActionsConfig.imports(configurer, this::getFeature);
    }

    /*
     * Actioner
     */

    @Override
    public List<ActionRef> getActions()
    {
        return actions;
    }
}

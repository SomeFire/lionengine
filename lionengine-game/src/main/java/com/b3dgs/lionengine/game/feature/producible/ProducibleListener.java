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
package com.b3dgs.lionengine.game.feature.producible;

/**
 * List of event linked to the producible production progress.
 */
public interface ProducibleListener
{
    /**
     * Notify listener that production has been started.
     * 
     * @param producer The producer reference.
     */
    void notifyProductionStarted(Producer producer);

    /**
     * Notify listener that production is currently in progress.
     * 
     * @param producer The producer reference.
     */
    void notifyProductionProgress(Producer producer);

    /**
     * Notify listener that production is over.
     * 
     * @param producer The producer reference.
     */
    void notifyProductionEnded(Producer producer);
}

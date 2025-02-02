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

import com.b3dgs.lionengine.ListenableModel;
import com.b3dgs.lionengine.game.Configurer;

/**
 * Layerable model implementation. 0 has higher priority, 1 less...
 */
public class LayerableModel extends FeatureModel implements Layerable
{
    /** Layers listener. */
    private final ListenableModel<LayerableListener> listenable = new ListenableModel<>();
    /** Layer refresh value. */
    private Integer layerRefresh = Integer.valueOf(0);
    /** Layer display value. */
    private Integer layerDisplay = layerRefresh;

    /**
     * Create feature.
     */
    public LayerableModel()
    {
        super();
    }

    /**
     * Create feature.
     * <p>
     * The {@link Services} must provide:
     * </p>
     * <ul>
     * <li>{@link LayerableListener}</li>
     * </ul>
     * 
     * @param services The services reference.
     */
    public LayerableModel(Services services)
    {
        super();

        listenable.addListener(services.get(LayerableListener.class));
    }

    /**
     * Create feature.
     * <p>
     * The {@link Services} must provide:
     * </p>
     * <ul>
     * <li>{@link LayerableListener}</li>
     * </ul>
     * <p>
     * The {@link Configurer} can provide a valid {@link LayerableConfig}.
     * </p>
     * 
     * @param services The services reference.
     * @param configurer The configurer reference.
     */
    public LayerableModel(Services services, Configurer configurer)
    {
        this(services);

        if (configurer.hasNode(LayerableConfig.NODE_LAYERABLE))
        {
            final LayerableConfig config = LayerableConfig.imports(configurer);
            layerRefresh = Integer.valueOf(config.getLayerRefresh());
            layerDisplay = Integer.valueOf(config.getLayerDisplay());
        }
    }

    /**
     * Create feature.
     * 
     * @param layer The default layer refresh and display value.
     */
    public LayerableModel(int layer)
    {
        this(layer, layer);
    }

    /**
     * Create feature.
     * 
     * @param layerRefresh The default layer refresh value.
     * @param layerDisplay The default layer display value.
     */
    public LayerableModel(int layerRefresh, int layerDisplay)
    {
        super();

        this.layerRefresh = Integer.valueOf(layerRefresh);
        this.layerDisplay = Integer.valueOf(layerDisplay);
    }

    /*
     * Layerable
     */

    @Override
    public void addListener(LayerableListener listener)
    {
        listenable.addListener(listener);
    }

    @Override
    public void removeListener(LayerableListener listener)
    {
        listenable.removeListener(listener);
    }

    @Override
    public void setLayer(Integer layerRefresh, Integer layerDisplay)
    {
        for (int i = 0; i < listenable.size(); i++)
        {
            listenable.get(i)
                      .notifyLayerChanged(this, this.layerRefresh, layerRefresh, this.layerDisplay, layerDisplay);
        }
        this.layerRefresh = layerRefresh;
        this.layerDisplay = layerDisplay;
    }

    @Override
    public Integer getLayerRefresh()
    {
        return layerRefresh;
    }

    @Override
    public Integer getLayerDisplay()
    {
        return layerDisplay;
    }
}

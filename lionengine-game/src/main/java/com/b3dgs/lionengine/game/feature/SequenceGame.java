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

import com.b3dgs.lionengine.Config;
import com.b3dgs.lionengine.Context;
import com.b3dgs.lionengine.Engine;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.engine.Loop;
import com.b3dgs.lionengine.graphic.engine.LoopFrameSkipping;
import com.b3dgs.lionengine.graphic.engine.Sequencable;
import com.b3dgs.lionengine.graphic.engine.Sequence;
import com.b3dgs.lionengine.graphic.engine.Sequencer;
import com.b3dgs.lionengine.graphic.engine.SourceResolutionProvider;
import com.b3dgs.lionengine.graphic.engine.TimeControl;
import com.b3dgs.lionengine.graphic.engine.Zooming;

/**
 * Sequence base dedicated to game module, supporting base tools by default.
 * <p>
 * The following tools are included:
 * </p>
 * <ul>
 * <li>{@link Services}: providing {@link Context}, {@link Zooming}, {@link TimeControl},
 * {@link SourceResolutionProvider}, {@link Sequencer} to control sequence (available after {@link #load()}).</li>
 * <li>{@link WorldGame}: added to {@link Services}, {@link #update(double)} and {@link #render(Graphic)} are already
 * called.</li>
 * <li>{@link #setSystemCursorVisible(boolean)}: set to <code>false</code>.</li>
 * </ul>
 */
public abstract class SequenceGame extends Sequence
{
    /** Services instance. */
    protected final Services services = new Services();
    /** Handled world. */
    protected final WorldGame world;

    /**
     * Create sequence. Resolution will be based on {@link Config#getOutput()}.
     * 
     * @param context The context reference.
     * @param creator The world creator reference.
     * @throws LionEngineException If invalid arguments.
     */
    protected SequenceGame(Context context, WorldCreator creator)
    {
        this(context, context.getConfig().getOutput(), creator);
    }

    /**
     * Create sequence.
     * 
     * @param context The context reference.
     * @param resolution The resolution source reference.
     * @param creator The world creator reference.
     * @throws LionEngineException If invalid arguments.
     */
    protected SequenceGame(Context context, Resolution resolution, WorldCreator creator)
    {
        this(context, resolution, new LoopFrameSkipping(), creator);
    }

    /**
     * Create sequence.
     * 
     * @param context The context reference.
     * @param resolution The resolution source reference.
     * @param loop The loop used (must not be <code>null</code>).
     * @param creator The world creator reference.
     * @throws LionEngineException If invalid arguments.
     */
    protected SequenceGame(Context context, Resolution resolution, Loop loop, WorldCreator creator)
    {
        super(context, resolution, loop);

        services.add(context);
        services.add(new Sequencer()
        {
            @Override
            public void end()
            {
                SequenceGame.this.end();
            }

            @Override
            public void end(Class<? extends Sequencable> nextSequenceClass, Object... arguments)
            {
                SequenceGame.this.end(nextSequenceClass, arguments);
            }
        });
        services.add((Zooming) this::setZoom);
        services.add((TimeControl) this::setTime);
        services.add(new SourceResolutionProvider()
        {
            @Override
            public int getWidth()
            {
                return SequenceGame.this.getWidth();
            }

            @Override
            public int getHeight()
            {
                return SequenceGame.this.getHeight();
            }

            @Override
            public int getRate()
            {
                return SequenceGame.this.getRate();
            }
        });

        world = services.add(creator.createWorld(services));

        setSystemCursorVisible(false);
    }

    /*
     * Sequence
     */

    @Override
    public void update(double extrp)
    {
        world.update(extrp);
    }

    @Override
    public void render(Graphic g)
    {
        world.render(g);
    }

    /**
     * Called when the resolution changed. Update world resolution.
     * 
     * @param width The new screen width.
     * @param height The new screen height.
     */
    @Override
    protected void onResolutionChanged(int width, int height)
    {
        world.onResolutionChanged(width, height);
    }

    /**
     * Called when the rate changed. Update world rate.
     * 
     * @param rate The new screen rate.
     */
    @Override
    protected void onRateChanged(int rate)
    {
        world.onRateChanged(rate);
    }

    /**
     * Called when sequence is closing.
     * {@link Engine#terminate()} called if does not have next sequence.
     * 
     * @param hasNextSequence <code>true</code> if there is a next sequence, <code>false</code> else (then application
     *            will end definitely).
     */
    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        if (!hasNextSequence)
        {
            Engine.terminate();
        }
    }

    /**
     * World factory interface.
     */
    protected interface WorldCreator
    {
        /**
         * Create the world.
         * 
         * @param services The services reference.
         * @return The created world.
         */
        WorldGame createWorld(Services services);
    }
}

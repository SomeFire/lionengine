/*
 * Copyright (C) 2013 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.game.projectile;

import com.b3dgs.lionengine.Timing;
import com.b3dgs.lionengine.core.Graphic;
import com.b3dgs.lionengine.core.Transparency;
import com.b3dgs.lionengine.core.Verbose;
import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.drawable.SpriteTiled;
import com.b3dgs.lionengine.game.CameraGame;
import com.b3dgs.lionengine.game.Damages;
import com.b3dgs.lionengine.game.SetupSurfaceGame;
import com.b3dgs.lionengine.game.Surface;
import com.b3dgs.lionengine.game.entity.EntityGame;
import com.b3dgs.lionengine.utility.UtilityImage;

/**
 * Standard projectile implementation, including collision and moves handling.
 * 
 * @param <E> The entity type used.
 * @param <E2> The source type used.
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public abstract class ProjectileGame<E extends EntityGame, E2 extends Surface>
        extends EntityGame
{
    /** Projectile data. */
    public final int frame;
    /** Projectile id. */
    public final int id;
    /** Damages. */
    public final Damages damages;
    /** Surface reference. */
    private final SpriteTiled sprite;
    /** Horizontal offset. */
    private final int offsetX;
    /** Vertical offset. */
    private final int offsetY;
    /** Delay before being added in the handler. */
    private final Timing delay;
    /** Horizontal vector. */
    private double vecX;
    /** Vertical vector. */
    private double vecY;
    /** Target. */
    private E target;
    /** Entity owner. */
    private E2 owner;
    /** Hit only target. */
    private boolean hitTargetOnly;
    /** Elapsed time. */
    private long time;

    /**
     * Create a new projectile. The projectile should contain at least theses data in the configuration file:
     * 
     * <pre>
     * {@code
     * <entity surface="projectile.png">
     *     <size width="10" height="10"/>
     *     <offset x="0" y="0"/>
     * </entity>
     * 
     * }
     * </pre>
     * 
     * @param setup The entity setup.
     * @param id The projectile id (when a projectile is destroyed, all projectiles with this id are also destroyed).
     *            Can be -1 to ignore it.
     * @param frame The projectile tile number (from surface).
     */
    public ProjectileGame(SetupSurfaceGame setup, int id, int frame)
    {
        super(setup);
        damages = new Damages();
        final int width = setup.configurable.getDataInteger("width", "size");
        final int height = setup.configurable.getDataInteger("height", "size");
        offsetX = getDataInteger("x", "offset");
        offsetY = getDataInteger("y", "offset");
        setSize(width, height);

        if (setup.surface == null || width == 0 || height == 0)
        {
            sprite = Drawable.loadSpriteTiled(UtilityImage.createImageBuffer(1, 1, Transparency.OPAQUE), 1, 1);
            Verbose.critical(ProjectileGame.class, "constructor", "Missing surface file: ", setup.surfaceFile.getPath());
        }
        else
        {
            sprite = Drawable.loadSpriteTiled(setup.surface, width, height);
        }
        this.frame = frame;
        this.id = id;
        owner = null;
        delay = new Timing();
    }

    /**
     * Action called when projectile hit an entity.
     * 
     * @param entity The entity hit.
     * @param damages The damages.
     */
    public abstract void onHit(E entity, int damages);

    /**
     * Define the projectile movement.
     * 
     * @param extrp The extrapolation value.
     * @param vecX The horizontal vector.
     * @param vecY The vertical vector.
     */
    protected abstract void updateMovement(double extrp, double vecX, double vecY);

    /**
     * Start projectile handling.
     * 
     * @param x The horizontal location.
     * @param y The vertical location.
     * @param vecX The horizontal vector.
     * @param vecY The vertical vector.
     */
    public void start(int x, int y, double vecX, double vecY)
    {
        setLocation(x + getWidth() / 2, y + getHeight() / 2);
        this.vecX = vecX;
        this.vecY = vecY;
    }

    /**
     * Rendering routine.
     * 
     * @param g graphics output.
     * @param camera camera reference.
     */
    public void render(Graphic g, CameraGame camera)
    {
        final int x = camera.getViewpointX(getLocationIntX() - offsetX);
        final int y = camera.getViewpointY(getLocationIntY() + offsetY);
        sprite.render(g, frame, x, y);
    }

    /**
     * Set the projectile target.
     * 
     * @param target The entity target.
     */
    public void setTarget(E target)
    {
        this.target = target;
    }

    /**
     * Set the hit target properties.
     * 
     * @param hitTargetOnly <code>true</code> to make the projectile hit only the target, <code>false</code> to allows
     *            other hits.
     */
    public void setCanHitTargetOnly(boolean hitTargetOnly)
    {
        this.hitTargetOnly = hitTargetOnly;
    }

    /**
     * Set the projectile owner.
     * 
     * @param owner The entity owner.
     */
    public void setOwner(E2 owner)
    {
        this.owner = owner;
    }

    /**
     * Set projectile delay (time before being added in the handler).
     * 
     * @param delay The delay time.
     */
    public void setDelay(long delay)
    {
        time = delay;
        this.delay.start();
    }

    /**
     * Get the owner.
     * 
     * @return The owner.
     */
    public E2 getOwner()
    {
        return owner;
    }

    /**
     * Get the target.
     * 
     * @return The target.
     */
    public E getTarget()
    {
        return target;
    }

    /**
     * Get the hit target only state.
     * 
     * @return <code>true</code> if can hit only the target, <code>false</code> else.
     */
    public boolean canHitOnlyTarget()
    {
        return hitTargetOnly;
    }

    /**
     * Check if projectile can be added in the handler (related to delay).
     * 
     * @return <code>true</code> if can be added, <code>false</code> else.
     */
    public boolean canBeAdded()
    {
        return delay.elapsed(time);
    }

    /*
     * EntityGame
     */

    @Override
    public void update(double extrp)
    {
        updateMovement(extrp, vecX, vecY);
        updateCollision();
    }
}

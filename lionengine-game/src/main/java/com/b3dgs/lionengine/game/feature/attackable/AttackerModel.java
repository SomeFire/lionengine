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
package com.b3dgs.lionengine.game.feature.attackable;

import java.util.function.BooleanSupplier;

import com.b3dgs.lionengine.AnimState;
import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.ListenableModel;
import com.b3dgs.lionengine.Range;
import com.b3dgs.lionengine.Tick;
import com.b3dgs.lionengine.UtilMath;
import com.b3dgs.lionengine.game.Configurer;
import com.b3dgs.lionengine.game.Damages;
import com.b3dgs.lionengine.game.FeatureProvider;
import com.b3dgs.lionengine.game.feature.Animatable;
import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.FeatureModel;
import com.b3dgs.lionengine.game.feature.Recyclable;
import com.b3dgs.lionengine.game.feature.Transformable;

/**
 * Attacker model implementation.
 */
public class AttackerModel extends FeatureModel implements Attacker, Recyclable
{
    /** Listeners list. */
    private final ListenableModel<AttackerListener> listenable = new ListenableModel<>();
    /** Attack tick delay. */
    private final Tick tick = new Tick();
    /** Attack damages. */
    private final Damages damages = new Damages();
    /** Attack distance allowed. */
    private Range distAttack = new Range(1, 1);
    /** Animatable reference. */
    private Animatable animatable;
    /** Transformable reference. */
    private Transformable transformable;
    /** Attacker checker. */
    private BooleanSupplier canAttack = Boolean.TRUE::booleanValue;
    /** Attacker target (can be <code>null</code>). */
    private Transformable target;
    /** Attack frame number. */
    private int frameAttack;
    /** Attack pause time in tick. */
    private int attackPause;
    /** Attack state. */
    private AttackState state;
    /** True if stop attack is requested. */
    private boolean stop;
    /** Currently attacking flag. */
    private boolean attacking;
    /** Attacked flag. */
    private boolean attacked;

    /**
     * Create feature.
     * <p>
     * The {@link Featurable} must have:
     * </p>
     * <ul>
     * <li>{@link Animatable}</li>
     * <li>{@link Transformable}</li>
     * </ul>
     */
    public AttackerModel()
    {
        super();
    }

    /**
     * Create feature.
     * <p>
     * The {@link Featurable} must have:
     * </p>
     * <ul>
     * <li>{@link Animatable}</li>
     * <li>{@link Transformable}</li>
     * </ul>
     * <p>
     * The {@link Configurer} can provide a valid {@link AttackerConfig}.
     * </p>
     * 
     * @param configurer The configurer reference.
     */
    public AttackerModel(Configurer configurer)
    {
        super();

        Check.notNull(configurer);

        if (configurer.hasNode(AttackerConfig.NODE_ATTACKER))
        {
            final AttackerConfig config = AttackerConfig.imports(configurer);
            damages.setDamages(config.getDamages());
            distAttack = config.getDistance();
            attackPause = config.getDelay();
            tick.set(attackPause);
        }
    }

    /**
     * Update the attack check case.
     */
    private void updateAttackCheck()
    {
        attacking = false;
        attacked = false;
        // Check if target is valid; exit if invalid
        if (target == null)
        {
            state = AttackState.NONE;
        }
        else
        {
            final double dist = UtilMath.getDistance(transformable.getX(),
                                                     transformable.getY(),
                                                     transformable.getWidth(),
                                                     transformable.getHeight(),
                                                     target.getX(),
                                                     target.getY(),
                                                     target.getWidth(),
                                                     target.getHeight());
            checkTargetDistance(dist);
        }
    }

    /**
     * Check the target distance and update the attack state.
     * 
     * @param dist The target distance.
     */
    private void checkTargetDistance(double dist)
    {
        if (distAttack.includes(dist))
        {
            if (canAttack.getAsBoolean())
            {
                state = AttackState.ATTACKING;
            }
        }
        else if (tick.elapsed(attackPause))
        {
            for (int i = 0; i < listenable.size(); i++)
            {
                listenable.get(i).notifyReachingTarget(target);
            }
        }
    }

    /**
     * Update the attacking case.
     */
    private void updateAttacking()
    {
        if (!attacked && tick.elapsed(attackPause))
        {
            updateAttackHit();
        }
        else if (attacked)
        {
            if (AnimState.FINISHED == animatable.getAnimState())
            {
                for (int i = 0; i < listenable.size(); i++)
                {
                    listenable.get(i).notifyAttackAnimEnded();
                }
                attacked = false;
                state = AttackState.CHECK;
            }
        }
        else
        {
            for (int i = 0; i < listenable.size(); i++)
            {
                listenable.get(i).notifyPreparingAttack();
            }
        }
    }

    /**
     * Update the attack state and hit when possible.
     */
    private void updateAttackHit()
    {
        if (!attacking)
        {
            for (int i = 0; i < listenable.size(); i++)
            {
                listenable.get(i).notifyAttackStarted(target);
            }
            attacking = true;
            attacked = false;
        }
        // Hit when frame attack reached

        else if (animatable.getFrame() == frameAttack)
        {
            attacking = false;
            for (int i = 0; i < listenable.size(); i++)
            {
                listenable.get(i).notifyAttackEnded(damages.getRandom(), target);
            }
            attacked = true;
            tick.restart();
        }
    }

    /*
     * Attacker
     */

    @Override
    public void prepare(FeatureProvider provider)
    {
        super.prepare(provider);

        animatable = provider.getFeature(Animatable.class);
        transformable = provider.getFeature(Transformable.class);

        if (provider instanceof AttackerListener)
        {
            addListener((AttackerListener) provider);
        }
    }

    @Override
    public void checkListener(Object listener)
    {
        super.checkListener(listener);

        if (listener instanceof AttackerListener)
        {
            addListener((AttackerListener) listener);
        }
    }

    @Override
    public void addListener(AttackerListener listener)
    {
        listenable.addListener(listener);
    }

    @Override
    public void removeListener(AttackerListener listener)
    {
        listenable.removeListener(listener);
    }

    @Override
    public void attack(Transformable target)
    {
        final boolean targetExist = target != null;
        final boolean targetDifferent = target != this.target;
        final boolean isNotAttacking = AttackState.NONE == state || stop;

        if (targetExist && targetDifferent || isNotAttacking)
        {
            this.target = target;
            state = AttackState.CHECK;
            attacking = false;
            stop = false;
            tick.start();
        }
    }

    @Override
    public void stopAttack()
    {
        stop = true;
    }

    @Override
    public void update(double extrp)
    {
        tick.update(extrp);
        switch (state)
        {
            case NONE:
                // Nothing to do
                break;
            case CHECK:
                updateAttackCheck();
                break;
            case ATTACKING:
                updateAttacking();
                break;
            default:
                throw new LionEngineException(state);
        }
        if (stop)
        {
            attacking = false;
            state = AttackState.NONE;
            stop = false;
        }
    }

    @Override
    public void setAttackChecker(BooleanSupplier checker)
    {
        Check.notNull(checker);

        canAttack = checker;
    }

    @Override
    public void setAttackDelay(int tick)
    {
        Check.superiorOrEqual(tick, 0);

        attackPause = tick;
        this.tick.set(tick);
    }

    @Override
    public void setAttackFrame(int frame)
    {
        Check.superiorStrict(frame, 0);

        frameAttack = frame;
    }

    @Override
    public void setAttackDistance(Range range)
    {
        Check.notNull(range);

        distAttack = range;
    }

    @Override
    public void setAttackDamages(Range range)
    {
        Check.notNull(range);

        damages.setDamages(range.getMin(), range.getMax());
    }

    @Override
    public int getAttackDamages()
    {
        return damages.getRandom();
    }

    @Override
    public boolean isAttacking()
    {
        return AttackState.ATTACKING == state;
    }

    @Override
    public Transformable getTarget()
    {
        return target;
    }

    /*
     * Recyclable
     */

    @Override
    public void recycle()
    {
        attacking = false;
        attacked = false;
        stop = false;
        target = null;
        state = AttackState.NONE;
    }
}

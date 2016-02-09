/*
 * Copyright (C) 2013-2016 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.object.trait.actionable;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.Configurer;
import com.b3dgs.lionengine.stream.XmlNode;

/**
 * Represents the action data from a configurer.
 * 
 * @see com.b3dgs.lionengine.game.object.trait.actionable.Actionable
 */
public final class ActionConfig
{
    /** Action node name. */
    public static final String ACTION = Configurer.PREFIX + "action";
    /** Action attribute name. */
    public static final String NAME = "name";
    /** Action attribute description. */
    public static final String DESCRIPTION = "description";
    /** Action attribute x. */
    public static final String X = "x";
    /** Action attribute y. */
    public static final String Y = "y";
    /** Action attribute width. */
    public static final String WIDTH = "width";
    /** Action attribute height. */
    public static final String HEIGHT = "height";
    /** Error parsing integer. */
    private static final String ERROR_PARSING_INT = "Error on parsing integer value: ";

    /**
     * Create the action data from node.
     *
     * @param configurer The configurer reference.
     * @return The action data.
     * @throws LionEngineException If unable to read node.
     */
    public static ActionConfig create(Configurer configurer)
    {
        final XmlNode node = configurer.getRoot();

        final String name = node.getChild(NAME).getText();
        final String description = node.getChild(DESCRIPTION).getText();
        final int x = getTextInt(node, X);
        final int y = getTextInt(node, Y);
        final int width = getTextInt(node, WIDTH);
        final int height = getTextInt(node, HEIGHT);

        return new ActionConfig(name, description, x, y, width, height);
    }

    /**
     * Get text content as integer.
     * 
     * @param node The main node.
     * @param child The child node containing the text.
     * @return The integer value.
     * @throws LionEngineException If error on parsing value.
     */
    private static int getTextInt(XmlNode node, String child)
    {
        final String text = node.getChild(child).getText();
        try
        {
            return Integer.parseInt(text);
        }
        catch (final NumberFormatException exception)
        {
            throw new LionEngineException(exception, ERROR_PARSING_INT, text);
        }
    }

    /** Action name. */
    private final String name;
    /** Action description. */
    private final String description;
    /** Horizontal location on screen. */
    private final int x;
    /** Vertical location on screen. */
    private final int y;
    /** Width on screen. */
    private final int width;
    /** Height on screen. */
    private final int height;

    /**
     * Disabled constructor.
     */
    private ActionConfig()
    {
        throw new LionEngineException(LionEngineException.ERROR_PRIVATE_CONSTRUCTOR);
    }

    /**
     * Create action from configuration media.
     *
     * @param name The action name.
     * @param description The action description.
     * @param x The horizontal location on screen.
     * @param y The vertical location on screen.
     * @param width The button width.
     * @param height The button height.
     */
    private ActionConfig(String name, String description, int x, int y, int width, int height)
    {
        this.name = name;
        this.description = description;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Get the action name.
     *
     * @return The action name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the action description.
     *
     * @return The action description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Get the button horizontal location.
     *
     * @return The button horizontal location.
     */
    public int getX()
    {
        return x;
    }

    /**
     * Get the button vertical location.
     *
     * @return The button vertical location.
     */
    public int getY()
    {
        return y;
    }

    /**
     * Get the button width.
     *
     * @return The button width.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Get the button height.
     *
     * @return The button height.
     */
    public int getHeight()
    {
        return height;
    }
}
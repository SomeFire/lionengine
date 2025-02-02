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
package com.b3dgs.lionengine.graphic;

import com.b3dgs.lionengine.Config;
import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;

/**
 * Implementation provider for the {@link FactoryGraphic}.
 * <p>
 * This class is Thread-Safe.
 * </p>
 */
public final class Graphics
{
    /** Factory graphic implementation. */
    private static volatile FactoryGraphic factoryGraphic;

    /**
     * Set the graphic factory used.
     * 
     * @param factoryGraphic The graphic factory used.
     */
    public static void setFactoryGraphic(FactoryGraphic factoryGraphic)
    {
        Graphics.factoryGraphic = factoryGraphic;
    }

    /**
     * Create a screen.
     * 
     * @param config The config reference (must not be <code>null</code>).
     * @return The screen instance.
     * @throws LionEngineException If invalid argument.
     */
    public static Screen createScreen(Config config)
    {
        return factoryGraphic.createScreen(config);
    }

    /**
     * Create a graphic context.
     * 
     * @return The graphic context.
     */
    public static Graphic createGraphic()
    {
        return factoryGraphic.createGraphic();
    }

    /**
     * Create a transform.
     * 
     * @return The created transform.
     */
    public static Transform createTransform()
    {
        return factoryGraphic.createTransform();
    }

    /**
     * Crate a text with {@link Constant#FONT_SANS_SERIF} and {@link TextStyle#NORMAL}.
     * 
     * @param size The font size in pixel (must be strictly positive).
     * @return The created text.
     * @throws LionEngineException If invalid arguments.
     */
    public static Text createText(int size)
    {
        return factoryGraphic.createText(Constant.FONT_SANS_SERIF, size, TextStyle.NORMAL);
    }

    /**
     * Crate a text.
     * 
     * @param fontName The font name (must not be <code>null</code>).
     * @param size The font size in pixel (must be strictly positive).
     * @param style The font style (must not be <code>null</code>).
     * @return The created text.
     * @throws LionEngineException If invalid arguments.
     */
    public static Text createText(String fontName, int size, TextStyle style)
    {
        return factoryGraphic.createText(fontName, size, style);
    }

    /**
     * Create an image buffer.
     * 
     * @param width The image width (must be strictly positive).
     * @param height The image height (must be strictly positive).
     * @return The image buffer.
     * @throws LionEngineException If invalid arguments.
     */
    public static ImageBuffer createImageBuffer(int width, int height)
    {
        return factoryGraphic.createImageBuffer(width, height);
    }

    /**
     * Create an image buffer.
     * 
     * @param width The image width (must be strictly positive).
     * @param height The image height (must be strictly positive).
     * @param transparency The color transparency (must not be <code>null</code>).
     * @return The image buffer.
     * @throws LionEngineException If invalid arguments.
     */
    public static ImageBuffer createImageBuffer(int width, int height, ColorRgba transparency)
    {
        return factoryGraphic.createImageBuffer(width, height, transparency);
    }

    /**
     * Get an image buffer from an image file.
     * 
     * @param media The image media (must not be <code>null</code>).
     * @return The created image buffer from file.
     * @throws LionEngineException If an error occurred when reading the image.
     */
    public static ImageBuffer getImageBuffer(Media media)
    {
        return factoryGraphic.getImageBuffer(media);
    }

    /**
     * Get an image buffer from an image buffer.
     * 
     * @param imageBuffer The image buffer (must not be <code>null</code>).
     * @return The created image buffer from file.
     * @throws LionEngineException If invalid argument.
     */
    public static ImageBuffer getImageBuffer(ImageBuffer imageBuffer)
    {
        return factoryGraphic.getImageBuffer(imageBuffer);
    }

    /**
     * Apply color mask to the image.
     * 
     * @param imageBuffer The image reference (must not be <code>null</code>).
     * @param maskColor The color mask (must not be <code>null</code>).
     * @return The masked image buffer.
     * @throws LionEngineException If invalid arguments.
     */
    public static ImageBuffer applyMask(ImageBuffer imageBuffer, ColorRgba maskColor)
    {
        return factoryGraphic.applyMask(imageBuffer, maskColor);
    }

    /**
     * Split an image into an array of sub image.
     * 
     * @param image The image to split (must not be <code>null</code>).
     * @param h The number of horizontal divisions (must be strictly positive).
     * @param v The number of vertical divisions (must be strictly positive).
     * @return The splited images array (can not be empty).
     * @throws LionEngineException If invalid arguments.
     */
    public static ImageBuffer[] splitImage(ImageBuffer image, int h, int v)
    {
        return factoryGraphic.splitImage(image, h, v);
    }

    /**
     * Rotate input image buffer.
     * 
     * @param image The input image buffer (must not be <code>null</code>).
     * @param angle The angle to apply in degree (0-359)
     * @return The new image buffer with angle applied.
     * @throws LionEngineException If invalid arguments.
     */
    public static ImageBuffer rotate(ImageBuffer image, int angle)
    {
        return factoryGraphic.rotate(image, angle);
    }

    /**
     * Resize input image buffer.
     * 
     * @param image The input image buffer (must not be <code>null</code>).
     * @param width The new width (must be strictly positive).
     * @param height The new height (must be strictly positive).
     * @return The new image buffer with new size.
     * @throws LionEngineException If invalid arguments.
     */
    public static ImageBuffer resize(ImageBuffer image, int width, int height)
    {
        return factoryGraphic.resize(image, width, height);
    }

    /**
     * Apply an horizontal flip to the input image.
     * 
     * @param image The input image buffer (must not be <code>null</code>).
     * @return The flipped image buffer as a new instance.
     * @throws LionEngineException If invalid argument.
     */
    public static ImageBuffer flipHorizontal(ImageBuffer image)
    {
        return factoryGraphic.flipHorizontal(image);
    }

    /**
     * Apply a vertical flip to the input image.
     * 
     * @param image The input image buffer (must not be <code>null</code>).
     * @return The flipped image buffer as a new instance.
     * @throws LionEngineException If invalid argument.
     */
    public static ImageBuffer flipVertical(ImageBuffer image)
    {
        return factoryGraphic.flipVertical(image);
    }

    /**
     * Save an image into a file.
     * 
     * @param image The image to save (must not be <code>null</code>).
     * @param media The output media (must not be <code>null</code>).
     * @throws LionEngineException If an error occurred when saving the image.
     */
    public static void saveImage(ImageBuffer image, Media media)
    {
        factoryGraphic.saveImage(image, media);
    }

    /**
     * Get raster buffer from data.
     * 
     * @param img The image buffer (must not be <code>null</code>).
     * @param fr The first red.
     * @param fg The first green.
     * @param fb The first blue.
     * @return The rastered image.
     * @throws LionEngineException If invalid arguments.
     */
    public static ImageBuffer getRasterBuffer(ImageBuffer img, double fr, double fg, double fb)
    {
        return factoryGraphic.getRasterBuffer(img, fr, fg, fb);
    }

    /**
     * Private constructor.
     */
    private Graphics()
    {
        throw new LionEngineException(LionEngineException.ERROR_PRIVATE_CONSTRUCTOR);
    }
}

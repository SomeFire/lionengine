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
package com.b3dgs.lionengine.headless.graphic;

import java.io.IOException;
import java.io.OutputStream;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Config;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.UtilConversion;
import com.b3dgs.lionengine.geom.Rectangle;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.FactoryGraphic;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.ImageBuffer;
import com.b3dgs.lionengine.graphic.Screen;
import com.b3dgs.lionengine.graphic.Text;
import com.b3dgs.lionengine.graphic.TextStyle;
import com.b3dgs.lionengine.graphic.Transform;
import com.b3dgs.lionengine.graphic.Transparency;
import com.b3dgs.lionengine.graphic.drawable.ImageHeader;
import com.b3dgs.lionengine.graphic.drawable.ImageInfo;

/**
 * Graphic factory implementation.
 */
// CHECKSTYLE IGNORE LINE: ClassDataAbstractionCoupling
public final class FactoryGraphicHeadless implements FactoryGraphic
{
    /** Reading image message. */
    static final String ERROR_IMAGE_READING = "Error on reading image !";
    /** Save image message. */
    static final String ERROR_IMAGE_SAVE = "Unable to save image: ";

    /**
     * Constructor.
     */
    public FactoryGraphicHeadless()
    {
        super();
    }

    /*
     * FactoryGraphic
     */

    @Override
    public Screen createScreen(Config config)
    {
        Check.notNull(config);

        return new ScreenHeadless(config);
    }

    @Override
    public Graphic createGraphic()
    {
        return new GraphicHeadless();
    }

    @Override
    public Transform createTransform()
    {
        return new TransformHeadless();
    }

    @Override
    public Text createText(String fontName, int size, TextStyle style)
    {
        return new TextHeadless(fontName, size, style);
    }

    @Override
    public ImageBuffer createImageBuffer(int width, int height)
    {
        final ImageBuffer buffer = new ImageBufferHeadless(width, height, Transparency.OPAQUE);

        final Graphic g = buffer.createGraphic();
        g.setColor(ColorRgba.BLACK);
        g.drawRect(0, 0, width, height, true);
        g.dispose();

        return buffer;
    }

    @Override
    public ImageBuffer createImageBuffer(int width, int height, ColorRgba transparency)
    {
        Check.notNull(transparency);

        final ImageBuffer buffer = new ImageBufferHeadless(width, height, Transparency.BITMASK);

        final Graphic g = buffer.createGraphic();
        g.setColor(transparency);
        g.drawRect(0, 0, width, height, true);
        g.dispose();

        return buffer;
    }

    @Override
    public ImageBuffer getImageBuffer(Media media)
    {
        Check.notNull(media);

        try
        {
            final ImageHeader info = ImageInfo.get(media);
            return new ImageBufferHeadless(info.getWidth(), info.getHeight(), Transparency.BITMASK);
        }
        catch (final LionEngineException exception)
        {
            throw new LionEngineException(exception, media, ERROR_IMAGE_READING);
        }
    }

    @Override
    public ImageBuffer getImageBuffer(ImageBuffer image)
    {
        Check.notNull(image);

        return new ImageBufferHeadless((ImageBufferHeadless) image);
    }

    @Override
    public ImageBuffer applyMask(ImageBuffer image, ColorRgba maskColor)
    {
        Check.notNull(image);
        Check.notNull(maskColor);

        final ImageBuffer mask = new ImageBufferHeadless((ImageBufferHeadless) image);
        final int height = mask.getHeight();
        final int width = mask.getWidth();
        final int rgba = maskColor.getRgba();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                final int col = mask.getRgb(x, y);
                final int flag = 0x00_FF_FF_FF;
                if (col == rgba)
                {
                    mask.setRgb(x, y, col & flag);
                }
            }
        }
        return mask;
    }

    @Override
    public ImageBuffer[] splitImage(ImageBuffer image, int h, int v)
    {
        Check.notNull(image);

        final ImageBuffer[] images = new ImageBuffer[h * v];
        for (int i = 0; i < images.length; i++)
        {
            images[i] = new ImageBufferHeadless(image.getWidth() / h, image.getHeight() / v, image.getTransparency());
        }
        return images;
    }

    @Override
    public ImageBuffer rotate(ImageBuffer image, int angle)
    {
        final Rectangle rotated = new Rectangle(0, 0, image.getWidth(), image.getHeight());
        rotated.rotate(angle);

        return new ImageBufferHeadless(rotated.getWidth(), rotated.getHeight(), image.getTransparency());
    }

    @Override
    public ImageBuffer resize(ImageBuffer image, int width, int height)
    {
        return new ImageBufferHeadless(width, height, image.getTransparency());
    }

    @Override
    public ImageBuffer flipHorizontal(ImageBuffer image)
    {
        Check.notNull(image);

        final ImageBuffer flip = new ImageBufferHeadless((ImageBufferHeadless) image);
        final int height = flip.getHeight();
        final int width = flip.getWidth();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                final int rgba = flip.getRgb(x, y);
                flip.setRgb(width - x - 1, y, rgba);
            }
        }
        return flip;
    }

    @Override
    public ImageBuffer flipVertical(ImageBuffer image)
    {
        Check.notNull(image);

        final ImageBuffer flip = new ImageBufferHeadless((ImageBufferHeadless) image);
        final int height = flip.getHeight();
        final int width = flip.getWidth();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                final int rgba = flip.getRgb(x, y);
                flip.setRgb(x, height - y - 1, rgba);
            }
        }
        return flip;
    }

    @Override
    public void saveImage(ImageBuffer image, Media media)
    {
        Check.notNull(media);

        try (OutputStream output = media.getOutputStream())
        {
            output.write(UtilConversion.intToByteArray(image.getWidth()));
            output.write(UtilConversion.intToByteArray(image.getHeight()));
        }
        catch (final IOException exception)
        {
            throw new LionEngineException(exception, media, ERROR_IMAGE_SAVE);
        }
    }

    @Override
    public ImageBuffer getRasterBuffer(ImageBuffer image, double fr, double fg, double fb)
    {
        Check.notNull(image);

        return new ImageBufferHeadless((ImageBufferHeadless) image);
    }
}

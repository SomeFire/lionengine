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
package com.b3dgs.lionengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Media implementation.
 * <p>
 * This class is Thread-Safe.
 * </p>
 */
final class MediaDefault implements Media
{
    /** Invalid path directory. */
    static final String ERROR_PATH_DIR = "Invalid directory: ";
    /** Error open media. */
    static final String ERROR_OPEN_MEDIA = "Cannot open the media !";
    /** Temp folder property. */
    private static final String TEMP_DIR = "java.io.tmpdir";
    /** No parent. */
    private static final String NO_PARENT = Constant.EMPTY_STRING;
    /** Unable to create temp directory. */
    private static final String ERROR_CREATE_TEMP_DIR = "Unable to create temp dir ";
    /** Temp folder. */
    private static final String TEMP = Constant.getSystemProperty(TEMP_DIR, Constant.EMPTY_STRING);
    /** Split regex. */
    private static final Pattern SLASH = Pattern.compile(Constant.SLASH);

    /**
     * Create the temp directory relative to loader class name.
     * 
     * @param loader The class loader.
     * @return The temp directory absolute path.
     */
    private static String getTempDir(Class<?> loader)
    {
        final File temp = new File(TEMP, loader.getSimpleName());
        final String path = temp.getAbsolutePath();
        if (!temp.isDirectory() && !temp.mkdir())
        {
            Verbose.warning(ERROR_CREATE_TEMP_DIR, path);
        }
        return path;
    }

    /** Separator. */
    private final String separator;
    /** Resources directory. */
    private final String resourcesDir;
    /** Class loader. */
    private final Optional<Class<?>> loader;
    /** Media path. */
    private final String path;
    /** Media parent path. */
    private final String parent;
    /** Media name. */
    private final String name;

    /**
     * Internal constructor.
     * 
     * @param separator The separator used (must not be <code>null</code>).
     * @param resourcesDir The resources directory path (can be <code>null</code>).
     * @param path The media path (must not be <code>null</code>).
     * @throws LionEngineException If invalid arguments.
     */
    MediaDefault(String separator, String resourcesDir, String path)
    {
        this(separator, resourcesDir, null, path);
    }

    /**
     * Internal constructor.
     * 
     * @param separator The separator used (must not be <code>null</code>).
     * @param loader The class loader used (can be <code>null</code> if not used).
     * @param path The media path (must not be <code>null</code>).
     * @throws LionEngineException If invalid arguments.
     */
    MediaDefault(String separator, Class<?> loader, String path)
    {
        this(separator, null, loader, path);
    }

    /**
     * Internal constructor.
     * 
     * @param separator The separator used (must not be <code>null</code>).
     * @param resourcesDir The resources directory path (can be <code>null</code> if not used).
     * @param loader The class loader used (can be <code>null</code> if not used).
     * @param path The media path (must not be <code>null</code>).
     * @throws LionEngineException If invalid arguments.
     */
    private MediaDefault(String separator, String resourcesDir, Class<?> loader, String path)
    {
        super();

        Check.notNull(separator);
        Check.notNull(path);

        this.separator = separator;
        this.resourcesDir = resourcesDir != null ? resourcesDir : Constant.EMPTY_STRING;
        this.loader = Optional.ofNullable(loader);
        this.path = path;
        final int index = path.lastIndexOf(separator);
        if (index > -1)
        {
            parent = path.substring(0, index);
        }
        else
        {
            parent = NO_PARENT;
        }
        name = path.substring(path.lastIndexOf(separator) + 1);
    }

    /**
     * Get the resources prefix.
     * 
     * @return The resources prefix.
     */
    private String getPrefix()
    {
        final String prefix;
        if (loader.isPresent())
        {
            prefix = loader.get().getPackage().getName().replace(Constant.DOT, separator);
        }
        else
        {
            prefix = resourcesDir;
        }
        return prefix;
    }

    /**
     * Create media from file.
     * 
     * @param prefix The prefix path.
     * @param prefixLength The prefix length.
     * @param file The file to created as media.
     * @return The created media.
     */
    private Media create(String prefix, int prefixLength, File file)
    {
        final String currentPath = file.getPath().replace(File.separator, separator);
        final String[] systemPath = SLASH.split(currentPath.substring(currentPath.indexOf(prefix) + prefixLength));
        final Media media;
        if (loader.isPresent())
        {
            media = new MediaDefault(separator, loader.get(), UtilFolder.getPathSeparator(separator, systemPath));
        }
        else
        {
            media = new MediaDefault(separator, resourcesDir, UtilFolder.getPathSeparator(separator, systemPath));
        }
        return media;
    }

    /**
     * Get input stream from JAR by default, try in temporary folder if not found in JAR.
     * 
     * @return The input stream found.
     * @throws FileNotFoundException If no stream found.
     */
    private InputStream getInputFromJarOrTemp() throws FileNotFoundException
    {
        final InputStream input = loader.get().getResourceAsStream(UtilFolder.getPathSeparator(separator, getPath()));
        if (input == null)
        {
            return new FileInputStream(getPathTemp());
        }
        return input;
    }

    /**
     * Get the absolute media path.
     * 
     * @return The absolute media path.
     */
    private String getPathAbsolute()
    {
        return UtilFolder.getPathSeparator(separator, resourcesDir, path);
    }

    /**
     * Get the temporary path equivalent.
     * 
     * @return The temporary path.
     */
    private String getPathTemp()
    {
        return UtilFolder.getPathSeparator(separator, getTempDir(loader.get()), path);
    }

    /*
     * Media
     */

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getPath()
    {
        return path;
    }

    @Override
    public String getParentPath()
    {
        return parent;
    }

    @Override
    public File getFile()
    {
        final File file;
        if (loader.isPresent())
        {
            final URL url = loader.get().getResource(path);
            if (url == null)
            {
                file = new File(getPathTemp());
            }
            else
            {
                file = new File(url.getFile());
            }
        }
        else
        {
            file = new File(getPathAbsolute());
        }
        return file;
    }

    @Override
    public Collection<Media> getMedias()
    {
        final File file = getFile();
        final File[] files = file.listFiles();
        if (files != null)
        {
            final Collection<Media> medias = new ArrayList<>(files.length);
            final String prefix = getPrefix();
            final int prefixLength = prefix.length();

            for (final File current : files)
            {
                final Media media = create(prefix, prefixLength, current);
                medias.add(media);
            }
            return medias;
        }
        throw new LionEngineException(this, ERROR_PATH_DIR);
    }

    @Override
    public InputStream getInputStream()
    {
        try
        {
            if (loader.isPresent())
            {
                return getInputFromJarOrTemp();
            }
            return new FileInputStream(getPathAbsolute());
        }
        catch (final FileNotFoundException exception)
        {
            throw new LionEngineException(exception, this, ERROR_OPEN_MEDIA);
        }
    }

    @Override
    public OutputStream getOutputStream()
    {
        try
        {
            if (loader.isPresent())
            {
                final String outputPath = getPathTemp();
                if (new File(outputPath).getParentFile().mkdirs())
                {
                    Verbose.info("Temp path created: ", outputPath);
                }
                return new FileOutputStream(outputPath);
            }
            return new FileOutputStream(getPathAbsolute());
        }
        catch (final FileNotFoundException exception)
        {
            throw new LionEngineException(exception, this, ERROR_OPEN_MEDIA);
        }
    }

    @Override
    public boolean exists()
    {
        if (loader.isPresent())
        {
            final String jarPath = UtilFolder.getPathSeparator(separator, getPath());
            return loader.get().getResource(jarPath) != null || UtilFile.exists(getPathTemp());
        }
        return getFile().exists();
    }

    /*
     * Object
     */

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + path.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || object.getClass() != getClass())
        {
            return false;
        }
        final MediaDefault media = (MediaDefault) object;
        return path.equals(media.path);
    }

    @Override
    public String toString()
    {
        return path;
    }
}

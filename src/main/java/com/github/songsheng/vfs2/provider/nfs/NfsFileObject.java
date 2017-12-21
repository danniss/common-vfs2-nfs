/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.songsheng.vfs2.provider.nfs;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import com.sun.nfs.XFileExtensionAccessor;
import com.sun.xfile.XFile;
import com.sun.xfile.XRandomAccessFile;
import com.sun.xfile.XFileInputStream;
import com.sun.xfile.XFileOutputStream;



import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.FileTypeHasNoContentException;
import org.apache.commons.vfs2.RandomAccessContent;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileObject;
import org.apache.commons.vfs2.provider.UriParser;
import org.apache.commons.vfs2.util.RandomAccessMode;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;

/**
 * A file in an Nfs file system.
 */
public class NfsFileObject
    extends AbstractFileObject<NfsFileSystem>
{
    // private final String fileName;
    private XFile file;

    protected NfsFileObject(final AbstractFileName name,
                            final NfsFileSystem fileSystem) throws FileSystemException
    {
        super(name, fileSystem);
        // this.fileName = UriParser.decode(name.getURI());
    }

    /**
     * Attaches this file object to its file resource.
     */
    @Override
    protected void doAttach() throws Exception
    {
        // Defer creation of the NfsFile to here
        if (file == null)
        {
            file = createNfsFile(getName());
        }
    }

    @Override
    protected void doDetach() throws Exception
    {
        // file closed through content-streams
        file = null;
    }

    private XFile createNfsFile(final FileName fileName)
        throws MalformedURLException, FileSystemException
    {
        final NfsFileName NfsFileName = (NfsFileName) fileName;

        final String path = NfsFileName.getUriWithoutAuth();

        UserAuthenticationData authData = null;
        XFile file;
        try
        {
        	// no auth for nfs
            /*authData = UserAuthenticatorUtils.authenticate(
                           getFileSystem().getFileSystemOptions(),
                           NfsFileProvider.AUTHENTICATOR_TYPES);*/

            
            file = new XFile(path);

            /*if (file.isDirectory() && !file.toString().endsWith("/"))
            {
                file = new XFile(path + "/");
            }*/
            return file;
        }
        finally
        {
            UserAuthenticatorUtils.cleanup(authData); // might be null
        }
    }

    /**
     * Determines the type of the file, returns null if the file does not
     * exist.
     */
    @Override
    protected FileType doGetType() throws Exception
    {
        if (!file.exists())
        {
            return FileType.IMAGINARY;
        }
        else if (file.isDirectory())
        {
            return FileType.FOLDER;
        }
        else if (file.isFile())
        {
            return FileType.FILE;
        }

        throw new FileSystemException("vfs.provider.Nfs/get-type.error", getName());
    }

    /**
     * Lists the children of the file.  Is only called if {@link #doGetType}
     * returns {@link FileType#FOLDER}.
     */
    @Override
    protected String[] doListChildren() throws Exception
    {
        // VFS-210: do not try to get listing for anything else than directories
        if (!file.isDirectory())
        {
            return null;
        }

        return UriParser.encode(file.list());
    }

    /**
     * Determines if this file is hidden.
     */
    @Override
    protected boolean doIsHidden() throws Exception
    {
        return false;
    }

    /**
     * Deletes the file.
     */
    @Override
    protected void doDelete() throws Exception
    {
        file.delete();
    }

    @Override
    protected void doRename(final FileObject newfile) throws Exception
    {
        file.renameTo(createNfsFile(newfile.getName()));
    }

    /**
     * Creates this file as a folder.
     */
    @Override
    protected void doCreateFolder() throws Exception
    {
        file.mkdir();
        file = createNfsFile(getName());
    }

    /**
     * Returns the size of the file content (in bytes).
     */
    @Override
    protected long doGetContentSize() throws Exception
    {
        return file.length();
    }

    /**
     * Returns the last modified time of this file.
     */
    @Override
    protected long doGetLastModifiedTime()
        throws Exception
    {
        return file.lastModified();
    }

    /**
     * Creates an input stream to read the file content from.
     */
    @Override
    protected InputStream doGetInputStream() throws Exception
    {
        try
        {
            return new XFileInputStream(file);
        }
        catch (final Exception e)
        {
        	if (file.isDirectory())
            {
                throw new FileTypeHasNoContentException(getName());
            } else {
                throw new org.apache.commons.vfs2.FileNotFoundException(getName());
            }
        }
    }

    /**
     * Creates an output stream to write the file content to.
     */
    @Override
    protected OutputStream doGetOutputStream(final boolean bAppend) throws Exception
    {
        return new XFileOutputStream(file, bAppend);
    }

    /**
     * random access
     */
    @Override
    protected RandomAccessContent doGetRandomAccessContent(final RandomAccessMode mode) throws Exception
    {
        return new NfsFileRandomAccessContent(file, mode);
    }

    @Override
    protected boolean doSetLastModifiedTime(final long modtime) throws Exception
    {
        return true;
    }
}

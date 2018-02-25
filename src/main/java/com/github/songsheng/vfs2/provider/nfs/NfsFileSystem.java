package com.github.songsheng.vfs2.provider.nfs;

import java.util.Collection;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileSystem;

/**
 * An Nfs file system.
 */
public class NfsFileSystem
    extends AbstractFileSystem
{
    protected NfsFileSystem(final FileName rootName, final FileSystemOptions fileSystemOptions)
    {
        super(rootName, null, fileSystemOptions);
    }

    /**
     * Creates a file object.
     */
    @Override
    protected FileObject createFile(final AbstractFileName name) throws FileSystemException
    {
        return new NfsFileObject(name, this);
    }

    /**
     * Returns the capabilities of this file system.
     */
    @Override
    protected void addCapabilities(final Collection<Capability> caps)
    {
        caps.addAll(NfsFileProvider.capabilities);
    }
}

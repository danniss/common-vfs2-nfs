package com.github.songsheng.vfs2.provider.nfs;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.GenericFileName;

/**
 * An Nfs URI.  Adds a share name to the generic URI.
 */
public class NfsFileName extends GenericFileName
{
    private static final int DEFAULT_PORT = 139;

    private String uriWithoutAuth;

    protected NfsFileName(
        final String scheme,
        final String hostName,
        final int port,
        final String userName,
        final String password,
        final String path,
        final FileType type)
    {
        super(scheme, hostName, port, DEFAULT_PORT, userName, password, path, type);
    }


    /**
     * Factory method for creating name instances.
     *
     * @param path path of file.
     * @param type file or directory
     * @return new NfsFileName object, never null.
     */
    @Override
    public FileName createName(final String path, final FileType type)
    {
        return new NfsFileName(
            getScheme(),
            getHostName(),
            getPort(),
            getUserName(),
            getPassword(),
            path,
            type);
    }

    /**
     * Construct the path suitable for NfsFile when used with NtlmPasswordAuthentication.
     *
     * @return caches and return URI with no username/password, never null
     * @throws FileSystemException if any of the invoked methods throw
     */
    public String getUriWithoutAuth() throws FileSystemException
    {
        if (uriWithoutAuth != null)
        {
            return uriWithoutAuth;
        }

        final StringBuilder sb = new StringBuilder(120);
        sb.append(getScheme());
        sb.append("://");
        sb.append(getHostName());
        if (getPort() != DEFAULT_PORT)
        {
            sb.append(":");
            sb.append(getPort());
        }
        sb.append(getPathDecoded());
        uriWithoutAuth = sb.toString();
        return uriWithoutAuth;
    }

}

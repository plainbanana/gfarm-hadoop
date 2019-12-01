package org.apache.hadoop.fs.gfarmfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.gfarmfs.GfarmFileSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.AbstractFileSystem;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.CreateFlag;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileAlreadyExistsException;
import org.apache.hadoop.fs.FileChecksum;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileSystem.Statistics;
import org.apache.hadoop.fs.FsServerDefaults;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Options.ChecksumOpt;
import org.apache.hadoop.fs.ParentNotDirectoryException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.UnresolvedLinkException;
import org.apache.hadoop.fs.UnsupportedFileSystemException;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.security.AccessControlException;
import org.apache.hadoop.util.Progressable;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;

import java.io.*;
import java.net.*;

/**
 * Gfarm implementation of AbstractFileSystem. This impl delegates to the
 * GfarmFileSystem
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class GfarmAbstractFileSystem extends AbstractFileSystem {

    public Gfarm(URI theUri, Configuration conf) throws IOException, URISyntaxException {
        super(theUri, new GfarmFileSystem(), conf, "gfarm", false);
    }

    @Override
    public int getUriDefaultPort() {
        return super.getUriDefaultPort();
    }

    // @Override
    // public String toString() {
    //     final StringBuilder sb = new StringBuilder("GFARM{");
    //     sb.append("URI =").append(fsImpl.getUri());
    //     sb.append("; fsImpl=").append(fsImpl);
    //     sb.append('}');
    //     return sb.toString();
    // }
}

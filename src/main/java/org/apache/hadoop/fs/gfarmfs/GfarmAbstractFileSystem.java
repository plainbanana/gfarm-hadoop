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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;


import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;

/**
 * Gfarm implementation of AbstractFileSystem. This impl delegates to the
 * GfarmFileSystem
 */
public class GfarmAbstractFileSystem extends AbstractFileSystem {

    private final FileSystem fsImpl;

    public GfarmAbstractFileSystem(URI theUri, Configuration conf) throws IOException, URISyntaxException {
        this(theUri, new GfarmFileSystem(), conf,"gfarm", false);
        System.out.println("init abstract filesystem\n");
    }

    public GfarmAbstractFileSystem(URI uri, FileSystem fsImpl, Configuration conf, String supportedScheme, boolean authorityNeeded) throws IOException, URISyntaxException {
        super(uri, supportedScheme, authorityNeeded, FileSystem.getDefaultUri(conf).getPort());
        this.fsImpl = fsImpl;
        this.fsImpl.initialize(uri, conf);
    }

    @Override
    public int getUriDefaultPort() {
        return GfarmFileSystem.DEFAULT_GFMD_METADB_PORT;
    }

    @Override
    public void checkPath(Path path) {
        super.checkPath(path);
        System.out.println("checkPath(): super successfully");
    }

    @Override
    @Deprecated
    public FsServerDefaults getServerDefaults() throws IOException {
        return this.getServerDefaults();
    }

    @Override
    public FSDataOutputStream createInternal(Path f, EnumSet<CreateFlag> flag, FsPermission absolutePermission, int bufferSize, short replication, long blockSize, Progressable progress, ChecksumOpt checksumOpt, boolean createParent) throws AccessControlException, FileAlreadyExistsException, FileNotFoundException, ParentNotDirectoryException, UnsupportedFileSystemException, UnresolvedLinkException, IOException {
        checkPath(f);

        // Default impl assumes that permissions do not matter
        // calling the regular create is good enough.
        // FSs that implement permissions should override this.
        if (!createParent) { // parent must exist.
            // since this.create makes parent dirs automatically
            // we must throw exception if parent does not exist.
            final FileStatus stat = getFileStatus(f.getParent());
            if (stat == null) {
                throw new FileNotFoundException("Missing parent:" + f);
            }
            if (!stat.isDirectory()) {
                throw new ParentNotDirectoryException("parent is not a dir:" + f);
            }
            // parent does exist - go ahead with create of file.
        }

        FSDataOutputStream stream = fsImpl.create(f, absolutePermission, flag, bufferSize, replication, blockSize, progress, checksumOpt);
        return stream;
    }

    @Override
    public void mkdir(Path path, FsPermission fsPermission, boolean b) throws AccessControlException, FileAlreadyExistsException, FileNotFoundException, UnresolvedLinkException, IOException {
        System.out.println("mkdir");
        checkPath(path);
        boolean status = fsImpl.mkdirs(path);
        System.out.println("mkdir status: "+status);
    }

    @Override
    public boolean delete(Path path, boolean recursive) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(path);
        boolean status = fsImpl.delete(path, recursive);
        System.out.println("delete(): fsImpl returned status=" + status);
        return status;
    }

    @Override
    public FSDataInputStream open(Path f) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(f);
        FSDataInputStream stream = super.open(f);
        System.out.println("open(): super returned stream=" + stream);
        return stream;
    }

    @Override
    public FSDataInputStream open(Path f, int bufferSize) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(f);
        FSDataInputStream stream = fsImpl.open(f, bufferSize);
        System.out.println("open(): fsImpl returned stream=" + stream);
        return stream;
    }

    @Override
    public boolean setReplication(Path f, short replication) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(f);
        boolean set = fsImpl.setReplication(f, replication);
        System.out.println("setReplication(): fsImpl returned set=" + set);
        return set;
    }

    @Override
    public void renameInternal(Path src, Path dst) throws AccessControlException, FileAlreadyExistsException, FileNotFoundException, ParentNotDirectoryException, UnresolvedLinkException, IOException {
        checkPath(src);
        checkPath(dst);
        fsImpl.rename(src, dst);
        System.out.println("renameInternal(): fsImpl returned successfully");
    }

    @Override
    public void setPermission(Path f, FsPermission permission) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(f);
        fsImpl.setPermission(f, permission);
        System.out.println("setPermission(): fsImpl returned successfully");
    }

    @Override
    public void setOwner(Path f, String username, String groupname) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(f);
        fsImpl.setOwner(f, username, groupname);
        System.out.println("setOwner(): fsImpl returned successfully");
    }

    @Override
    public void setTimes(Path f, long mtime, long atime) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(f);
        fsImpl.setTimes(f, mtime, atime);
        System.out.println("setTimes(): fsImpl returned successfully");
    }

    @Override
    public FileChecksum getFileChecksum(Path f) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(f);
        FileChecksum checksum = fsImpl.getFileChecksum(f);
        System.out.println("getFileChecksum(): fsImpl returned status=" + checksum);
        return checksum;
    }

    @Override
    public FileStatus getFileStatus(Path f) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(f);
        FileStatus status = fsImpl.getFileStatus(f);
        System.out.println("getFileStatus(): fsImpl returned status=" + status);
        return status;
    }

    @Override
    public BlockLocation[] getFileBlockLocations(Path f, long start, long len) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(f);
        BlockLocation[] locations = fsImpl.getFileBlockLocations(f, start, len);
        System.out.println("getFileBlockLocations(): fsImpl returned locations=" + locations);
        return locations;
    }

    @Override
    public FsStatus getFsStatus() throws AccessControlException, FileNotFoundException, IOException {
        FsStatus status = fsImpl.getStatus();
        System.out.println("getFsStatus(): fsImpl returned status=" + status);
        return status;
    }

    @Override
    public FileStatus[] listStatus(Path f) throws AccessControlException, FileNotFoundException, UnresolvedLinkException, IOException {
        checkPath(f);
        FileStatus[] status = fsImpl.listStatus(f);
        System.out.println("listStatus(): fsImpl returned status=" + status);
        return status;
    }

    @Override
    public void setVerifyChecksum(boolean verifyChecksum) throws AccessControlException, IOException {
        fsImpl.setVerifyChecksum(verifyChecksum);
        System.out.println("setVerifyChecksum(): fsImpl returned successfully");
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

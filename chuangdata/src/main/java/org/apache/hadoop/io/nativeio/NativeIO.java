//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.apache.hadoop.io.nativeio;

import com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.apache.hadoop.classification.InterfaceStability.Unstable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.HardLink;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SecureIOUtils.AlreadyExistsException;
import org.apache.hadoop.io.nativeio.Errno;
import org.apache.hadoop.io.nativeio.NativeIOException;
import org.apache.hadoop.util.NativeCodeLoader;
import org.apache.hadoop.util.PerformanceAdvisory;
import org.apache.hadoop.util.Shell;
import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

@Private
@Unstable
public class NativeIO {
    private static boolean workaroundNonThreadSafePasswdCalls = false;
    private static final Log LOG = LogFactory.getLog(NativeIO.class);
    private static boolean nativeLoaded = false;
    private static final Map<Long, CachedUid> uidCache;
    private static long cacheTimeout;
    private static boolean initialized;

    public NativeIO() {
    }

    public static boolean isAvailable() {
        return NativeCodeLoader.isNativeCodeLoaded() && nativeLoaded;
    }

    private static native void initNative();

    static long getMemlockLimit() {
        return isAvailable()?getMemlockLimit0():0L;
    }

    private static native long getMemlockLimit0();

    static long getOperatingSystemPageSize() {
        try {
            Field e = Unsafe.class.getDeclaredField("theUnsafe");
            e.setAccessible(true);
            Unsafe unsafe = (Unsafe)e.get((Object)null);
            return (long)unsafe.pageSize();
        } catch (Throwable var2) {
            LOG.warn("Unable to get operating system page size.  Guessing 4096.", var2);
            return 4096L;
        }
    }

    private static String stripDomain(String name) {
        int i = name.indexOf(92);
        if(i != -1) {
            name = name.substring(i + 1);
        }

        return name;
    }

    public static String getOwner(FileDescriptor fd) throws IOException {
        ensureInitialized();
        if(Shell.WINDOWS) {
            String uid1 = Windows.getOwner(fd);
            uid1 = stripDomain(uid1);
            return uid1;
        } else {
            long uid = POSIX.getUIDforFDOwnerforOwner(fd);
            CachedUid cUid = (CachedUid)uidCache.get(Long.valueOf(uid));
            long now = System.currentTimeMillis();
            if(cUid != null && cUid.timestamp + cacheTimeout > now) {
                return cUid.username;
            } else {
                String user = POSIX.getUserName(uid);
                LOG.info("Got UserName " + user + " for UID " + uid + " from the native implementation");
                cUid = new CachedUid(user, now);
                uidCache.put(Long.valueOf(uid), cUid);
                return user;
            }
        }
    }

    public static FileInputStream getShareDeleteFileInputStream(File f) throws IOException {
        if(!Shell.WINDOWS) {
            return new FileInputStream(f);
        } else {
            FileDescriptor fd = Windows.createFile(f.getAbsolutePath(), 2147483648L, 7L, 3L);
            return new FileInputStream(fd);
        }
    }

    public static FileInputStream getShareDeleteFileInputStream(File f, long seekOffset) throws IOException {
        if(!Shell.WINDOWS) {
            RandomAccessFile fd1 = new RandomAccessFile(f, "r");
            if(seekOffset > 0L) {
                fd1.seek(seekOffset);
            }

            return new FileInputStream(fd1.getFD());
        } else {
            FileDescriptor fd = Windows.createFile(f.getAbsolutePath(), 2147483648L, 7L, 3L);
            if(seekOffset > 0L) {
                Windows.setFilePointer(fd, seekOffset, 0L);
            }

            return new FileInputStream(fd);
        }
    }

    public static FileOutputStream getCreateForWriteFileOutputStream(File f, int permissions) throws IOException {
        FileDescriptor nioe;
        if(!Shell.WINDOWS) {
            try {
                nioe = POSIX.open(f.getAbsolutePath(), 193, permissions);
                return new FileOutputStream(nioe);
            } catch (NativeIOException var4) {
                if(var4.getErrno() == Errno.EEXIST) {
                    throw new AlreadyExistsException(var4);
                } else {
                    throw var4;
                }
            }
        } else {
            try {
                nioe = Windows.createFile(f.getCanonicalPath(), 1073741824L, 7L, 1L);
                POSIX.chmod(f.getCanonicalPath(), permissions);
                return new FileOutputStream(nioe);
            } catch (NativeIOException var3) {
                if(var3.getErrorCode() == 80L) {
                    throw new AlreadyExistsException(var3);
                } else {
                    throw var3;
                }
            }
        }
    }

    private static synchronized void ensureInitialized() {
        if(!initialized) {
            cacheTimeout = (new Configuration()).getLong("hadoop.security.uid.cache.secs", 14400L) * 1000L;
            LOG.info("Initialized cache for UID to User mapping with a cache timeout of " + cacheTimeout / 1000L + " seconds.");
            initialized = true;
        }

    }

    public static void renameTo(File src, File dst) throws IOException {
        if(!nativeLoaded) {
            if(!src.renameTo(dst)) {
                throw new IOException("renameTo(src=" + src + ", dst=" + dst + ") failed.");
            }
        } else {
            renameTo0(src.getAbsolutePath(), dst.getAbsolutePath());
        }

    }

    public static void link(File src, File dst) throws IOException {
        if(!nativeLoaded) {
            HardLink.createHardLink(src, dst);
        } else {
            link0(src.getAbsolutePath(), dst.getAbsolutePath());
        }

    }

    private static native void renameTo0(String var0, String var1) throws NativeIOException;

    private static native void link0(String var0, String var1) throws NativeIOException;

    public static void copyFileUnbuffered(File src, File dst) throws IOException {
        if(nativeLoaded && Shell.WINDOWS) {
            copyFileUnbuffered0(src.getAbsolutePath(), dst.getAbsolutePath());
        } else {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel input = null;
            FileChannel output = null;

            try {
                fis = new FileInputStream(src);
                fos = new FileOutputStream(dst);
                input = fis.getChannel();
                output = fos.getChannel();
                long remaining = input.size();
                long position = 0L;

                for(long transferred = 0L; remaining > 0L; position += transferred) {
                    transferred = input.transferTo(position, remaining, output);
                    remaining -= transferred;
                }
            } finally {
                IOUtils.cleanup(LOG, new Closeable[]{output});
                IOUtils.cleanup(LOG, new Closeable[]{fos});
                IOUtils.cleanup(LOG, new Closeable[]{input});
                IOUtils.cleanup(LOG, new Closeable[]{fis});
            }
        }

    }

    private static native void copyFileUnbuffered0(String var0, String var1) throws NativeIOException;

    static {
        if(NativeCodeLoader.isNativeCodeLoaded()) {
            try {
                initNative();
                nativeLoaded = true;
            } catch (Throwable var1) {
                PerformanceAdvisory.LOG.debug("Unable to initialize NativeIO libraries", var1);
            }
        }

        uidCache = new ConcurrentHashMap();
        initialized = false;
    }

    private static class CachedUid {
        final long timestamp;
        final String username;

        public CachedUid(String username, long timestamp) {
            this.timestamp = timestamp;
            this.username = username;
        }
    }

    public static class Windows {
        public static final long GENERIC_READ = 2147483648L;
        public static final long GENERIC_WRITE = 1073741824L;
        public static final long FILE_SHARE_READ = 1L;
        public static final long FILE_SHARE_WRITE = 2L;
        public static final long FILE_SHARE_DELETE = 4L;
        public static final long CREATE_NEW = 1L;
        public static final long CREATE_ALWAYS = 2L;
        public static final long OPEN_EXISTING = 3L;
        public static final long OPEN_ALWAYS = 4L;
        public static final long TRUNCATE_EXISTING = 5L;
        public static final long FILE_BEGIN = 0L;
        public static final long FILE_CURRENT = 1L;
        public static final long FILE_END = 2L;
        public static final long FILE_ATTRIBUTE_NORMAL = 128L;

        public Windows() {
        }

        public static native FileDescriptor createFile(String var0, long var1, long var3, long var5) throws IOException;

        public static native long setFilePointer(FileDescriptor var0, long var1, long var3) throws IOException;

        private static native String getOwner(FileDescriptor var0) throws IOException;

        private static native boolean access0(String var0, int var1);

        public static boolean access(String path, AccessRight desiredAccess) throws IOException {
            return true;
            // return access0(path, desiredAccess.accessRight());
        }

        public static native void extendWorkingSetSize(long var0) throws IOException;

        static {
            if(NativeCodeLoader.isNativeCodeLoaded()) {
                try {
                    NativeIO.initNative();
                    NativeIO.nativeLoaded = true;
                } catch (Throwable var1) {
                    PerformanceAdvisory.LOG.debug("Unable to initialize NativeIO libraries", var1);
                }
            }

        }

        public static enum AccessRight {
            ACCESS_READ(1),
            ACCESS_WRITE(2),
            ACCESS_EXECUTE(32);

            private final int accessRight;

            private AccessRight(int access) {
                this.accessRight = access;
            }

            public int accessRight() {
                return this.accessRight;
            }
        }
    }

    public static class POSIX {
        public static final int O_RDONLY = 0;
        public static final int O_WRONLY = 1;
        public static final int O_RDWR = 2;
        public static final int O_CREAT = 64;
        public static final int O_EXCL = 128;
        public static final int O_NOCTTY = 256;
        public static final int O_TRUNC = 512;
        public static final int O_APPEND = 1024;
        public static final int O_NONBLOCK = 2048;
        public static final int O_SYNC = 4096;
        public static final int O_ASYNC = 8192;
        public static final int O_FSYNC = 4096;
        public static final int O_NDELAY = 2048;
        public static final int POSIX_FADV_NORMAL = 0;
        public static final int POSIX_FADV_RANDOM = 1;
        public static final int POSIX_FADV_SEQUENTIAL = 2;
        public static final int POSIX_FADV_WILLNEED = 3;
        public static final int POSIX_FADV_DONTNEED = 4;
        public static final int POSIX_FADV_NOREUSE = 5;
        public static final int SYNC_FILE_RANGE_WAIT_BEFORE = 1;
        public static final int SYNC_FILE_RANGE_WRITE = 2;
        public static final int SYNC_FILE_RANGE_WAIT_AFTER = 4;
        private static final Log LOG = LogFactory.getLog(NativeIO.class);
        private static boolean nativeLoaded = false;
        private static boolean fadvisePossible = true;
        private static boolean syncFileRangePossible = true;
        static final String WORKAROUND_NON_THREADSAFE_CALLS_KEY = "hadoop.workaround.non.threadsafe.getpwuid";
        static final boolean WORKAROUND_NON_THREADSAFE_CALLS_DEFAULT = true;
        private static long cacheTimeout = -1L;
        private static CacheManipulator cacheManipulator = new CacheManipulator();
        private static final Map<Integer, CachedName> USER_ID_NAME_CACHE;
        private static final Map<Integer, CachedName> GROUP_ID_NAME_CACHE;
        public static final int MMAP_PROT_READ = 1;
        public static final int MMAP_PROT_WRITE = 2;
        public static final int MMAP_PROT_EXEC = 4;

        public POSIX() {
        }

        public static CacheManipulator getCacheManipulator() {
            return cacheManipulator;
        }

        public static void setCacheManipulator(CacheManipulator cacheManipulator) {
            POSIX.cacheManipulator = cacheManipulator;
        }

        public static boolean isAvailable() {
            return NativeCodeLoader.isNativeCodeLoaded() && nativeLoaded;
        }

        private static void assertCodeLoaded() throws IOException {
            if(!isAvailable()) {
                throw new IOException("NativeIO was not loaded");
            }
        }

        public static native FileDescriptor open(String var0, int var1, int var2) throws IOException;

        private static native Stat fstat(FileDescriptor var0) throws IOException;

        private static native void chmodImpl(String var0, int var1) throws IOException;

        public static void chmod(String path, int mode) throws IOException {
            if(!Shell.WINDOWS) {
                chmodImpl(path, mode);
            } else {
                try {
                    chmodImpl(path, mode);
                } catch (NativeIOException var3) {
                    if(var3.getErrorCode() == 3L) {
                        throw new NativeIOException("No such file or directory", Errno.ENOENT);
                    }

                    LOG.warn(String.format("NativeIO.chmod error (%d): %s", new Object[]{Long.valueOf(var3.getErrorCode()), var3.getMessage()}));
                    throw new NativeIOException("Unknown error", Errno.UNKNOWN);
                }
            }

        }

        static native void posix_fadvise(FileDescriptor var0, long var1, long var3, int var5) throws NativeIOException;

        static native void sync_file_range(FileDescriptor var0, long var1, long var3, int var5) throws NativeIOException;

        static void posixFadviseIfPossible(String identifier, FileDescriptor fd, long offset, long len, int flags) throws NativeIOException {
            if(nativeLoaded && fadvisePossible) {
                try {
                    posix_fadvise(fd, offset, len, flags);
                } catch (UnsupportedOperationException var8) {
                    fadvisePossible = false;
                } catch (UnsatisfiedLinkError var9) {
                    fadvisePossible = false;
                }
            }

        }

        public static void syncFileRangeIfPossible(FileDescriptor fd, long offset, long nbytes, int flags) throws NativeIOException {
            if(nativeLoaded && syncFileRangePossible) {
                try {
                    sync_file_range(fd, offset, nbytes, flags);
                } catch (UnsupportedOperationException var7) {
                    syncFileRangePossible = false;
                } catch (UnsatisfiedLinkError var8) {
                    syncFileRangePossible = false;
                }
            }

        }

        static native void mlock_native(ByteBuffer var0, long var1) throws NativeIOException;

        static void mlock(ByteBuffer buffer, long len) throws IOException {
            assertCodeLoaded();
            if(!buffer.isDirect()) {
                throw new IOException("Cannot mlock a non-direct ByteBuffer");
            } else {
                mlock_native(buffer, len);
            }
        }

        public static void munmap(MappedByteBuffer buffer) {
            if(buffer instanceof DirectBuffer) {
                Cleaner cleaner = ((DirectBuffer)buffer).cleaner();
                cleaner.clean();
            }

        }

        private static native long getUIDforFDOwnerforOwner(FileDescriptor var0) throws IOException;

        private static native String getUserName(long var0) throws IOException;

        public static Stat getFstat(FileDescriptor fd) throws IOException {
            Stat stat = null;
            if(!Shell.WINDOWS) {
                stat = fstat(fd);
                stat.owner = getName(IdCache.USER, stat.ownerId);
                stat.group = getName(IdCache.GROUP, stat.groupId);
            } else {
                try {
                    stat = fstat(fd);
                } catch (NativeIOException var3) {
                    if(var3.getErrorCode() == 6L) {
                        throw new NativeIOException("The handle is invalid.", Errno.EBADF);
                    }

                    LOG.warn(String.format("NativeIO.getFstat error (%d): %s", new Object[]{Long.valueOf(var3.getErrorCode()), var3.getMessage()}));
                    throw new NativeIOException("Unknown error", Errno.UNKNOWN);
                }
            }

            return stat;
        }

        private static String getName(IdCache domain, int id) throws IOException {
            Map idNameCache = domain == IdCache.USER?USER_ID_NAME_CACHE:GROUP_ID_NAME_CACHE;
            CachedName cachedName = (CachedName)idNameCache.get(Integer.valueOf(id));
            long now = System.currentTimeMillis();
            String name;
            if(cachedName != null && cachedName.timestamp + cacheTimeout > now) {
                name = cachedName.name;
            } else {
                name = domain == IdCache.USER?getUserName(id):getGroupName(id);
                if(LOG.isDebugEnabled()) {
                    String type = domain == IdCache.USER?"UserName":"GroupName";
                    LOG.debug("Got " + type + " " + name + " for ID " + id + " from the native implementation");
                }

                cachedName = new CachedName(name, now);
                idNameCache.put(Integer.valueOf(id), cachedName);
            }

            return name;
        }

        static native String getUserName(int var0) throws IOException;

        static native String getGroupName(int var0) throws IOException;

        public static native long mmap(FileDescriptor var0, int var1, boolean var2, long var3) throws IOException;

        public static native void munmap(long var0, long var2) throws IOException;

        static {
            if(NativeCodeLoader.isNativeCodeLoaded()) {
                try {
                    Configuration t = new Configuration();
                    NativeIO.workaroundNonThreadSafePasswdCalls = t.getBoolean("hadoop.workaround.non.threadsafe.getpwuid", true);
                    NativeIO.initNative();
                    nativeLoaded = true;
                    cacheTimeout = t.getLong("hadoop.security.uid.cache.secs", 14400L) * 1000L;
                    LOG.debug("Initialized cache for IDs to User/Group mapping with a  cache timeout of " + cacheTimeout / 1000L + " seconds.");
                } catch (Throwable var1) {
                    PerformanceAdvisory.LOG.debug("Unable to initialize NativeIO libraries", var1);
                }
            }

            USER_ID_NAME_CACHE = new ConcurrentHashMap();
            GROUP_ID_NAME_CACHE = new ConcurrentHashMap();
        }

        private static enum IdCache {
            USER,
            GROUP;

            private IdCache() {
            }
        }

        private static class CachedName {
            final long timestamp;
            final String name;

            public CachedName(String name, long timestamp) {
                this.name = name;
                this.timestamp = timestamp;
            }
        }

        public static class Stat {
            private int ownerId;
            private int groupId;
            private String owner;
            private String group;
            private int mode;
            public static final int S_IFMT = 61440;
            public static final int S_IFIFO = 4096;
            public static final int S_IFCHR = 8192;
            public static final int S_IFDIR = 16384;
            public static final int S_IFBLK = 24576;
            public static final int S_IFREG = 32768;
            public static final int S_IFLNK = 40960;
            public static final int S_IFSOCK = 49152;
            public static final int S_IFWHT = 57344;
            public static final int S_ISUID = 2048;
            public static final int S_ISGID = 1024;
            public static final int S_ISVTX = 512;
            public static final int S_IRUSR = 256;
            public static final int S_IWUSR = 128;
            public static final int S_IXUSR = 64;

            Stat(int ownerId, int groupId, int mode) {
                this.ownerId = ownerId;
                this.groupId = groupId;
                this.mode = mode;
            }

            Stat(String owner, String group, int mode) {
                if(!Shell.WINDOWS) {
                    this.owner = owner;
                } else {
                    this.owner = NativeIO.stripDomain(owner);
                }

                if(!Shell.WINDOWS) {
                    this.group = group;
                } else {
                    this.group = NativeIO.stripDomain(group);
                }

                this.mode = mode;
            }

            public String toString() {
                return "Stat(owner=\'" + this.owner + "\', group=\'" + this.group + "\'" + ", mode=" + this.mode + ")";
            }

            public String getOwner() {
                return this.owner;
            }

            public String getGroup() {
                return this.group;
            }

            public int getMode() {
                return this.mode;
            }
        }

        @VisibleForTesting
        public static class NoMlockCacheManipulator extends CacheManipulator {
            public NoMlockCacheManipulator() {
            }

            public void mlock(String identifier, ByteBuffer buffer, long len) throws IOException {
                POSIX.LOG.info("mlocking " + identifier);
            }

            public long getMemlockLimit() {
                return 1125899906842624L;
            }

            public long getOperatingSystemPageSize() {
                return 4096L;
            }

            public boolean verifyCanMlock() {
                return true;
            }
        }

        @VisibleForTesting
        public static class CacheManipulator {
            public CacheManipulator() {
            }

            public void mlock(String identifier, ByteBuffer buffer, long len) throws IOException {
                POSIX.mlock(buffer, len);
            }

            public long getMemlockLimit() {
                return NativeIO.getMemlockLimit();
            }

            public long getOperatingSystemPageSize() {
                return NativeIO.getOperatingSystemPageSize();
            }

            public void posixFadviseIfPossible(String identifier, FileDescriptor fd, long offset, long len, int flags) throws NativeIOException {
                POSIX.posixFadviseIfPossible(identifier, fd, offset, len, flags);
            }

            public boolean verifyCanMlock() {
                return NativeIO.isAvailable();
            }
        }
    }
}

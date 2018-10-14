package com.chuangdata.userprofile.job;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class JobDriver {
    private Configuration conf = new Configuration();
    private String[] args;

    protected JobDriver(String[] args) {
        this.args = args;
    }

    protected Configuration getConfiguration() {
        return this.conf;
    }

    protected void setProperty(String name, String value) {
        this.conf.set(name, value);
    }

    protected String[] parseHadoopArgs(String[] args) throws IOException {
        return new GenericOptionsParser(conf, args).getRemainingArgs();
    }

    public int run() throws Exception {
        return run(parseHadoopArgs(args));
    }

    protected abstract int run(String[] args) throws Exception;

    protected void setDistributedCache(String cacheFilePath, String linkName) throws URISyntaxException {
        DistributedCache.createSymlink(conf);
        String top_host_cache_file = new Path(cacheFilePath).toUri() + "#" + linkName;
        DistributedCache.addCacheFile(new URI(top_host_cache_file), conf);
    }
}


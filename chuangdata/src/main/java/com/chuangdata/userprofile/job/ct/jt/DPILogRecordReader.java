package com.chuangdata.userprofile.job.ct.jt;
/**
 * @author luxiaofeng
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class DPILogRecordReader extends RecordReader<LongWritable, Text> {

    private long start;
    private long pos;
    private long end;
    private Reader in;

    private final LongWritable key = new LongWritable();
    private final Text value = new Text();

    /**
     * Get the progress within the split.
     */
    @Override
    public float getProgress() {
        if (start == end) {
            return 0.0f;
        } else {
            return Math.min(1.0f, (pos - start) / (float) (end - start));
        }
    }

    public synchronized long getPos() throws IOException {
        return pos;
    }

    @Override
    public synchronized void close() throws IOException {
        if (in != null) {
            IOUtils.closeStream(in);
        }
    }

    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException, InterruptedException {
        FileSplit split = (FileSplit) genericSplit;
        start = split.getStart();
        end = start + split.getLength();
        final Path file = split.getPath();
        Configuration job = context.getConfiguration();

//        FileSystem fs = file.getFileSystem(job);
        in = new Reader(job, Reader.file(file));

        // start should always be 0
        if (start != 0) {
            // never get here
            /*fileIn.seek(start);

            // read and ignore the first line
            in.readLine(new Text());
            start = fileIn.getPos();*/
        }

        this.pos = start;
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (in.next(key, value)) {
            // readed key and value
            pos = in.getPosition();
            return true;
        } else {
            pos = end;
            return false;
        }
    }
}

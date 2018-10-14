package com.chuangdata.userprofile.model;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Set;

public class MultipleModel implements Writable {
    private MapWritable mapWritable;

    public MultipleModel() {
        mapWritable = new MapWritable();
    }

    public void add(Writable model) {
        this.mapWritable.put(model, NullWritable.get());
    }

    public Set<Writable> getSet() {
        return mapWritable.keySet();
    }

    public void readFields(DataInput in) throws IOException {
        mapWritable.readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        mapWritable.write(out);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Writable w : getSet()) {
            builder.append(w.toString()).append("\t");
        }
        if (builder.length() > 0)
            return builder.toString().substring(0, builder.length() - 1);
        return builder.toString();
    }

}

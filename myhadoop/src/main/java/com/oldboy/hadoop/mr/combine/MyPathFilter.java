package com.oldboy.hadoop.mr.combine;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

/**
 * 路径过滤器
 */
public class MyPathFilter implements PathFilter{
	public boolean accept(Path path) {
		String name = path.getName();
		return name.contains("9") ;
	}
}

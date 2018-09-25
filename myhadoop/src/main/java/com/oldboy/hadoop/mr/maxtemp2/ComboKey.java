package com.oldboy.hadoop.mr.maxtemp2;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 组合key
 */
public class ComboKey implements WritableComparable<ComboKey> {
	private int year ;
	private int temp ;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	public int compareTo(ComboKey o) {
		int oyear = o.getYear() ;
		int otemp = o.getTemp() ;
		if(this.year == oyear){
			return -(this.temp - otemp) ;
		}
		else {
			return this.year - oyear ;
		}
	}

	//串行
	public void write(DataOutput out) throws IOException {
		out.writeInt(year);
		out.writeInt(temp);
	}

	//反串
	public void readFields(DataInput in) throws IOException {
		this.year = in.readInt() ;
		this.temp = in.readInt() ;
	}
}

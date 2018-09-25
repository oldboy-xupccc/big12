package com.oldboy.hadoop.mr.fulljoin.reduce;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 组合key
 */
public class ComboKey implements WritableComparable<ComboKey> {
	//0-customer  1-order
	private int type;
	private int cid;
	private int oid;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public int compareTo(ComboKey o) {
		int o_type = o.getType() ;
		int o_cid = o.getCid() ;
		int o_oid = o.getOid() ;

//		//判断类型是否相同
//		if(type == o_type){
//			//都是客户
//			if(type == 0){
//				return cid - o_cid ;
//			}
//			else{
//				//是否是同一客户的订单
//				if(cid == o_cid){
//					return oid - o_oid ;
//				}
//				else{
//					return cid - o_cid ;
//				}
//			}
//		}
//		//不同
//		else{
//			//customer -> order
//			if(type == 0){
//				if(o_cid == cid){
//					return -1 ;
//				}
//				else{
//					return cid - o_cid ;
//				}
//			}
//			//order -> customer
//			else {
//				if(cid == o_cid){
//					return 1 ;
//				}
//				else{
//					return cid - o_cid ;
//				}
//			}
//		}

		//
		if(type == 0 && o_type ==0){
			return cid - o_cid ;
		}
		else if(type == 0 && o_type == 1){
			if(cid == o_cid){
				return -1 ;
			}
			else{
				return cid - o_cid ;
			}
		}
		else if(type == 1 && o_type == 0){
			if(cid == o_cid){
				return 1 ;
			}
			else{
				return  cid - o_cid ;
			}
		}
		else{
			if(cid == o_cid){
				return oid - o_oid ;
			}
			else{
				return cid - o_cid ;
			}
		}
	}

	public void write(DataOutput out) throws IOException {
		out.writeInt(type);
		out.writeInt(cid);
		out.writeInt(oid);

	}

	public void readFields(DataInput in) throws IOException {
		this.type = in.readInt();
		this.cid = in.readInt();
		this.oid = in.readInt() ;
	}

}

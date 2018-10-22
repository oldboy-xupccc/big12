package com.oldboy.java.test;

import com.oldboy.java.domain.MyKey;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Administrator on 2018/9/6.
 */
public class TestTreeMap {

	/**
	 *
	 */
	@Test
	public void testNewTree(){
		Map<MyKey, String > map = new TreeMap(new Comparator() {
			public int compare(Object o1, Object o2) {
				MyKey k1 = (MyKey) o1;
				MyKey k2 = (MyKey) o2;
				return-( k1.n - k2.n);
			}
		}) ;
		map.put(new MyKey(1) , "tom1") ;
		map.put(new MyKey(2) , "tom1") ;
		map.put(new MyKey(3), "tom1") ;

	}

	public static void travelMap(Map map){
		for(Object obj : map.entrySet()){
			Map.Entry e = (Map.Entry) obj;
			Object key = e.getKey() ;
			Object value = e.getValue();
			System.out.println(key + " : " + value);
		}
	}

	@Test
	public void testMidTravel() throws Exception {
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1 , "tom1");
		map.put(2 , "tom1");
		map.put(3 , "tom1");
		map.put(4 , "tom1");
		map.put(5 , "tom1");
		map.put(6 , "tom1");
		map.put(7 , "tom1");
		map.put(8 , "tom1");

		List<Map.Entry> list = new ArrayList<Map.Entry>() ;
		list.add(getRoot(map));
		midOrderTravel( 1,list );
	}

	public static Map.Entry getRoot(TreeMap map) throws Exception {
		Field f = TreeMap.class.getDeclaredField("root") ;
		f.setAccessible(true);
		Object o = f.get(map) ;
		return (Map.Entry) o;
	}

	//得到e节点的k值
	public static Object getKey(Map.Entry e) throws Exception {
		Field f = e.getClass().getDeclaredField("key") ;
		f.setAccessible(true);
		Object o = f.get(e) ;
		return o;
	}
	//得到e节点的k值
	public static String getColor(Map.Entry e) throws Exception {
		Field f = e.getClass().getDeclaredField("color") ;
		f.setAccessible(true);
		Object o = f.get(e) ;
		return o.toString();
	}

	public static Map.Entry getLeft(Map.Entry e) throws Exception {
		Field f = e.getClass().getDeclaredField("left") ;
		f.setAccessible(true);
		Object o = f.get(e) ;
		return (Map.Entry) o;
	}

	public static Object getLeftKey(Map.Entry e) throws Exception {
		Field f = e.getClass().getDeclaredField("left") ;
		f.setAccessible(true);
		Object o = f.get(e) ;
		if(o != null){
			Field k = o.getClass().getDeclaredField("key") ;
			k.setAccessible(true);
			return k.get(o) ;
		}
		return null;
	}

	public static Map.Entry getRight(Map.Entry e) throws Exception {
		Field f = e.getClass().getDeclaredField("right") ;
		f.setAccessible(true);
		Object o = f.get(e) ;
		return (Map.Entry) o;
	}

	public static Object getRightKey(Map.Entry e) throws Exception {
		Field f = e.getClass().getDeclaredField("right");
		f.setAccessible(true);
		Object o = f.get(e);
		if (o != null) {
			Field k = o.getClass().getDeclaredField("key");
			k.setAccessible(true);
			return k.get(o);
		}
		return null;
	}

	public static void preOrderTravel(Map.Entry e) throws Exception {
		if(e != null){
			System.out.println(getKey(e));
			preOrderTravel(getLeft(e)) ;
			preOrderTravel(getRight(e)) ;
		}
	}

	public static void midOrderTravel(int level ,List<Map.Entry> list) throws Exception {
		List<Map.Entry> sublist = new ArrayList<Map.Entry>() ;
		if(!list.isEmpty()){
			System.out.print(level + " ==> ");
			for(Map.Entry e : list){
				Object key = getKey(e) ;
				String red = getColor(e) ;
				System.out.print(String.format("(%d:%s)", key , red)) ;
				Map.Entry left = getLeft(e);
				if(left != null)
					sublist.add(left) ;
				Map.Entry right = getRight(e);
				if(right != null)
					sublist.add(right) ;
			}
			System.out.println();
			midOrderTravel(level + 1 , sublist);
		}
	}


	@Test
	public void testSeed(){
		System.out.println(new Random(1000).nextFloat());
		System.out.println(new Random(1000).nextFloat());
		System.out.println(new Random(1000).nextFloat());
		System.out.println(new Random(1000).nextFloat());
	}


}

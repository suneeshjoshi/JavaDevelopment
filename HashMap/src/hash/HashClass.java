package hash;

import java.util.HashMap;

public class HashClass {

	HashMap v = null;
	
	public HashClass(){
		System.out.println("CTOR");
		v= new HashMap();
	}
	
	public void addKey(Integer key , String value){
		v.put(key, value);
	}
	
	public String getValue(String key){
		return (String) ( v.get(key) ) ;
	}


}

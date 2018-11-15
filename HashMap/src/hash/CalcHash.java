package hash;
import java.lang.reflect.Method;

import hash.HashClass;

public class CalcHash {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashClass obj = new HashClass();
		
		String ftseKey="12345";
		String indexCode="EMTX_AG_G";
		String value="123.98723";
		StringBuffer combinedKey=new StringBuffer(ftseKey).append(indexCode);
		 
		
		obj.addKey( combinedKey.hashCode(), value);
		
		System.out.println(obj.v.keySet());
		
		Class reflectionObj = HashClass.class;
		Method[] methodArray= reflectionObj.getMethods();
		
		System.out.println(reflectionObj.getSuperclass());
		
		for(Method m : methodArray){
			Class[] paramTypes= m.getParameterTypes();
			System.out.println( m.getName() );

			for(Class p : paramTypes){
				System.out.println( "\t"+p.getName() );
			
			}

			
		}
		
	}

}

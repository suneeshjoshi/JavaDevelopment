package syncmap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class Producer extends Thread {
	  Map<String, Integer> sm;
	  
	  Producer(Map<String, Integer> sm, String name){
		  this.sm=sm;
		  setName("PRODUCER:"+name);
	  }
	  
    public void run() {
    	System.out.println(this.getName()+" :: Producer::run() : Starting ");
       	 try {
       		 int i=0;
       		 while(i<10){
       			 System.out.println(this.getName()+" :: adding ... "+i);
       			 sm.putIfAbsent(this.getName(), i);
   			 }
       			 i++;
			} catch (Exception e) {
					e.printStackTrace();
			}
        }
   }

  class Consumer extends Thread{
	  Map<String, Integer> sm;
	  
	  Consumer(Map<String, Integer> sm){
		  this.sm=sm;
		  setName("CONSUMER");
	  }
	  
    public void run() {
			System.out.println(this.getName()+" :: Consumer::run() : Starting ");

         	 try {
           		 while(true){
//      				System.out.println("     -------     "+this.getName()+" :: "+sq.take());
           			
           			for(String key:sm.keySet()){
           				System.out.println("     -------     "+this.getName()+" :: "+key +" :: value = "+sm.get(key));
           				sm.remove(key);
           			}
           			
           		 }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
  }

public class SyncMap2 {

	   public static void main(String... args) {
			Map<String, Integer> sm = Collections.synchronizedMap(new HashMap<String, Integer>());
//		   SynchronousQueue<String> sq = new SynchronousQueue<String>();  
			System.out.println("main() : Starting ");
			
			new Consumer(sm).start();
			
			new Producer(sm,"A").start();
			new Producer(sm,"B").start();
			new Producer(sm,"C").start();
			new Producer(sm,"D").start();
			new Producer(sm,"E").start();
			new Producer(sm,"F").start();
			
			System.out.println("main() : End ");
	   }
}

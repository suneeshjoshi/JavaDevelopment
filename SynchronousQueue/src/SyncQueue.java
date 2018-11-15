import java.util.concurrent.SynchronousQueue;

class Producer extends Thread {
	  SynchronousQueue<String> sq;
	  String data;
	  
	  Producer(SynchronousQueue<String> sq, String data){
		  this.sq=sq;
		  this.data=data;
		  setName("PRODUCER:"+data);
	  }
	  
    public void run() {
    	System.out.println(this.getName()+" :: Producer::run() : Starting ");
       	 try {
       		 int i=0;
       		 while(i<20){
       			 String data=new Integer(i).toString();
       			 if(!sq.contains(data)){
	       			 System.out.println(this.getName()+" :: adding ... "+i);
					 sq.put(data);
       			 }
       			 else{
       				 System.out.println(this.getName()+" :: ======= NOT adding "+i+". ALREADY PRESENT");
       			 }
       			 i++;
       		 }
			} catch (InterruptedException e) {
					e.printStackTrace();
			}
        }
   }

  class Consumer extends Thread{
	  SynchronousQueue<String> sq;
	  
	  Consumer(SynchronousQueue<String> sq){
		  this.sq=sq;
		  setName("CONSUMER");
	  }
	  
    public void run() {
			System.out.println(this.getName()+" :: Consumer::run() : Starting ");

         	 try {
           		 while(true){
     				System.out.println("     -------     "+this.getName()+" :: "+sq.take());
           		 }
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
  }

public class SyncQueue {

	   public static void main(String... args) {
			SynchronousQueue<String> sq = new SynchronousQueue<String>(true);  
			System.out.println("main() : Starting ");
			
			new Consumer(sq).start();
			
			new Producer(sq,"A").start();
			new Producer(sq,"B").start();
			new Producer(sq,"C").start();
			new Producer(sq,"D").start();
			new Producer(sq,"E").start();
			new Producer(sq,"F").start();
			
			System.out.println("main() : End ");
	   }
}

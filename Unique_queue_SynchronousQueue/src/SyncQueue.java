import java.util.concurrent.SynchronousQueue;


class SJSynchronousQueue<T> extends SynchronousQueue{
	private SynchronousQueue<T> sq= new SynchronousQueue<T>();
	
	SJSynchronousQueue(){
	}
	
	synchronized boolean containsElement(Object o){
		 return sq.contains(o);
	}
	  
}

class Producer extends Thread {
//	  SynchronousQueue<String> sq;
	  SJSynchronousQueue<String> sq;
	  int seq=0;
	  
	  Producer(SJSynchronousQueue<String> sq, String name, int seq){
		  this.sq=sq;
		  setName("PRODUCER:"+name);
		  this.seq=seq;
	  }
	  
    public void run() {
    	System.out.println(this.getName()+" :: Producer::run() : Starting ");
       	 try {
       		 int i=0;
       		 while(i<3){
       		
       			 String data=new Integer((seq*10+i)).toString();
       			 if(!sq.containsElement(data)){
	       			 System.out.println(this.getName()+" :: adding ... "+data);
					 sq.put(data);
       			 }
       			 else{
       				 System.out.println(this.getName()+" :: ======= NOT adding "+i+". ALREADY PRESENT");
       			 }
       			 i++;
       		 }
       		 
       		 System.out.println(this.getName()+"ALL WORK DONE.");
       		 //return;
			} catch (InterruptedException e) {
					e.printStackTrace();
			}
        }
   }

  class Consumer extends Thread{
	  SJSynchronousQueue<String> sq;
	  
	  Consumer(SJSynchronousQueue<String> sq){
		  this.sq=sq;
		  setName("CONSUMER");
	  }
	  
    public void run() {
			System.out.println(this.getName()+" :: Consumer::run() : Starting ");

         	 try {
           		 while(true){
     				System.out.println("     -------     "+this.getName()+" :: "+sq.take());
     				Thread.sleep(1000);
           		 }
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
  }

public class SyncQueue {

	   public static void main(String... args) {
			SJSynchronousQueue<String> sq = new SJSynchronousQueue<String>();  
			System.out.println("main() : Starting ");
			
			System.out.println("main() : Before Starting any threads , Number of active threads = "+Thread.activeCount());
			
			new Consumer(sq).start();
			
			System.out.println("main() : After Consumer Thread, Number of active threads = "+Thread.activeCount());
			for(int i=0;i<3;i++){
				new Producer(sq,"A"+i,i).start();
				System.out.println("main() : After starting "+i+" Producer Threads , Number of active threads = "+Thread.activeCount());
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("main() : Number of active threads = "+Thread.activeCount());
			System.out.println("main() : End ");
	   }
}

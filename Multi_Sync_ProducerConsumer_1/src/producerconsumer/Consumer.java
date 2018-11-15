package producerconsumer;

import java.util.LinkedList;

public class Consumer extends Thread{
	LinkedList<Integer> queue;
	int id;
	
	Consumer(LinkedList<Integer> queue, String id){
		this.queue = queue;
		this.setName(id);
	}
	
	public void run(){
		 while ( true ) {
            try {
                int work = 0;
 
                synchronized ( queue ) {
                    while ( queue.isEmpty() )
                        queue.wait();
                     
                    // Get the next work item off of the queue
                    work = queue.remove();
                }
 
                // Process the work item
            doWork(work);
	        }
	        catch ( InterruptedException ie ) {
	            break;  // Terminate
	        }
	    }
	}
	
	void doWork(int prefix){
		System.out.println(this.getName()+" :: process product = "+prefix);
	}
	
}

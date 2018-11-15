/**
 * 
 */
package producerconsumer;

import java.util.LinkedList;

/**
 * @author sunee
 *
 */
public class Producer extends Thread{
	LinkedList<Integer> queue;
	int id=0;
	Producer(LinkedList<Integer> queue, String id){
		this.queue = queue;
		this.setName(id);
	}
	
	public void run(){
		int i=0;
		while(true){
			System.out.println("Producer "+this.id+" Putting value ... ("+i+")");
			queue.add(i++);
			notify();
		}
	}
}

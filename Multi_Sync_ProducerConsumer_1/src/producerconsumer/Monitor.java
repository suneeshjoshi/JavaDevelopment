package producerconsumer;

import java.util.LinkedList;

// Here this class Q works as a monitor. 
public class Monitor {
	int n;
	private LinkedList<Integer> q = new LinkedList<Integer>();
	
	
	boolean valueSet=false;
	
	synchronized int get() {
		
/*		while(!valueSet){
			try {
				// this wait() will cause the execution to stop, until the Producer puts in a value and sets the value of valueSet to true.
				// upon which the value is taken and a notify() is done to tell the Producer that the value has been consumed.
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		*/System.out.println("GET :: Linkedlist size = "+this.q.size());

		int n = this.q.pop();
		System.out.println("Got : "+n);
		
		valueSet=false;
//		notify();
		return n;
	}
	
	synchronized void put(int n){
/*		while(valueSet){
			try {
				// this wait() will cause the execution to stop, until the Consumer consumes the value and sets the value of valueSet to false.
				// upon which a new value is put into the queue and a notify() is done to tell the Consumer that a new value is ready to be consumed.
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	*/	
		System.out.println("PUT :: Linkedlist size = "+this.q.size());
		this.q.add(n);
		valueSet=true;
		System.out.println("Put : "+n);
//		notify();
	}
}

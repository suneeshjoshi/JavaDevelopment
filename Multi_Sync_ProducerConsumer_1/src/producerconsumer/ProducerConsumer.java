package producerconsumer;

import java.util.LinkedList;

public class ProducerConsumer {

	public static void main(String args[]){
		LinkedList<Integer> q = new LinkedList<Integer>();
		new Producer(q,"A").start();
		new Producer(q,"B").start();
		new Producer(q,"C").start();
		new Producer(q,"D").start();
		new Consumer(q,"E").start();
		
		System.out.println("Control+C to stop.");
	}
}

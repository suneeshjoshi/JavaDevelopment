package producerconsumer;

public class ProducerConsumer {

	public static void main(String args[]){
		Q q = new Q();
		new Producer(q);
		new Consumer(q);
		
		System.out.println("Control+C to stop.");
	}
}

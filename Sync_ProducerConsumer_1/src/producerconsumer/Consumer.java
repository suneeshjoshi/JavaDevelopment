package producerconsumer;

public class Consumer extends Thread{
	Q q;
	
	Consumer(Q q){
		this.q = q;
		start();
	}
	
	public void run(){
		while(true){
			q.get();
		}
	}
}

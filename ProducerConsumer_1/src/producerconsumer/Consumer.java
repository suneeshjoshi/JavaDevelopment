package producerconsumer;

public class Consumer extends Thread{
	Q q;
	
	Consumer(Q q){
		this.q = q;
		start();
	}
	
	public void run(){
		int i=0;
		while(true){
			q.get();

			if(i++>100)
				break;
		}
	}
}

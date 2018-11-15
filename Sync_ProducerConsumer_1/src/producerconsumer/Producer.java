/**
 * 
 */
package producerconsumer;

/**
 * @author sunee
 *
 */
public class Producer extends Thread{
	Q q;
	Producer(Q q){
		this.q = q;
		start();
	}
	
	public void run(){
		int i=0;
		while(true){
			q.put(i++);
		}
	}
}

package java8;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Program1 {

	static boolean mapFunc(int i) {
		return i>3;
	}
	
	public static void main(String[] args) {

//		HashMap hm = new HashMap();
		
		List<Integer> l = Arrays.asList(1,2,3,4,5,6);
		
		List collect1 = (List) l.stream().map(f->mapFunc(f)==true).collect(Collectors.toList());

		List collect2 = (List) l.stream().filter(f->f%3==0).collect(Collectors.toList());
		
		collect1.forEach(i->System.out.println(i));
		collect2.forEach(i->System.out.println(i));
		
		l.forEach(i->System.out.println(i));
		
		Random r = new Random();
		r.ints(2000).parallel().filter(f->f%7==0).forEach(f->System.out.println(f));
//		
//		ExecutorService exec = Executors.newFixedThreadPool(100);
//		System.out.println("START");
//		
//		for(int i=0;i<100;i++) {
//		exec.submit(()->{
//			int s = ( new Random().nextInt(10000)%5)* 10000;
//			System.out.println(Thread.currentThread().getName()+" - sleeping for "+s);
//			try {
//				Thread.sleep(s) ;
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});
//		}
//
//		exec.shutdown();
//		
//		System.out.println("FINISH");

		ExecutorService exec = Executors.newCachedThreadPool();
		System.out.println("START");
		
		for(int i=0;i<100;i++) {
		exec.submit(()->{
			int s = ( new Random().nextInt(10000)%5)* 10000;
			System.out.println(Thread.currentThread().getName()+" - sleeping for "+s);
			try {
				Thread.sleep(s) ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		}

//		exec.shutdown();
		
		System.out.println("FINISH");

	
	}

}

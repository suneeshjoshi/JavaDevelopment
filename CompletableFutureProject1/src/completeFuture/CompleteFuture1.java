package completeFuture;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


	
public class CompleteFuture1{ 

	void function1() {
		CompletableFuture cf = CompletableFuture.supplyAsync(() -> {
			myPrint("In function 1 : START ");
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(5));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myPrint("In function 1 : END ");
			return 2;}, Executors.newFixedThreadPool(30))
				.thenApplyAsync(f->{
					myPrint("In function 2 : START ");

					try {
						Thread.sleep(TimeUnit.SECONDS.toMillis(3));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					myPrint("In function 2 : END ");
					return f*10;}, Executors.newCachedThreadPool());
							
		try {
			System.out.println(cf.get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	void myPrint(String msg) {
		System.out.println(Thread.currentThread().getName()+" : "+LocalDate.now().now()+" : "+msg);
	}
	
	public static void main(String[] args) {
		CompleteFuture1 obj = new CompleteFuture1();

		obj.myPrint("START");
//		obj.function1();
		
		

		obj.myPrint("---------------------");
			
		Thread t = new Thread( () ->  {System.out.println("I am Functiona interface implementation on run() ");} );
		t.start();
		
		List<Integer> l = Arrays.asList(1,2,3,4,524,24242,452,9,14,5,215);
		List<Integer> res = l.parallelStream().filter(f->f%3==0).collect(Collectors.toList());
		res.stream().forEach(e -> System.out.println(e));
		
		obj.myPrint("END");
		
	}
	
}

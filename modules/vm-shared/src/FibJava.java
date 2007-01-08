
public class FibJava {

	
	public int fib(int n)
	{
		if (2 > n) {
			return n;
		} else {
			return fib(n-2) + fib(n-1);
		}
		
	}
	
	public void test(int n)
	{
		long before = System.currentTimeMillis();
		fib(n);
		long after = System.currentTimeMillis();
		
		System.out.println("time spent: "+((after-before)/1000.0));
	}
	
	public static void main(String[] args) {
		FibJava fib = new FibJava();
		fib.test(34);
		fib.test(34);
		fib.test(34);
		fib.test(34);
		fib.test(34);
		fib.test(34);
		fib.test(34);
	}
	
}

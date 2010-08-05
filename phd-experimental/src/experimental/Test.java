package experimental;



public class Test {

	public static void main(String[] args) {
		System.out.println(testMethod(2, 1, 1));
		System.out.println(testMethod(3, 1, 1));
		System.out.println(testMethod(4, 1, 1));
		System.out.println(testMethod(5, 1, 1));
		System.out.println(testMethod(6, 1, 1));
		System.out.println(testMethod(7, 1, 1));
		System.out.println(testMethod(8, 1, 1));
		System.out.println(testMethod(9, 1, 1));
	}
	
	public static int testMethod(int n, int n0, int n1) {
		n = --n1;
		int var0 = n1;
		for (int var1 = 0, var2=0; (var1<var0 && var1<100); var1++,var2=var1) {
		    int var3 = (n0 * --n1);
		    for (int var4 = 0, var5 = 0; (var4<var3 && var4<100); var4++,var5=var4) {
		        n = --var5;
		    }
		}
		boolean var6 = ((true && false) ^ true);
		boolean var7 = var6;
		int var8 = n0;
		return n;
	}
	
}

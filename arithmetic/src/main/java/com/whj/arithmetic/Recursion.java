package com.whj.arithmetic;
/**
 * 递归
 * @author Administrator
 *
 */
public class Recursion {

	public static void main(String[] args) {
		String str = "aaw";
		char[] chars = str.toCharArray();
		reverseStr(chars,0);
	}
	
	/**
	 * 用递归实现n的阶乘   n! = 1 * 2 * 3 * 4 * 5
	 * @param n
	 * @return
	 */
	public static int nJC(int n){
		if(n == 0) {
			return 1;
		}
		
		// f(n) = n * (n - 1)
		return n * nJC( n - 1 );
	}
	
	
	
	public static char reverseStr(char[] str,int index){
		if(index == str.length - 1){
			return str[index];
		}
		
		char c = reverseStr(str,index + 1);
		
		System.out.print(c);
		return c;
	}
}

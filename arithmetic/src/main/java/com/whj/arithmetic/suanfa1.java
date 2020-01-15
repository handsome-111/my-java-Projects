package com.whj.arithmetic;

import java.util.ArrayList;
import java.util.List;

/**
 * 二分法查找
 * @author Administrator
 *
 */
public class suanfa1 {
	public static void main(String[] args) {
		int[] list = new int[10];
		int guess = 2;
		binarySearch(list,guess);
	}
	
	public static void binarySearch(int[] list , int guess){
		int low = 0;
		int high = list.length - 1;		
		
		int count = 0;
		int mid;
		
		out:
		while (high >= low){
			mid = (high + low) / 2;
			count++;
			
			if ( mid == guess) {
				System.out.println("找到了元素:" + mid  + "一共查找了" + count + "次");
				break out; 
			}
			
			/**
			 * 如果猜大了，则修改最大值，如果猜小了，则修改最小值
			 */
			if ( mid > guess) {
				high = mid - 1;
				continue out;
			} else {
				low = mid + 1;
			}
	
		}
	}
}

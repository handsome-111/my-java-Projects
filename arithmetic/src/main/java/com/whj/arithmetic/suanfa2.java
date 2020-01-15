package com.whj.arithmetic;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 选择排序，讲队列从小到大排序
 * @author Administrator
 *
 */
public class suanfa2 {

	public static String selectionSort(int[] list){		
		
		for( int i = 0 ; i < list.length; i++ ) {
			
			 //最小数的索引
			int minIndex = i;
			
			for( int j = i; j < list.length; j++) {
				//找到最小数，并记录最小数的索引
				if(list[minIndex] > list[j]) {
					minIndex = j;
				}
			}
			
			//交换符合条件的数
			if(list[i] > list[minIndex] ){
				int temp = list[minIndex];
				list[minIndex] = list[i];
				list[i] = temp;
				
			}
		}
		
		return Arrays.toString(list);
	}
	
	public static void main(String[] args) {
		int[] a = {0,6,45,98,97,789};
		System.out.println(selectionSort(a));
	}
}

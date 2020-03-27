package com.whj.datastructure.arithmetic;

import java.util.Arrays;

public class InsertSort {
	public static void main(String[] args) {
		int[] arr = {546,4,44,4,5,45,489,2};
		
		/**
		 * 迭代要插入的数，即从1开始，而不是0
		 */
		for( int i = 1; i < arr.length; i++) {
			
		
			int insertValue = arr[i];		//要插入的值
			int prevIndex = i - 1; 			//要插入的前一个索引，因为这个索引会不断地向前移动
		
			//逐步地将一个个的值后移
			while(prevIndex >= 0 && insertValue < arr[prevIndex]) { 
				arr[prevIndex+1] = arr[prevIndex];		//将值后移
				prevIndex --;
			}
			//当条件不满足的时候，即前一个索引的值 是小于要插入的值，则前一个索引+1，就是要插入的位置，将值 “插入”即可
			arr[prevIndex + 1] = insertValue;

		}
		System.out.println(Arrays.toString(arr));
	}
}

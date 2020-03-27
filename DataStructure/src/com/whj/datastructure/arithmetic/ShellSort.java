package com.whj.datastructure.arithmetic;

import java.util.Arrays;

/**
 * 希尔排序
 * 
 * @author Administrator
 *
 */
public class ShellSort {
	public static void main(String[] args) {
		int[] arr = {8,9,1,7,4,897,895,45,4};
		/**
		 * gap:距离，步数，增量，表示两个元素之间的步数，偏移量
		 * 随着每次增量的不同,分组后的数组也会变化
		 */
		for(int gap = arr.length/2; gap > 0; gap /=2 ) {
			
			//进行插入排序,来排序每一组
			//是将数据分成了gap组，如第一组：1,3,5,7  第二组 :2,4,6,8 
			//int i = gap，表示从第gap个元素开始，就像不是0,而是1一样
			for(int i = gap; i < arr.length; i++) {
				//遍历各组中所有的元素，每组的元素 之间都有gap（距离）,所以要通过gap来遍历，如有一组： 1,3,5,7
				int insertValue = arr[i];
				int prevIndex = i - gap;
				
				//满足条件的，逐步地将一个个的值后移
				while(prevIndex >=0 && insertValue < arr[prevIndex]) {
					arr[prevIndex + gap] = arr[prevIndex];
					prevIndex -= gap;
				}
				arr[prevIndex + gap] = insertValue;
				
			}
		}
		System.out.println(Arrays.toString(arr));
	}		
}

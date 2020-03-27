package com.whj.datastructure.arithmetic;

import java.util.Arrays;

/**
 * 快速排序
 * @author Administrator
 *
 */
public class QuickSort {
	public static void main(String[] args) {
		int arr[] = {1,4,5645,7489,789,754,5641,561,231,57,897,65415,64,98795411,65465,4};
		quickSort(arr,0,arr.length-1);
		System.out.println(Arrays.toString(arr));
	}
	public static void quickSort(int[] arr,int l,int r) {
		int left = l;
		int right = r;
		
		int pivot = (left + right) / 2;	//中间的索引,并不是大小折中的值
		
		//在不断移动left和right时,如果 left 大于或者等于right的时候,就结束了
		while( left < right ) {
			
			/**
			 * 1.移动索引
			 */
			//如果左边的值大于等于中间值,退出去和右边的交换
			while(arr[left] < arr[pivot]) {
				left++;		//继续右移
			}
			//如果右边的值小于等于中间值,退出去和左边的交换
			while(arr[right] > arr[pivot]) {
				right--;	//继续左移
			}
			
			if(left >= right) {
				break;
			}
			
			/**
			 * 2.左边与右边的值交换
			 */
			//将左边和右边的值交换，并不是对称交换喔
			int temp = arr[left];
			arr[left] = arr[right];
			arr[right] = temp;
			
			
			//交换完后，如果发现arr[left] 等于中间值，说明左边的已经遍历 完了,但是右边的还需要继续遍历,防止死循环
			//如 arr = {1,1,1}
			//left = 0,privot = 1,right=2
			//索引不用移动，进行交换
			//交换之后还是{1,1,1}
			//在没有 下面两条if的情况下 会一直死循环,左边和右边的值一直在调换,并且索引不会移动 
			if(arr[left] == arr[pivot] ) {
				right --;
			}
			
			if(arr[right] == arr[pivot]) {
				left ++;
			}
		}
		
		if( left == right ) {
			left++;
			right--;
		}
		//向左递归,如果最开始的索引还是小于移动的right索引,则继续递归排序
		if(l < right) {
			quickSort(arr,l,right);		//最开始的l索引,和移动的right索引
		}
		if(r > left){ 
			quickSort(arr,left,r);		//最开始的r索引,和移动的leftright索引
		}
	}
}

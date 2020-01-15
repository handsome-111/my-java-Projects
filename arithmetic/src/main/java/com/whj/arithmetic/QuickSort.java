package com.whj.arithmetic;

import java.util.Arrays;

public class QuickSort {
	public static void main(String[] args) {
		int[] a = {14,4,987,895,45,41,4564,54,54,9749,4,4,63,132,4164,54};
		int[] b = quickSort(a,0,a.length - 1);
		System.out.println(Arrays.toString(b) + ",");
	}
	
	public static int[] quickSort(int[] arr,int left,int right){
		if( left < right ) {
				int base = partition(arr,left,right);
			quickSort(arr,left,base - 1);
			quickSort(arr,base + 1 , right);
		}
		return arr;
	}
	
	public static int partition(int[] arr, int left, int right){
		
		int base = arr[left];
		
		
		while(left != right) {
			//从左边开始找比base大的值，到时候要放到右边去
			while(arr[left] > base && left < right) {
				left ++;
			}
			
			//从右边找比base小的值，到时候要放到左边去
			while(arr[right] <= base && right > left) {
				right --;
			}
			
			
			//如果都找到了，则互换值，把大的值放右边，把小的值放左边
			if(right > left ){
				int temp = arr[right];
				arr[right] = arr[left];
				arr[left] = temp; 
			}
			System.out.println(left + "," + right);
			System.out.println(arr[left] + "," + arr[right]);
			//如果左边的指针撞到了右边的指针，则不互换，直接结束全部循环。
		}
		System.out.println("出来");
		
		//base和 right的值交换
		/*int temp = arr[right];				
		arr[right] = arr[base];
		arr[base] = temp;*/
		arr[left] = arr[right];
		arr[right] = base;
		
		//此时 left = right,完成了一次排序
		System.out.println(right);
		return right;
	}
}

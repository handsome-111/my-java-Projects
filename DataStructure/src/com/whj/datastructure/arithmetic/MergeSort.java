package com.whj.datastructure.arithmetic;

import java.util.Arrays;
/**
 * 归并排序
 * @author Administrator
 *
 */
public class MergeSort {
	public static void main(String[] args) {
		int[] arr = {456,456,459,7494,541,4,574,456,4,456,156,4897,84,51,524,6543,514};
		int[] temp = new int[arr.length];
 		mergeSort(arr,0,arr.length-1,temp);
 		System.out.println(Arrays.toString(arr));
	}
	//分+治的方法
	public static void  mergeSort(int arr[],int left,int right,int[] temp) {
		
		if(left < right ) {
			int mid = (left + right) / 2;	//中间的索引
			//向左递归分解
			mergeSort(arr,left,mid,temp);
			//向右递归分解
			mergeSort(arr,mid+1,right,temp);
			//合并
			merge(arr,left,mid,right,temp);
		}
		
	}
	
	//合并的方法，即治的方法
	public static void merge(int[] arr,int left,int mid,int right,int[] temp) {
		int i = left;		//初始化i，左边的有序序列初始索引
		int j = mid + 1;	//初始化j，右边的有序序列初始索引
		int t = 0;		//指向temp数组的当前索引
		
		/**
		 * 1.先处理两边有序列表的一边
		 */
		//先把左右两边的数据按照规则填充到temp
		//直到左右两边的有序序列，有一边处理完毕为止
		while( i <= mid && j <= right) {
			
			if( arr[i] <= arr[j] ) {
				temp[t] = arr[i];	//将i和j中最小的拷贝到temp里
				t++;	//后移
				i++;	//拷贝过的后移
			}
			
			if(arr[i] > arr[j]) {
				temp[t] = arr[j];	//将i和j中最小的拷贝到temp里
				t++;	//右移
				j++;	//拷贝过的后移
			}
		}
		/**
		 * 2.处理有序列表的另一边
		 */
		while( i<= mid) {
			temp[t] = arr[i];
			t++;
			i++;
		}
		
		while( j<= right) {
			temp[t] = arr[j];
			t++;
			j++;
		}
		/**
		 * 3.把temp数组的元素拷贝到arr
		 * 注意,每次拷贝不是拷贝所有
		 */
		t = 0;
		int tempLeft = left;		//拆分出来后的left,即传递过来的left
		while(tempLeft <= right ) {
			arr[tempLeft] = temp[t];
			t++;
			tempLeft++;
		}
	}
}

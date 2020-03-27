package com.whj.datastructure.arithmetic;

public class RadixSort {
	public static void main(String[] args) {
		
	}
	
	public static void radixSort(int[] arr) {
		//1.定义二维数组，表示10个桶，每个桶就是一个一维数组
		//2.为了防止放入数据时，数据溢出，则每个一维数组（桶），大小为arr.length
		int[][] bucket = new int[10][arr.length];
		
		//为了记录每个桶中，实际放了多少个数据，我们定义一个一维数组来记录各个桶每次放入的数据个数
		int[] bucketElementCounts = new int[10];
		for(int j = 0; j < arr.length;j++) {
			//取出每个元素的个位数的值
			int digitOfElement = arr[j]%10;
			//放入对应的桶中
			bucket[digitOfElement][bucketElementCounts[digitOfElement]] = arr[j];
		}
	}
}

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
			//����߿�ʼ�ұ�base���ֵ����ʱ��Ҫ�ŵ��ұ�ȥ
			while(arr[left] > base && left < right) {
				left ++;
			}
			
			//���ұ��ұ�baseС��ֵ����ʱ��Ҫ�ŵ����ȥ
			while(arr[right] <= base && right > left) {
				right --;
			}
			
			
			//������ҵ��ˣ��򻥻�ֵ���Ѵ��ֵ���ұߣ���С��ֵ�����
			if(right > left ){
				int temp = arr[right];
				arr[right] = arr[left];
				arr[left] = temp; 
			}
			System.out.println(left + "," + right);
			System.out.println(arr[left] + "," + arr[right]);
			//�����ߵ�ָ��ײ�����ұߵ�ָ�룬�򲻻�����ֱ�ӽ���ȫ��ѭ����
		}
		System.out.println("����");
		
		//base�� right��ֵ����
		/*int temp = arr[right];				
		arr[right] = arr[base];
		arr[base] = temp;*/
		arr[left] = arr[right];
		arr[right] = base;
		
		//��ʱ left = right,�����һ������
		System.out.println(right);
		return right;
	}
}

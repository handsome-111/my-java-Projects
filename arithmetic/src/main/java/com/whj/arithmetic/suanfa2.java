package com.whj.arithmetic;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * ѡ�����򣬽����д�С��������
 * @author Administrator
 *
 */
public class suanfa2 {

	public static String selectionSort(int[] list){		
		
		for( int i = 0 ; i < list.length; i++ ) {
			
			 //��С��������
			int minIndex = i;
			
			for( int j = i; j < list.length; j++) {
				//�ҵ���С��������¼��С��������
				if(list[minIndex] > list[j]) {
					minIndex = j;
				}
			}
			
			//����������������
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

package com.whj.arithmetic;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ַ�����
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
				System.out.println("�ҵ���Ԫ��:" + mid  + "һ��������" + count + "��");
				break out; 
			}
			
			/**
			 * ����´��ˣ����޸����ֵ�������С�ˣ����޸���Сֵ
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

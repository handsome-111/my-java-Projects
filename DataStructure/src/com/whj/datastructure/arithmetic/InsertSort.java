package com.whj.datastructure.arithmetic;

import java.util.Arrays;

public class InsertSort {
	public static void main(String[] args) {
		int[] arr = {546,4,44,4,5,45,489,2};
		
		/**
		 * ����Ҫ�������������1��ʼ��������0
		 */
		for( int i = 1; i < arr.length; i++) {
			
		
			int insertValue = arr[i];		//Ҫ�����ֵ
			int prevIndex = i - 1; 			//Ҫ�����ǰһ����������Ϊ��������᲻�ϵ���ǰ�ƶ�
		
			//�𲽵ؽ�һ������ֵ����
			while(prevIndex >= 0 && insertValue < arr[prevIndex]) { 
				arr[prevIndex+1] = arr[prevIndex];		//��ֵ����
				prevIndex --;
			}
			//�������������ʱ�򣬼�ǰһ��������ֵ ��С��Ҫ�����ֵ����ǰһ������+1������Ҫ�����λ�ã���ֵ �����롱����
			arr[prevIndex + 1] = insertValue;

		}
		System.out.println(Arrays.toString(arr));
	}
}

package com.whj.datastructure.arithmetic;

import java.util.Arrays;

/**
 * ϣ������
 * 
 * @author Administrator
 *
 */
public class ShellSort {
	public static void main(String[] args) {
		int[] arr = {8,9,1,7,4,897,895,45,4};
		/**
		 * gap:���룬��������������ʾ����Ԫ��֮��Ĳ�����ƫ����
		 * ����ÿ�������Ĳ�ͬ,����������Ҳ��仯
		 */
		for(int gap = arr.length/2; gap > 0; gap /=2 ) {
			
			//���в�������,������ÿһ��
			//�ǽ����ݷֳ���gap�飬���һ�飺1,3,5,7  �ڶ��� :2,4,6,8 
			//int i = gap����ʾ�ӵ�gap��Ԫ�ؿ�ʼ��������0,����1һ��
			for(int i = gap; i < arr.length; i++) {
				//�������������е�Ԫ�أ�ÿ���Ԫ�� ֮�䶼��gap�����룩,����Ҫͨ��gap������������һ�飺 1,3,5,7
				int insertValue = arr[i];
				int prevIndex = i - gap;
				
				//���������ģ��𲽵ؽ�һ������ֵ����
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

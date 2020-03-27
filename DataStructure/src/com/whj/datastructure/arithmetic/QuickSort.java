package com.whj.datastructure.arithmetic;

import java.util.Arrays;

/**
 * ��������
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
		
		int pivot = (left + right) / 2;	//�м������,�����Ǵ�С���е�ֵ
		
		//�ڲ����ƶ�left��rightʱ,��� left ���ڻ��ߵ���right��ʱ��,�ͽ�����
		while( left < right ) {
			
			/**
			 * 1.�ƶ�����
			 */
			//�����ߵ�ֵ���ڵ����м�ֵ,�˳�ȥ���ұߵĽ���
			while(arr[left] < arr[pivot]) {
				left++;		//��������
			}
			//����ұߵ�ֵС�ڵ����м�ֵ,�˳�ȥ����ߵĽ���
			while(arr[right] > arr[pivot]) {
				right--;	//��������
			}
			
			if(left >= right) {
				break;
			}
			
			/**
			 * 2.������ұߵ�ֵ����
			 */
			//����ߺ��ұߵ�ֵ�����������ǶԳƽ����
			int temp = arr[left];
			arr[left] = arr[right];
			arr[right] = temp;
			
			
			//��������������arr[left] �����м�ֵ��˵����ߵ��Ѿ����� ����,�����ұߵĻ���Ҫ��������,��ֹ��ѭ��
			//�� arr = {1,1,1}
			//left = 0,privot = 1,right=2
			//���������ƶ������н���
			//����֮����{1,1,1}
			//��û�� ��������if������� ��һֱ��ѭ��,��ߺ��ұߵ�ֵһֱ�ڵ���,�������������ƶ� 
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
		//����ݹ�,����ʼ����������С���ƶ���right����,������ݹ�����
		if(l < right) {
			quickSort(arr,l,right);		//�ʼ��l����,���ƶ���right����
		}
		if(r > left){ 
			quickSort(arr,left,r);		//�ʼ��r����,���ƶ���leftright����
		}
	}
}

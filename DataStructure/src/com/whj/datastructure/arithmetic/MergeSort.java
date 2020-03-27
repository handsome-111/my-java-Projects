package com.whj.datastructure.arithmetic;

import java.util.Arrays;
/**
 * �鲢����
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
	//��+�εķ���
	public static void  mergeSort(int arr[],int left,int right,int[] temp) {
		
		if(left < right ) {
			int mid = (left + right) / 2;	//�м������
			//����ݹ�ֽ�
			mergeSort(arr,left,mid,temp);
			//���ҵݹ�ֽ�
			mergeSort(arr,mid+1,right,temp);
			//�ϲ�
			merge(arr,left,mid,right,temp);
		}
		
	}
	
	//�ϲ��ķ��������εķ���
	public static void merge(int[] arr,int left,int mid,int right,int[] temp) {
		int i = left;		//��ʼ��i����ߵ��������г�ʼ����
		int j = mid + 1;	//��ʼ��j���ұߵ��������г�ʼ����
		int t = 0;		//ָ��temp����ĵ�ǰ����
		
		/**
		 * 1.�ȴ������������б��һ��
		 */
		//�Ȱ��������ߵ����ݰ��չ�����䵽temp
		//ֱ���������ߵ��������У���һ�ߴ������Ϊֹ
		while( i <= mid && j <= right) {
			
			if( arr[i] <= arr[j] ) {
				temp[t] = arr[i];	//��i��j����С�Ŀ�����temp��
				t++;	//����
				i++;	//�������ĺ���
			}
			
			if(arr[i] > arr[j]) {
				temp[t] = arr[j];	//��i��j����С�Ŀ�����temp��
				t++;	//����
				j++;	//�������ĺ���
			}
		}
		/**
		 * 2.���������б����һ��
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
		 * 3.��temp�����Ԫ�ؿ�����arr
		 * ע��,ÿ�ο������ǿ�������
		 */
		t = 0;
		int tempLeft = left;		//��ֳ������left,�����ݹ�����left
		while(tempLeft <= right ) {
			arr[tempLeft] = temp[t];
			t++;
			tempLeft++;
		}
	}
}

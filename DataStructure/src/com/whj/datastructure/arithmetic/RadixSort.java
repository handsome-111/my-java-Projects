package com.whj.datastructure.arithmetic;

public class RadixSort {
	public static void main(String[] args) {
		
	}
	
	public static void radixSort(int[] arr) {
		//1.�����ά���飬��ʾ10��Ͱ��ÿ��Ͱ����һ��һά����
		//2.Ϊ�˷�ֹ��������ʱ�������������ÿ��һά���飨Ͱ������СΪarr.length
		int[][] bucket = new int[10][arr.length];
		
		//Ϊ�˼�¼ÿ��Ͱ�У�ʵ�ʷ��˶��ٸ����ݣ����Ƕ���һ��һά��������¼����Ͱÿ�η�������ݸ���
		int[] bucketElementCounts = new int[10];
		for(int j = 0; j < arr.length;j++) {
			//ȡ��ÿ��Ԫ�صĸ�λ����ֵ
			int digitOfElement = arr[j]%10;
			//�����Ӧ��Ͱ��
			bucket[digitOfElement][bucketElementCounts[digitOfElement]] = arr[j];
		}
	}
}

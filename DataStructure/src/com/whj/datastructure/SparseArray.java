package com.whj.datastructure;

import java.util.Arrays;

/**
 * 稀疏数组
 * @author Administrator
 *
 */
public class SparseArray {
	public static void main(String[] args) {
		//创建一个原始的二维数组，11*11的
		//0：表示没有棋子，1：表示黑子，2：表示蓝子
		int chessArr[][] = new int[11][11];
		chessArr[1][2] = 1;
		chessArr[2][3] = 2;

		//输出原始的二维数组
		for(int[] a : chessArr){
			System.out.println(Arrays.toString(a));
		}
		
		/**
		 * 将二维数组转换为稀疏数组
		 * 1.先遍历二维数组，得到非0的个数
		 * 
		 */
		int sum = 0;
		for( int[] arr : chessArr){
			for( int chess : arr){
				if(chess != 0){
					sum ++;
				}
			}
		}
		
		
		int[][] sparseArr = new int[sum+1][3];
		sparseArr[0][0] = 11;
		sparseArr[0][1] = 11;
		sparseArr[0][2] = sum;
		int count = 0;

		for(int i = 0 ; i < chessArr.length; i++) {
			for( int j = 0; j < chessArr.length; j++){
				if(chessArr[i][j] != 0 ) {
					count ++;
					sparseArr[count][0] = i;
					sparseArr[count][1] = j;
					sparseArr[count][2] = chessArr[i][j];
				}
				
			}
		}
		
		System.out.println("打印稀疏数组：");
		for(int[] a : sparseArr) {
			System.out.println(Arrays.toString(a));
		}
		
		
		/**
		 * 将稀疏数组转换为数组
		 */
		int row = sparseArr[0][0];
		int col = sparseArr[0][1];
		int sum2 = sparseArr[0][2];
		sum2++;

		int[][] chessArr2 = new int[row][col];
		for(int i = 1; i < sum2; i++){
			int index1 = sparseArr[i][0];
			int index2 = sparseArr[i][1];
			int val = sparseArr[i][2];
			chessArr2[index1][index2] = val;
		}
		
		/**
		 * 打印数组
		 */
		for(int[] a : chessArr2) {
			System.out.println(Arrays.toString(a));
		}
		
	}
}

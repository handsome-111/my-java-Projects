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
		
		//将二维数组转换为稀疏数组
		
	}
}

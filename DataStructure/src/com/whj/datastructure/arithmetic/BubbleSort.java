package com.whj.datastructure.arithmetic;

import java.util.Arrays;

/**
 * Ã°ÅÝÅÅÐò
 * @author Administrator
 *
 */
public class BubbleSort {
	public static void main(String[] args) {
		int a[] = {44,5,454,54,8978};
		int b = 0;
		/**
		 * 1.±éÀúlength´Î
		 * 2.Öð²½
		 */
		for(int i = 0; i < a.length - 1; i++ ) {
			for( int j = 0; j < a.length - 1 - 1;j++) {
				if(a[j] > a[j + 1] ) {
					int temp = a[j + 1];
					a[j + 1] = a[j];
					a[j] = temp;
				}
			}
			b++;
		}
		System.out.println(Arrays.toString(a) + "," + b);
	}
}

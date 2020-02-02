package com.whj.datastructure;

import java.util.Arrays;

import javax.management.RuntimeErrorException;

public class ArrayQueue{
	private int maxSize;
	private int front;		//队列头,若有取出数据，则后移，默认值为-1，取出则是取出0，然后取出1这样
	private int rear;		//队列尾,若要存储数据，则后移，然后存储
	private int[] arr;		//用于存放数据,模拟队列
	
	public ArrayQueue(int maxSize){
		this.maxSize = maxSize;
		this.arr = new int[maxSize];
		/**
		 * 因为是存储和取出的操作是，先移动指针再进行存储和取出操作，所以是赋值为-1，然后存储和取出0
		 */
		this.front = -1;	//指向队列头的前一个位置
		this.rear = -1;		//指向队列的尾部，就是队列的最后一个数据
	}
	
	//判断队列是否是满的
	public boolean isFull(){
		return rear == maxSize - 1;
	}
	
	public boolean isEmpty(){
		return rear == front;
	}
	
	public void addQueue(int n) {
		//判断是否为空
		if( isFull() ) {
			System.out.println("队列满，不能添加数据");
			return;
		}
		//先后移，再存储
		rear ++; 	
		arr[rear] = n; 	

	}
	
	public int getQueue(){
		//判断是否为空
		if( isEmpty() ) {
			//通过抛出异常
			throw new RuntimeException("队列为空，不能取数据");
		}
		
		//先后移，再取值
		front ++;	
		return arr[front];	
	}
	
	public void showQueue(){
		if( isEmpty() ) {
			System.out.println("队列为空，没有数据");
			return;
		}
		for(int i = front + 1; i <= rear; i++) {
			System.out.print(arr[i] + ",");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		ArrayQueue queue = new ArrayQueue(3);
		queue.addQueue(11);
		queue.addQueue(33);
		queue.showQueue();

		
		queue.getQueue();
		queue.getQueue();
		queue.showQueue();
	}
}

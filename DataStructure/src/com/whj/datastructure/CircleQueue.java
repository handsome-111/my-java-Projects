package com.whj.datastructure;

public class CircleQueue {
	private int maxSize;	//表示数组的最大容量
	private int front;		//队列头,指向队列的第一个元素
	private int rear;		//队列尾,若要存储数据，则后移，然后存储
	private int[] arr;		//用于存放数据,模拟队列
	private int usedSize = 0;	//队列的有效个数
	
	//maxSize设置为4,其队列的有效数据最大是3
	public CircleQueue(int maxSize){
		this.maxSize = maxSize;
		this.arr = new int[this.maxSize];
		this.front = 0;
		this.rear = 0;
		this.usedSize = 0;
	}
	
	public boolean isFull(){
		//当前的rear指针是没有值的，因为是先添加了之后再移动指针，
		//比如前面已经有添加数据了，然后在移动指针，说明当前指针没有值
		//队列有 0,1,2   rear = 2, maxSize = 3, front = 0,  2索引下是没有值的,是预留出来的位置。满足一下公式
		return ( rear + 1 )  % maxSize == front;
	}
	
	public boolean isEmpty(){
		return rear == front;
	}
	
	/**
	 * 添加数据
	 * @param n
	 */
	public void addQueue(int n) {
		if(isFull()) {
			System.out.println("队列已满，无法添加");
			return ;
		}
		arr[rear] = n;
		usedSize++;
		//先添加数据，再移动指针
		//若左边小于maxSize,则值等于左边，若大于右边，则取余，相当与环形，又循环了一次
		rear =  ( rear + 1) % maxSize ;		
	}
	
	/**
	 * 取出数据
	 * @return
	 */
	public int getQueue(){
		if( isEmpty() ) {
			throw new RuntimeException("队列空，不能取数据");
		}
		int temp = arr[front];
		usedSize--;
		front = ( front + 1) % maxSize ;
		return temp;
	}
	
	public void showQueue(){
		/**
		 * 打印arr[i % maxSize] = arr[front % maxSize]
		 * 当front小于maxSize的时候，就是front,当超过maxSize,说明已经从头开始了,就是fornt % maxSize的余数, 0=》1这样继续往下
		 */
		for(int i = front; i < front + usedSize ; i++) {
			System.out.print(arr[i % maxSize] + ",");
		}
		System.out.println();
	}
	
	
	public static void main(String[] args) {
		//队列最多存储2 个容量
		CircleQueue queue = new CircleQueue(3);
		queue.addQueue(2);
		queue.addQueue(4);
		queue.addQueue(5);
		
		queue.showQueue();
		
		queue.getQueue();
		queue.showQueue();
		
		queue.getQueue();
		queue.showQueue();
	}
}

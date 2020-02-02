package com.whj.datastructure.linkedlist;

/**
 * 单向链表
 * @author Administrator
 *
 */
public class SingleLinkedList {
	//先初始化一个头节点，头节点不动，不存放具体的数据
	private HeroNode head = new HeroNode(0,"","");
	
	/**
	 * 添加节点到链表
	 * 1.找到当前链表的最后节点
	 * 2.将最后这个节点的next 指向新的节点
	 * 
	 * @param hreoNode
	 */
	public void add(HeroNode heroNode) {
		
		//从头节点开始
		HeroNode node = head;
		
		//遍历找到最后一个节点
		while(true) {
			if(node.next == null) {
				break;
			}
			//如果没有找到最后，temp就指向了链表的最后		
			node = node.next;
		}
		//添加数据添加到最后一个节点的next里,即next引用是下一个节点
		//将最后一个节点的next赋值为heroNode
		node.next = heroNode;
	}
	
	//按照排序将数据加入节点
	public void addByOrder(HeroNode heroNode){
		
		HeroNode node = head;
		boolean flag = false;	//标记节点是否存在
		//遍历每一个节点
		while ( true ) {
			//尾部
			if( node.next == null ) {
				break;
			}
			
			//如果下一个节点大于当前节点，则不做改变，就放后面
			if(node.next.no > heroNode.no) {
				break;
			} else if ( node.next.no == heroNode.no ) {
				flag = true;	//说明节点存在
				break;
			}
			
			node = node.next;	//后移
		}
		
		if( flag ) {
			System.out.printf("英雄编号 %d 已经存在,不能添加",heroNode.no);
		} else {
			//添加节点,
			//即使在尾部或者中间都满足
			//新节点的 下一节点  = 旧节点的下一节点,旧节点的下一节 = 新节点
			heroNode.next = node.next;
			node.next = heroNode;
		}
	}
	
	//显示链表数据
	public void list(){
		if(head.next == null) {
			System.out.println("链表为空");
			return ;
		}
		
		HeroNode node = head.next;
		//迭代输出每个节点
		while(true) {
			//判断链表是否到了最后一个节点
			if(node == null) {
				break;
			}
			System.out.println(node);
			//将temp后移
			node = node.next;
		}
	}
	/**
	 * 反转链表,使用头插法：一个个往头部放入节点
	 * 1.遍历将每个节点取出,然后一个一个往前面放
	 * 2.如： 1,2,3,4  =》 1 =》 2,1 = 》   3,2,1  =》  4,3,2,1
	 * 
	 */
	public void  reverseList(){
		//如果当前链表为空  或者 当前链表只有一个节点,则无需反转
		if(head.next == null || head.next.next == null) {
			return ;
		}
		
		HeroNode node = head.next;		//取出第一个节点
		
		//创建一个反向的头节点
		HeroNode reverseHead = new HeroNode(0,"","");
		
		/**
		 * 1.先遍历原来顺序的节点,并将其节点取出
		 * 2.	
		 */
		HeroNode iteratorNext = null;		//要迭代的下一个节点
		while ( node != null ) {
			//取出当前节点的下一个节点,因为当前节点的下一节点要修改,所以取出的节点要用来下一次迭代用
			iteratorNext = node.next;
			
			//要将节点插入头的后面,所以将旧节点的next给新节点,旧节点就next向当前节点
			node.next = reverseHead.next;
			reverseHead.next = node;
			
			node = iteratorNext;
		}
		
		head.next = reverseHead.next;
	}
}

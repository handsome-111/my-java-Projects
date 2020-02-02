package com.whj.datastructure.linkedlist;

public class SingleLinkedListDemo {
	public static void main(String[] args) {
		HeroNode h1 = new HeroNode(111,"111","111");
		HeroNode h2 = new HeroNode(444,"444","444");
		HeroNode h3 = new HeroNode(222,"222","222");
		HeroNode h4 = new HeroNode(333,"333","333"); 	
		
		SingleLinkedList list = new SingleLinkedList();
		
		//无序添加
//		list.add(h1);
//		list.add(h2);
//		list.add(h3);
//		list.add(h4);
		
		
		//有序添加,从小到大
		list.addByOrder(h1);
		list.addByOrder(h2);
		list.addByOrder(h3);
		list.addByOrder(h4);
		
		list.list();
		System.out.println("--------------");
		list.reverseList();
		list.list();

	}
}

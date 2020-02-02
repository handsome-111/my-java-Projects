package com.whj.datastructure.linkedlist;

/**
 * 例子：英雄节点,no,名称,昵称
 * 1.不存放具体的数据
 * 2.作用就是表示单链的头
 *
 */
public class HeroNode {
	public int no;
	private String name;
	private String nickName;
	public HeroNode next;		//指向下一个节点,即存储着下一个节点,引用指向下一个节点
	
	public HeroNode(int no, String name, String nickName) {
		super();
		this.no = no;
		this.name = name;
		this.nickName = nickName;
	}

	@Override
	public String toString() {
		return "HeroNode [no=" + no + ", name=" + name + ", nickName=" + nickName + "]";
	}
	
	
}

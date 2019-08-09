package com.youmeng.taoshelf.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result<T> {

    private Long total;

    private List<T> items;
    
    private Map<Integer,List<Good>> map = new HashMap<Integer,List<Good>>();			//按照类目存放商品

    public Result() {
        total = 0L;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

	public Map<Integer, List<Good>> getMap() {
		return map;
	}

	public void setMap(Map<Integer, List<Good>> map) {
		this.map = map;
	}
	
	public void put(Good good){
		int cid = good.getCid();
		//存在类目则添加商品
        if(map.containsKey(cid)){	
        	map.get(cid).add(good);
        }
        //不存在类目，则放入类目
        if(!map.containsKey(cid)){
        	List<Good> list = new ArrayList<Good>();
        	list.add(good);
        	map.put(cid, list);
        }
	}
	/**
	 * 将商品集合分类放入Map中
	 * @param goods
	 */
	public void append(List<Good> goods){
		total += goods.size();
		for(Good good : goods){
			put(good);
		}
	}
}

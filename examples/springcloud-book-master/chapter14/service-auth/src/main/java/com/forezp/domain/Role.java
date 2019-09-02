package com.forezp.domain;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author Mr.hj
 * @since 2019-09-02
 */
@TableName("role")
public class Role implements Serializable,GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
        "id=" + id +
        ", name=" + name +
        "}";
    }

	@Override
	public String getAuthority() {
		return name;
	}
}

package model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import utils.SerializableUtil;
import business.proxy.ICacheable;
import business.proxy.IPersistable;

/**
 * 实现IPersistable会存储到数据库
 * 实现ICacheable会存储到缓存，缓存KEY为全限定名|id
 * use for test
 *
 */
@Entity
@Table(name="item")
public class Item extends AbstractEntity<Integer> implements IPersistable, ICacheable, Externalizable{

	@Column(name="name")
	private String name;
	@Column(name="price")
	private Double price;
	@Column(name="order_num")
	private String orderNum;

	public Item() {
		
	}
	
	public Item(String name, Double price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String toString() {
		return name + " (" + price + ")";
	}

	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id = SerializableUtil.readInt(in);
		name = SerializableUtil.readString(in);
		price = SerializableUtil.readDouble(in);
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		SerializableUtil.writeInt(id, out);
		SerializableUtil.writeString(name, out);
		SerializableUtil.writeDouble(price, out);
	}

}

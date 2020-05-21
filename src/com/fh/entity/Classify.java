package com.fh.entity;

public class Classify {

	private Integer id;
	private String name;
	//
	// url picture name remark distr
	private String photo_list;
	private Integer price;
	private Integer price_unit;
	private String area;

	// id photo_list name price+price_unit area

	public String getPhoto_list() {
		return photo_list;
	}

	public void setPhoto_list(String photo_list) {
		this.photo_list = photo_list;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getPrice_unit() {
		return price_unit;
	}

	public void setPrice_unit(Integer price_unit) {
		this.price_unit = price_unit;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

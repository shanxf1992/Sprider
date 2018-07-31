package com.itheima.entity;

public class Pet {

    private String pid; //商品编号
    private String breed; //品种
    private String shapes; //体型
    private String weight; //体重
    private String sex; //性别
    private String name; //商品名称
    private Double price; //价格

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getShapes() {
        return shapes;
    }

    public void setShapes(String shapes) {
        this.shapes = shapes;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "pid='" + pid + '\'' +
                ", breed='" + breed + '\'' +
                ", shapes='" + shapes + '\'' +
                ", weight='" + weight + '\'' +
                ", sex='" + sex + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

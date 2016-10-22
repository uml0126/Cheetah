package com.rex.cheetah.testcase.invocation;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

public class InvocationEntity {
    private String name;
    private int age;
    private long[] ips;
    private int weight;
    private int height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long[] getIps() {
        return ips;
    }

    public void setIps(long[] ips) {
        this.ips = ips;
    }
    
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public void setFacade(int weight, int height) {
        this.setWeight(weight);
        this.setHeight(height);
    }
}
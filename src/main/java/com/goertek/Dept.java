package com.goertek;

import java.util.ArrayList;
import java.util.List;

public class Dept {

    List<Dept> subDept;

    List<Emp> empList;

    boolean isCut;

    String name;

    String pid;

    String dept;

    String id;

    public int getChildsNum() {
        return childsNum;
    }

    public void setChildsNum(int childsNum) {
        this.childsNum = childsNum;
    }

    int childsNum;

    String email;

    public List<Dept> getSubDept() {
        return subDept;
    }

    public void setSubDept(List<Dept> subDept) {
        this.subDept = subDept;
    }

    public void addSubDept(Dept subDept) {
        if (this.subDept == null)
            this.subDept = new ArrayList<Dept>();
        this.subDept.add(subDept);
    }

    public List<Emp> getEmpList() {
        if (empList == null)
            empList = new ArrayList<Emp>();
        return empList;
    }

    public void setEmpList(List<Emp> empList) {
        this.empList = empList;
    }


    public void addEmp(Emp emp) {
        if (this.empList == null)
            this.empList = new ArrayList<Emp>();
        this.empList.add(emp);
    }

    public boolean isCut() {
        return isCut;
    }

    public void setCut(boolean cut) {
        isCut = cut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

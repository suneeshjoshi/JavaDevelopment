package com.suneesh.aopdemo.dao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountDAO {

    public void addAccount(){
        System.out.println(getClass() + " : Doing my DB work , Adding an Account.");
    }

    public void displayAccount(){
        System.out.println(getClass() + " : Display an Account.");
    }

    public List<String> findAccount(){
        return List.of("String 1", "String 2", "String 3", "String 4");
    }

}

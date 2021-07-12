package com.suneesh.aopdemo.dao;

import org.springframework.stereotype.Component;

@Component
public class AccountDAO {

    public void addAccount(){
        System.out.println(getClass() + " : Doing my DB work , Adding an Account.");
    }

    public void displayAccount(){
        System.out.println(getClass() + " : Display an Account.");
    }

}

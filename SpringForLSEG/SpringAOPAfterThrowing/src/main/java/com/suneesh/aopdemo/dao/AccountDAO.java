package com.suneesh.aopdemo.dao;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountDAO {

    public void addAccount(){
        System.out.println(getClass() + " : Doing my DB work , Adding an Account.");
    }

    public void displayAccount(){
        System.out.println(getClass() + " : Display an Account.");
    }

    public List<String> findAccount() throws Exception {
        throw new Exception("Throwing some exception");

    }

}

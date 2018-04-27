package com.example.mytest.utils;

import java.util.Comparator;

/**
 * Created by KEVIN.DAI on 15/11/26.
 */
public class SortComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}
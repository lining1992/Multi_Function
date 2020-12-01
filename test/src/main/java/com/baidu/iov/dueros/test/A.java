package com.baidu.iov.dueros.test;

/**
 * @author v_lining05
 * @date 2020/11/20
 */
abstract class A implements I{

    public A() {

    }


    @Override
    public void appKey() {
        System.out.println("A appKey");
    }
}

class B extends A {

    @Override
    public void appKey() {
        System.out.println("B appKey");
    }
}

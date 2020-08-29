package com.baidu.iov.dueros.test


/**
 *
 * @author v_lining05
 * @date   2020-08-21
 *
 */
class TestUtils constructor(val str : String) {

    var isNull : String = "not null"
    var isNUll2 : String? = null

    init {
        println("init===$str")
    }

    companion object {
        val string : String = "string"
        val str1 : String = "str"
        fun init() {
            println("companion object")
        }
    }

    fun testMethod() {
        println("testMethod ==== $str")
    }

}
package com.baidu.iov.dueros.test

import android.util.Log

/**
 *
 * @author v_lining05
 * @date   2020-08-21
 *
 */
class KotlinUtils {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val test = TestUtils("123456")
            test.testMethod();
            TestUtils.init();
            println(TestUtils.str1)
            println(TestUtils.string)
        }
    }
}

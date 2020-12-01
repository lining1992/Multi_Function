package com.baidu.iov.dueros.test;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

/**
 * @author v_lining05
 * @date 2020-08-21
 */
public class Test {

    public static void main(String[] args) {

//        final CountDownLatch countDownLatch = new CountDownLatch(50);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 50; i++) {
//                    final int index = i;
//                    try {
//                        System.out.println("countDownLatch==" + index);
//                        countDownLatch.countDown();
//                    } catch (Exception e) {
//                        System.out.println("countDownLatch==" + e.getMessage());
//                    }
//                }
//            }
//        }).start();
//        try {
//            countDownLatch.await();
//        } catch (Exception e) {
//            System.out.println("countDownLatch==end" + e.getMessage());
//        }
//        System.out.println("countDownLatch==end");

        HashMap<Object, Object> hashMap = new HashMap<>(4, 0.75f);
        hashMap.put("1", "map1");
        hashMap.put("1", "map3");
        hashMap.put("1", "map4");
        hashMap.put("1", "map2");
        hashMap.put("1", "map5");

        System.out.println("value:" + hashMap.get("1"));
        System.out.println("hashCode:" + "map1".hashCode());
        System.out.println("hashCode:" + "map2".hashCode());
        System.out.println("hashCode:" + "map1".hashCode());
        int i = (16 - 1) & "map5".hashCode();
        System.out.println("索引:" + i);

        System.out.println("整数转罗马数字：" + intToRoman(56));

        System.out.println("罗马数字转整型：" + romanToInt("XL"));

        ListNode l1 = new ListNode(1);    //创建链表对象 l1 （对应有参 和 无参 构造方法）
        l1.add(6);                //插入结点，打印
        l1.add(9);
        ListNode l2 = new ListNode(2);    //创建链表对象 l1 （对应有参 和 无参 构造方法）
        l2.add(3);                //插入结点，打印
        l2.add(8);
        System.out.println("单链表合并:");
        mergeTwoLists(l1, l2).print();
        System.out.println();
        System.out.println("=================");
        ListNode listNode = new ListNode(2);
        listNode.add(4);
        listNode.add(3);
        ListNode listNode1 = new ListNode(5);
        listNode1.add(6);
        listNode1.add(4);
        addTwoNumbers(listNode, listNode1).print();
        System.out.println();
        System.out.println("=================");

        Stack<Integer> stack = new Stack<Integer>();



    }

    static int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    static String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    /**
     * 整数转罗马数字
     *
     * @param num
     * @return
     */
    public static String intToRoman(int num) {
        StringBuilder sb = new StringBuilder();
        // Loop through each symbol, stopping if num becomes 0.
        for (int i = 0; i < values.length && num >= 0; i++) {
            // Repeat while the current symbol still fits into num.
            System.out.println("intToRoman==values[i]=" + values[i]);
            while (values[i] <= num) {
                System.out.println("intToRoman==i=" + i);
                num = num - values[i];
                System.out.println("intToRoman==num=" + num);
                System.out.println("intToRoman==symbols[i]=" + symbols[i]);
                sb.append(symbols[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 罗马数字转整型
     *
     * @param s
     * @return
     */
    public static int romanToInt(String s) {
        int res = 0;
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        int i = 0;
        while (i < s.length()) {
            //虽然无法到达最后一位，但是如果罗马数字最后以特殊数字结尾（比如4，9），则不需要判断最后一位，因为一定是两个字母的组合中的一个。
            if (i + 1 < s.length() && map.get(s.charAt(i)) < map.get(s.charAt(i + 1))) {
                res = res + map.get(s.charAt(i + 1)) - map.get(s.charAt(i));
                i += 2;
            } else {
                res = res + map.get(s.charAt(i));
                i += 1;
            }
        }
        return res;
    }


    //创建一个链表的类
    static class ListNode {
        int val;    //数值 data
        ListNode next;    // 结点 node

        ListNode(int x) {    //可以定义一个有参构造方法，也可以定义一个无参构造方法
            val = x;
        }

        // 添加新的结点
        public void add(int newval) {
            ListNode newNode = new ListNode(newval);
            if (this.next == null) {
                this.next = newNode;
            } else {
                this.next.add(newval);
            }
        }

        // 打印链表
        public void print() {
            System.out.print(this.val);
            if (this.next != null) {
                System.out.print("-->");
                this.next.print();
            }
        }
    }

    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // 创建一个新链表， dummyHead 虚拟头结点
        ListNode dummyHead = new ListNode(0);
        ListNode curr = dummyHead;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                curr.next = l1;
                curr = curr.next;
                l1 = l1.next;
            } else {
                curr.next = l2;
                curr = curr.next;
                l2 = l2.next;
            }
        }
        // 有一方为空时，直接将剩下的链接到 curr 上
        if (l1 == null) {
            curr.next = l2;
        } else {
            curr.next = l1;
        }
        return dummyHead.next; // 从头结点之后开始返回，也就是合并之后的链表
    }

    /**
     * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，
     * 并且它们的每个节点只能存储 一位 数字。
     * <p>
     * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
     * <p>
     * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
     * <p>
     * 示例：
     * <p>
     * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
     * 输出：7 -> 0 -> 8
     * 原因：342 + 465 = 807
     *
     * @param l1
     * @param l2
     * @return
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode root = new ListNode(0);
        ListNode cursor = root;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            int l1Val = l1 != null ? l1.val : 0;
            int l2Val = l2 != null ? l2.val : 0;
            int sumVal = l1Val + l2Val + carry;
            carry = sumVal / 10;

            ListNode sumNode = new ListNode(sumVal % 10);
            cursor.next = sumNode;
            cursor = sumNode;

            if (l1 != null) l1 = l1.next;
            if (l2 != null) l2 = l2.next;
        }
        return root.next;
    }

    /**
     * 独一无二的出现次数
     * <p>
     * 给你一个整数数组 arr，请你帮忙统计数组中每个数的出现次数。
     * <p>
     * 如果每个数的出现次数都是独一无二的，就返回 true；否则返回 false。
     *
     * @param arr
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean uniqueOccurrences(int[] arr) {
        Map<Integer, Integer> counter = new HashMap<>();
        for (int elem : arr) {
            counter.put(elem, counter.getOrDefault(elem, 0) + 1);
        }
        return counter.size() == new HashSet<Integer>(counter.values()).size();
    }

    /**
     * 二叉树深度-后序遍历
     *
     * 复杂度分析：
     * 时间复杂度 O(N) ： N 为树的节点数量，计算树的深度需要遍历所有节点。
     * 空间复杂度 O(N) ： 最差情况下（当树退化为链表时），递归深度可达到 NN 。
     *
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    /**
     * 两数之和
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {
        int length = nums.length;
        for (int i = 0; i < length; ++i) {
            for (int j = i + 1; j < length; ++j) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }





}

package com.example.convert;

import com.codefactory.utils.Util_File;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass5 {

    public static void main(String[] args) {

//        List<Node> list1 = new ArrayList<>();
//        list1.add(new Node(1, "a1"));
//        list1.add(new Node(1, "a1"));
//        list1.add(new Node(2, "a2"));
//        list1.add(new Node(3, "a3"));
//        list1.add(new Node(4, "a4"));
//        list1.add(new Node(5, "a5"));

//        List<Node> list2 = new ArrayList<>();
//        list2.add(new Node(1, "b1"));
//        list2.add(new Node(2, "b2"));
//        list2.add(new Node(6, "b6"));
//        list2.add(new Node(7, "b7"));
//        list2.add(new Node(8, "b8"));

//        list1.retainAll(list2);
//        System.out.println(list1);
//
//        list2.removeAll(list1);
//        System.out.println(list2);
//
//        list1.addAll(list2);
//        System.out.println(list1);

        String block="[{\"PersonID\":\"\",\"shopName\":\"\",\"Number\":\"1\",\"SendMode\":\"0\",\"GoodsID\":\"18000723\"},{\"PersonID\":\"\",\"shopName\":\"\",\"Number\":\"1\",\"SendMode\":\"0\",\"GoodsID\":\"18000710\"},{\"PersonID\":\"\",\"shopName\":\"\",\"Number\":\"1\",\"SendMode\":\"0\",\"GoodsID\":\"18000763\"},{\"PersonID\":\"\",\"shopName\":\"\",\"Number\":\"1\",\"SendMode\":\"0\",\"GoodsID\":\"18000860\"},{\"PersonID\":\"\",\"shopName\":\"\",\"Number\":\"1\",\"SendMode\":\"1\",\"GoodsID\":\"18000596\"}]";
        List<String> orgList=extracted("D:\\Work\\orglist.txt");
        System.out.println(orgList.size());
        List<String> newList=extracted("D:\\Work\\newlist.txt");
        System.out.println(newList.size());
        orgList.removeAll(newList);
        String save="";
        System.out.println(orgList.size());
        for (int i = 0; i < orgList.size(); i++) {
            String resultSplit="('"+orgList.get(i)+"','98092561','"+block+"','2022-03-26 00:00:00','00','1')";
            save+=resultSplit+",";
        }
        save="INSERT INTO hmm_coupon.member_reg_activity " +
        "( `member_id`, `ytb_app_id`, `content`, `create_time`, `card_gift_type` ,`status`) " +
                "VALUES"+save+";";
        Util_File.string2Stream(save,new File("resultlist.txt"));


    }

    private static List<String> extracted(String path) {
        List<String> listOrg=new ArrayList<>();
        String oldlistString=Util_File.inputStream2String(path);
        String[] olgListStringArray=oldlistString.split("\n");
        for (int i = 0; i < olgListStringArray.length; i++) {
            listOrg.add(olgListStringArray[i].trim());
        }
        return listOrg;
    }

    static class Node {
        public int id;
        public String attrs;

        public Node(int id, String attrs) {
            this.id = id;
            this.attrs = attrs;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "id=" + id +
                    ", attrs='" + attrs + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return id == node.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}

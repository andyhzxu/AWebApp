package com.anh.kafka;

public class MyData<K> {
    private String[] values;

    public MyData() {
        values = new String[10];
    }

    public void put(K k) {
        values[0] = String.valueOf(k);
        System.out.println("values[0]: " + values[0]);
    }

}

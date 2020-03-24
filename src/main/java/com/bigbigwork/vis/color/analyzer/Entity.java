package com.bigbigwork.vis.color.analyzer;

import com.alibaba.fastjson.JSONArray;
import com.bigbigwork.vis.color.util.LAB;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Entity {

    private int C;
    private int W;
    private int[] T;
    private JSONArray A;
    private JSONArray terms;
    private int[] ccount;
    private int[] tcount;
    private LAB[] color;
    private Map<String, Integer> map;


    public int getC() {
        return C;
    }

    public Entity setC(int c) {
        C = c;
        return this;
    }

    public int getW() {
        return W;
    }

    public Entity setW(int w) {
        W = w;
        return this;
    }

    public int[] getT() {
        return T;
    }

    public Entity setT(int[] t) {
        T = t;
        return this;
    }


    /**
     *
     * @return word association matrix
     */
    public JSONArray getA() {
        return A;
    }

    /**
     * word association matrix
     * @param a
     * @return
     */
    public Entity setA(JSONArray a) {
        A = a;
        return this;
    }

    public int[] getCcount() {
        return ccount;
    }

    public Entity setCcount(int[] ccount) {
        this.ccount = ccount;
        return this;
    }

    public int[] getTcount() {
        return tcount;
    }

    public Entity setTcount(int[] tcount) {
        this.tcount = tcount;
        return this;
    }

    public JSONArray getTerms() {
        return terms;
    }

    public Entity setTerms(JSONArray terms) {
        this.terms = terms;
        return this;
    }

    public LAB[] getColor() {
        return color;
    }

    public Entity setColor(LAB[] color) {
        this.color = color;
        build();
        return this;
    }


    // build lookup table
    public void build(){
        Function<LAB,String> string = x -> String.format("%s,%s,%s", (int)x.L,(int)x.a,(int)x.b);
//        Function<LAB,String> string = x -> Stream.of(x.L,x.a,x.b).map(String::valueOf).collect(Collectors.joining(","));
        map = IntStream.range(0, color.length).boxed().collect(Collectors.toMap(i -> string.apply(color[i]), Function.identity()));
    }

    public Integer getIndex(String key){
        return map.get(key);
    }
}

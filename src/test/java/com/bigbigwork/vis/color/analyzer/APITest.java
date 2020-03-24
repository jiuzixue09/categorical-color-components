package com.bigbigwork.vis.color.analyzer;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

class APITest {

    @Test
    void index(){
        var c3 = new C3();
        var api = new API(c3);
        Integer index = api.index("1f77b4");
        System.out.println(index);

    }

    @Test
    void color(){
        var c3 = new C3();
        var api = new API(c3);
        JSONObject color = api.color("1f77b4");
        System.out.println(color.getJSONArray("terms"));

    }

}
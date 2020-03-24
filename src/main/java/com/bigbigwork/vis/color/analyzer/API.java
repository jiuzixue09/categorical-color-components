package com.bigbigwork.vis.color.analyzer;

import com.alibaba.fastjson.JSONObject;
import com.bigbigwork.vis.color.util.LAB;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class API {
// NOTE: entropy min/max currently hard-wired to XKCD results
    private static final double minE = -4.5;
    private static final double maxE = 0;

    private Entity c3;
    public API(Entity c3){
        this.c3 = c3;
    }

    public Entity getC3() {
        return c3;
    }

    private LAB toLab(String hexStr){
        int pixel = Integer.parseInt(hexStr, 16);
        int red = (pixel & 0xff0000) >> 16;
        int green = (pixel & 0xff00) >> 8;
        int blue = (pixel & 0xff);
        return LAB.fromRGB(red, green, blue, 0);
    }

    public Integer index(String c) {
        var x = toLab(c);
        var L = 5 * Math.round(x.L / 5);
        var a = 5 * Math.round(x.a / 5);
        var b = 5 * Math.round(x.b / 5);

        String str = String.format("%s,%s,%s", L,a,b);
        return c3.getIndex(str);
    }

    public JSONObject color(String x) {
        var c = index(x);
        var h = (entropy(c) - minE) / (maxE - minE);
        var t = relatedTerms(c, 1, 2);
        var l = toLab(x);

        var z = ((int) l.L) +", "+ ((int) l.a) +", "+ ((int) l.b);
        var json = new JSONObject();
        json.put("x",x);
        json.put("c",c);
        json.put( "h",h);
        json.put("terms",t);
        json.put( "z", z);
        return json;
    }

    public double entropy(int c) {
        var H = 0.0;
        var p = 0.0;
        for (var w = 0; w < c3.getW(); ++w) {
            p = 1.0 * (c3.getT()[c * c3.getW() + w]) / c3.getCcount()[c];
            if (p > 0) H += p * Math.log(p) / Math.log(2);
        }
        return H;
    }


    public List<JSONObject> relatedTerms(int c, int limit, int minCount) {
        var cc = c* c3.getW();
        var list = new ArrayList<JSONObject>();
        var sum = 0;
        var s = 0;
        var cnt = c3.getW();
        for (var w = 0; w < cnt; ++w) {
            if ((s = c3.getT()[cc+w]) > 0) {
                var j = new JSONObject();
                j.put("index",w);
                j.put("score", s);
                list.add(j);
                sum += s;
            }
        }
        if (minCount > 0) {
            list = (ArrayList<JSONObject>) list.stream().filter(it -> c3.getTcount()[it.getIntValue("index")] > minCount).collect(Collectors.toList());
        }

        list.sort((o1, o2) -> o2.getIntValue("score") - o1.getIntValue("score"));

        double finalSum = sum;
        list.forEach(it -> it.compute("score",(s1, score) -> (int)score / finalSum));

        return limit > 0 ? list.subList(0, limit) : list;
    }

}

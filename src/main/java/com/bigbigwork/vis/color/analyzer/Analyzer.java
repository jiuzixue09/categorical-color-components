package com.bigbigwork.vis.color.analyzer;

import com.alibaba.fastjson.JSONObject;

import java.text.NumberFormat;

public class Analyzer {
    private static final API api;
    private final NumberFormat percentageFormat;

    public Analyzer(){
        percentageFormat = NumberFormat.getPercentInstance();
        percentageFormat.setMinimumFractionDigits(2);
    }

    static {
        var c3 = new C3();
        var json = c3.load();
        var entity = c3.init(json);
        api = new API(entity);
    }

    public JSONObject analyze(String hexStr){


        JSONObject color = api.color(hexStr);
        var terms = color.getJSONArray("terms").getJSONObject(0);
        var index = terms.getIntValue("index");

        var name = api.getC3().getTerms().get(index);
        var score = percentageFormat.format(terms.getDouble("score"));
        var rs = new JSONObject();
        rs.put("name", name);
        rs.put("score", score);
        return rs;

    }
}

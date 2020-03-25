package com.bigbigwork.vis.color.analyzer;

import com.alibaba.fastjson.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

@Singleton
public class Analyzer {

    @Inject
    API api;
    private NumberFormat percentageFormat;
    private Predicate<String> predicate;

    public Analyzer(){
        percentageFormat = NumberFormat.getPercentInstance();
        percentageFormat.setMinimumFractionDigits(2);

        predicate = this::isBlank;
        predicate = predicate.and(this::rightLength).and(this::isHex);
    }

    private boolean isBlank(String str){
        return Objects.nonNull(str) && !str.isBlank();
    }
    private boolean rightLength(String str){
        return str.length() == 6;
    }
    private boolean isHex(String str){
        return str.matches("#?[a-fA-F0-9]+");
    }


    public JSONObject analyze(String hexStr){
        var rs = new JSONObject();

        if(!predicate.test(hexStr)){
            rs.put("status", 500);
            rs.put("message", "wrong HEX color");
            return rs;
        }

        try {
            JSONObject color = api.color(hexStr);
            var terms = color.getJSONArray("terms").getJSONObject(0);
            var index = terms.getIntValue("index");

            var name = api.getEntity().getTerms().get(index);
            var score = percentageFormat.format(terms.getDouble("score"));

            rs.put("name", name);
            rs.put("score", score);
            rs.put("status", 200);

        }catch (Exception e){
            rs.put("status", 500);
            rs.put("message", Arrays.deepToString(e.getStackTrace()));
        }
        return rs;

    }
}

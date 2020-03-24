package com.bigbigwork.vis.color.analyzer;

import com.alibaba.fastjson.JSONObject;
import com.bigbigwork.vis.color.util.LAB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class C3 {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    public static final String FILE = "data/xkcd/c3_data.json";

    public JSONObject load(){
        try {
            LOG.info("read file {}", FILE);
            var str = Files.readString(Paths.get(FILE));
            return JSONObject.parseObject(str);
        } catch (IOException e) {
            LOG.error("file read error", e);
            e.printStackTrace();
        }
        return null;
    }

    public Entity init(JSONObject json) {
        var colorArr = json.getJSONArray("color");

        // parse colors
        var color = new LAB[colorArr.size() / 3];
        for (var i=0; i<colorArr.size(); i+=3) {
            color[i/3] = new LAB(colorArr.getDouble(i), colorArr.getDouble(i + 1), colorArr.getDouble(i + 2));
        }
        var C = color.length;

        // parse terms
        var terms = json.getJSONArray("terms");
        var W = terms.size();

        var tArray = json.getJSONArray("T");
        // parse count table
        Optional<Integer> max = tArray.toJavaList(Integer.class).stream().max(Integer::compareTo);
        var T = new int[max.orElseThrow() + 1];
        var keys = new ArrayList<Integer>();
        for (var i = 0; i < tArray.size(); i += 2) {
            T[tArray.getIntValue(i)] = tArray.getIntValue(i + 1);
            keys.add(tArray.getIntValue(i));
        }

        // construct counts
        var ccount = new int[C];
        var tcount = new int[W];
        keys.forEach(idx -> {
            var c = idx / W;
            var w = idx % W;
            var v = T[idx];
            ccount[c] += v;
            tcount[w] += v;
        });

        // parse word association matrix
        var A = json.getJSONArray("A");

        Entity entity = new Entity();
        entity.setA(A).setC(C).setW(W).setT(T).setCcount(ccount).setTcount(tcount).setColor(color).setTerms(terms);

      return entity;
    }


}

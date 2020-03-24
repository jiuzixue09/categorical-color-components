package com.bigbigwork.vis.color.analyzer;

import org.junit.jupiter.api.Test;

class C3Test {

    @Test
    void load() {
        var c3 = new C3();
        var json = c3.load();
        c3.init(json);
    }

    @Test
    void init(){
        var c3 = new C3();
        var json = c3.load();
        c3.init(json);
    }

}
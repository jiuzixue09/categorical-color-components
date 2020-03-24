package com.bigbigwork.vis.color.analyzer;

import org.junit.jupiter.api.Test;

class AnalyzerTest {

    @Test
    void analyze() {
        var analyzer = new Analyzer();
        var json = analyzer.analyze("1f77b4");
        System.out.println(json);
    }

}
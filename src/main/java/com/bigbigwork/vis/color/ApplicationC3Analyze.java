package com.bigbigwork.vis.color;

import com.bigbigwork.vis.color.analyzer.Analyzer;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationC3Analyze {
    private static final Analyzer analyzer = new Analyzer();
    protected static final Logger LOG = LoggerFactory.getLogger(ApplicationC3Analyze.class);

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        //https://code.aliyun.com/jhk-neptune/categorical-color-components.git
        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/analyze/:name", ctx -> ctx.result(analyzer.analyze(ctx.pathParam("name")).toJSONString()));

        app.after("/analyze/*", ctx -> {
            LOG.info("analyze: path={},status={}, rs={}", ctx.path(),ctx.status(), ctx.resultString());
        });

    }
}

package io.github.wwqgtxx.soilqualitymonitor.common;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wwq on 2016/5/31.
 */
public class NoCacheFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) res;
        resp.setHeader("Pragma", "No-cache");
        resp.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.addDateHeader("expires", -1);
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
package com.yoxaron.cuurency_exchange.filter;

import com.yoxaron.cuurency_exchange.exception.ApiException;
import com.yoxaron.cuurency_exchange.exception.InternalServerError;
import com.yoxaron.cuurency_exchange.utils.JsonResponseUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.yoxaron.cuurency_exchange.utils.JsonResponseUtil.*;

@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            chain.doFilter(request, response);
        } catch (ApiException e) {
            sendErrorResponse(httpResponse, e);
        } catch (Exception e) {
            sendErrorResponse(httpResponse, new InternalServerError());
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

package com.lyp.demoexceptionhandle.exception;

import com.lyp.demoexceptionhandle.result.Result;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 李宜鹏
 * @Date: 2020/4/26 23:05
 */
@RestController
public class GlobalErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @ResponseBody
    @RequestMapping(path = ERROR_PATH)
    public Result error(HttpServletRequest request, HttpServletResponse response) {
        Result result = null;

        int status = response.getStatus();
        switch (status) {
            case 404:
                result = new Result(status, "404错误");
                break;
            case 500:
                result = new Result(status, "500错误");
                break;
            default:
                result = new Result(status, "其他异常");
                break;
        }
        return result;
    }

    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return GlobalErrorController.ERROR_PATH;
    }
}

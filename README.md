@[TOC](Spring统一异常的方式)
在日常项目开发过程中，异常总是在所难免，

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020042523331427.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNzU4Mzc2Mw==,size_16,color_FFFFFF,t_70)
这样的提示对用户来说非常不友好，我们可以采用以下方式对异常统一处理

# 实现方式
有两种方式可以实现异常的统一处理

 1. 使用@ControllerAdvice和@ExceptionHandler注解
 2. 实现ErrorController接口的方式

## 准备相关类
### 统一结果类
建一个统一返回类，用以统一返回后台信息格式，方便前端经行处理
```java
package com.lyp.demoexceptionhandle.result;

import java.util.StringJoiner;

/**
 * @Author: 李宜鹏
 * @Date: 2020/4/26 0:13
 */
public class Result {

    private int code;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Result.class.getSimpleName() + "[", "]")
                .add("code=" + code)
                .add("message='" + message + "'")
                .add("data=" + data)
                .toString();
    }

```
### 用于测试的Controller

```java
package com.lyp.demoexceptionhandle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试Controller
 * @Author: 李宜鹏
 * @Date: 2020/4/26 0:06
 */

@RestController
public class TestController {

    /**
     * 测试方法
     * @param name
     * @return
     */
    @RequestMapping("/test")
    public String test(String name) {
        return name.toString();
    }
}
```


## 第一种：使用@ControllerAdvice和@ExceptionHandler注解

```java
package com.lyp.demoexceptionhandle.exception;

import com.lyp.demoexceptionhandle.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author: 李宜鹏
 * @Date: 2020/4/26 0:01
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result NullPointerException() {
        return new Result(500, "空指针异常");
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result Exception() {
        return new Result(500, "其他异常错误");
    }
}
```
统一异常处理后页面提示信息如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200426233443393.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNzU4Mzc2Mw==,size_16,color_FFFFFF,t_70)
这样就可以在前端做相应的处理了

## 第二种：实现ErrorController接口
```java
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
    @RequestMapping(path = ERROR_PATH )
    public Result error(HttpServletRequest request, HttpServletResponse response){
        Result result = new Result(500,"HttpErrorController error:"+response.getStatus());
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
```
统一处理后页面提示信息如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200426233443393.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNzU4Mzc2Mw==,size_16,color_FFFFFF,t_70)
## 两种方式的区别
两种方式都可以实现对后台异常的统一处理

**使用@ControllerAdvice和@ExceptionHandler注解**的方式可以根据后台的具体异常经行处理，如：NullPointerException、IndexOutOfBoundsException..根据报错的异常类型来进行异常的统一处理

**使用实现ErrorController接口**的方式可以根据HttpServletResponse的状态码，如：404、405、500...等状态码来经行异常的统一处理

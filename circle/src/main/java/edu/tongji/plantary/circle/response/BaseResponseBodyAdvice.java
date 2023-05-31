package edu.tongji.plantary.circle.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.UiConfiguration;

import java.util.ArrayList;

@ControllerAdvice(basePackages = {"edu.tongji.plantary.circle.controller"})
//@ControllerAdvice(annotations = RestController.class)
public class BaseResponseBodyAdvice implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // 遇到feign接口之类的请求, 不应该再次包装,应该直接返回
        // 上述问题的解决方案: 可以在feign拦截器中,给feign请求头中添加一个标识字段, 表示是feign请求
        // 在此处拦截到feign标识字段, 则直接放行 返回body.


        // 这里需要过滤掉swagger的相关返回

        if (body instanceof BaseResponse || body instanceof Json || body instanceof UiConfiguration || (body instanceof ArrayList && ((ArrayList) body).get(0) instanceof SwaggerResource)) {
            return body;
        } else if (body == null) {
            System.out.println("响应拦截成功");
            //return BaseResponse.ok();
            return BaseResponse.bad();

        } else {
            System.out.println("响应拦截成功");
            return BaseResponse.ok(body);
        }
    }
}


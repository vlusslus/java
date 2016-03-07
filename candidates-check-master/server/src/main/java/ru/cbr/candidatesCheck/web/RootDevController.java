package ru.cbr.candidatesCheck.web;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;


@Controller
@ConditionalOnMissingBean(name="rootProductionController")
public class RootDevController extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter{
    private String ROOT_PATH = "client/src";

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ResponseBody
    void root(HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        File index = new File(ROOT_PATH + "/index.html");
        String data = FileUtils.readFileToString(index, "UTF-8");
        IOUtils.write(data, response.getWriter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/module/**")
                .addResourceLocations("file:" + ROOT_PATH + "/module/")
        ;
    }
}

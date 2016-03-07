package ru.cbr.candidatesCheck.web;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Configuration
@ConditionalOnResource(resources = RootProductionController.CLASSPATH_META_INF_RESOURCES_INDEX_HTML)
public class RootProductionController extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {
    protected static final String CLASSPATH_META_INF_RESOURCES_INDEX_HTML = "classpath:/index.html";

    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    void root(HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        IOUtils.copy(resourceLoader.getResource(CLASSPATH_META_INF_RESOURCES_INDEX_HTML).getInputStream(),response.getOutputStream());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/module/**").addResourceLocations("classpath:/module/");
    }

}

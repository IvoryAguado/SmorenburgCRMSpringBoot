package me.smorenburg.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableWebMvc
public class WebController extends WebMvcConfigurerAdapter {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/resources/",
            "classpath:/static/"};

    @Value("${crm.name}")
    private String crmName;


    @Autowired
    private
    RequestProcessingTimeInterceptor requestProcessingTimeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(requestProcessingTimeInterceptor);
        // next two should be avoid -- tightly coupled and not very testable
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**").addResourceLocations(
                CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
//        registry.addViewController("/results").setViewName("results");
//        registry.addViewController("/login").setViewName("login");
//        registry.addViewController("/form").setViewName("form");
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ExtendedModelMap model) {
        return "redirect:/";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(ExtendedModelMap model) {
        model.addAttribute("contentView", "/pageviews/landing");
        return "index";
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String projects(ExtendedModelMap model) {
        model.addAttribute("contentView", "/pageviews/projects");
        return "index";
    }

    @RequestMapping(value = "/team", method = RequestMethod.GET)
    public String team(ExtendedModelMap model) {
        model.addAttribute("contentView", "/pageviews/team");
        return "index";
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public String services(ExtendedModelMap model) {
        model.addAttribute("contentView", "/pageviews/services");
        return "index";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contact(ExtendedModelMap model) {
        model.addAttribute("contentView", "/pageviews/contact");
        return "index";
    }

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver result = new ClassLoaderTemplateResolver();
        result.setPrefix("templates/");
        result.setSuffix(".html");
        result.setTemplateMode("HTML");
        result.setOrder(1);
        return result;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setViewClass(ThymeleafView.class);
        return viewResolver;
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();

    }

    @Component
    public class RequestProcessingTimeInterceptor extends HandlerInterceptorAdapter {

        private final Logger logger = LoggerFactory
                .getLogger(RequestProcessingTimeInterceptor.class);

        @Override
        public boolean preHandle(HttpServletRequest request,
                                 HttpServletResponse response, Object handler) throws Exception {
            request.setAttribute("crmName", crmName);
            return true;
        }

        @Override
        public void postHandle(HttpServletRequest request,
                               HttpServletResponse response, Object handler,
                               ModelAndView modelAndView) throws Exception {
        }

        @Override
        public void afterCompletion(HttpServletRequest request,
                                    HttpServletResponse response, Object handler, Exception ex)
                throws Exception {
        }

    }
}

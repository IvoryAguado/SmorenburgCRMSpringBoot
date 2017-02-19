package me.smorenburg.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Controller
@EnableWebMvc
public class WebController extends WebMvcConfigurerAdapter {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/resources/",
            "classpath:/static/"};
    @Value("${crm.name}")
    private String crmName;

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
        model.addAttribute("crmName", crmName);
        return "index";
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String projects(ExtendedModelMap model) {
        model.addAttribute("crmName", crmName);
        model.addAttribute("contentView", "/pageviews/projects");
        return "index";
    }

    @RequestMapping(value = "/team", method = RequestMethod.GET)
    public String team(ExtendedModelMap model) {
        model.addAttribute("crmName", crmName);
        model.addAttribute("contentView", "/pageviews/team");
        return "index";
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public String services(ExtendedModelMap model) {
        model.addAttribute("crmName", crmName);
        model.addAttribute("contentView", "/pageviews/services");
        return "index";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contact(ExtendedModelMap model) {
        model.addAttribute("crmName", crmName);
        model.addAttribute("contentView", "/pageviews/contact");
        return "index";
    }

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver result = new ClassLoaderTemplateResolver();
        result.setPrefix("templates/");
        result.setSuffix(".html");
        result.setTemplateMode("HTML5");
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

    class SmorenburgCRMModelView extends ExtendedModelMap {

    }
}

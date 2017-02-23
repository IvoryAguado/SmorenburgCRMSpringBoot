package me.smorenburg.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Controller
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.websystique.springmvc")
public class WebController extends WebMvcConfigurerAdapter {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:webjars/"
    };

    @Value("${crm.name}")
    private String crmName;

    @Value("${crm.copyright}")
    private String crmCopyRight;

    @Value("${crm.copyright.link}")
    private String crmCopyRightLink;

    @Value("${crm.social.twitter.link}")
    private String crmTwitterLink;

    @Value("${crm.social.facebook.link}")
    private String crmFacebookLink;

    @Value("${crm.social.instagram.link}")
    private String crmInstagramLink;

    @Value("${crm.social.rss.link}")
    private String crmRssLink;

    @Value("${crm.address.road}")
    private String crmAddressRoad;

    @Value("${crm.address.city}")
    private String crmAddressCity;

    @Value("${crm.address.country}")
    private String crmAddressCountry;

    @Value("${crm.address.postalcode}")
    private String crmAddressPostalCode;

    @Value("${crm.address.email}")
    private String crmAddressEmail;

    @Value("${crm.address.phone}")
    private String crmAddressPhone;

    @Value("${crm.social.email.link}")
    private String crmEmailLink;


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
                CLASSPATH_RESOURCE_LOCATIONS).setCacheControl(
                CacheControl.maxAge(30L, TimeUnit.DAYS).cachePublic())
                .resourceChain(true)
                .addResolver(new WebJarsResourceResolver());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/error").setViewName("error");
//        registry.addViewController("/login").setViewName("login");
//        registry.addViewController("/form").setViewName("form");
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ExtendedModelMap model) {
        return "redirect:/";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(ExtendedModelMap model) {
        model.addAttribute("contentView", "pageviews/landing");
        return "index";
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String projects(ExtendedModelMap model) {
        model.addAttribute("contentView", "pageviews/projects");
        return "index";
    }

    @RequestMapping(value = "/team", method = RequestMethod.GET)
    public String team(ExtendedModelMap model) {
        model.addAttribute("contentView", "pageviews/team");
        return "index";
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public String services(ExtendedModelMap model) {
        model.addAttribute("contentView", "pageviews/services");
        return "index";
    }


    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contact(ExtendedModelMap model) {
        model.addAttribute("contentView", "pageviews/contact");
        return "index";
    }


    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();

    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("contentView", "pageviews/errorpage");
        return "error";
    }

    @Component
    public class RequestProcessingTimeInterceptor extends HandlerInterceptorAdapter {

        private final Logger logger = LoggerFactory
                .getLogger(RequestProcessingTimeInterceptor.class);

        @Override
        public boolean preHandle(HttpServletRequest request,
                                 HttpServletResponse response, Object handler) throws Exception {
            request.setAttribute("crmName", crmName);
            request.setAttribute("crmCopyRight", crmCopyRight);
            request.setAttribute("crmCopyRightLink", crmCopyRightLink);
            request.setAttribute("crmEmailLink", crmEmailLink);
            request.setAttribute("crmRssLink", crmRssLink);
            request.setAttribute("crmInstagramLink", crmInstagramLink);
            request.setAttribute("crmFacebookLink", crmFacebookLink);
            request.setAttribute("crmTwitterLink", crmTwitterLink);

            request.setAttribute("crmAddressRoad", crmAddressRoad);
            request.setAttribute("crmAddressCity", crmAddressCity);
            request.setAttribute("crmAddressCountry", crmAddressCountry);
            request.setAttribute("crmAddressPostalCode", crmAddressPostalCode);
            request.setAttribute("crmAddressEmail", crmAddressEmail);
            request.setAttribute("crmAddressPhone", crmAddressPhone);

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

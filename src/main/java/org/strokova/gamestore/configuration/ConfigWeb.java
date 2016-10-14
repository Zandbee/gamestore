package org.strokova.gamestore.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.strokova.gamestore.controller.Controllers;
import org.strokova.gamestore.util.PathsManager;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * author: Veronika, 9/6/2016.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = Controllers.class)
public class ConfigWeb extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final String RESOURCE_PREFIX = "/WEB-INF/templates/";
    private static final String RESOURCE_SUFFIX = ".html";
    private static final String RESOURCE_MESSAGES_PREFIX = "/WEB-INF/messages/messages";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }


    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding(ENCODING_UTF_8);
        return resolver;
    }

    @Bean
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(templateResolver());
        engine.addDialect(new SpringSecurityDialect());
        return engine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix(RESOURCE_PREFIX);
        resolver.setSuffix(RESOURCE_SUFFIX);
        resolver.setTemplateMode(TemplateMode.HTML);

        resolver.setCacheable(false); // TODO: remove when release

        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(RESOURCE_MESSAGES_PREFIX);
        messageSource.setDefaultEncoding(ENCODING_UTF_8);

        messageSource.setCacheSeconds(10); // TODO: remove when release
        
        return messageSource;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
        registry.addResourceHandler("/files/**").addResourceLocations("file:///" + PathsManager.UPLOADS_DIR + "/");
        registry.addResourceHandler("/templates/**").addResourceLocations("/WEB-INF/templates/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}

package dev.coding.springboot.common.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfiguration {

    public static final String SLIDE_SHOW_DATA_CACHE_NAME = "slide-show-data-cache";
}

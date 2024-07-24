/*
 * Copyright 2022 Apollo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.github.oliver.apollo;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties processor
 *
 * @author licheng
 */
@ConditionalOnProperty("apollo.AutoRefreshConfigurationProperties")
public class SpringConfigurationPropertiesProcessor implements BeanPostProcessor, BeanFactoryAware {

  private static final Logger logger = LoggerFactory.getLogger(
      SpringConfigurationPropertiesProcessor.class);
  private static final String REFRESH_SCOPE_NAME = "org.springframework.cloud.context.config.annotation.RefreshScope";

  private BeanFactory beanFactory;

  public SpringConfigurationPropertiesProcessor() {
//    springConfigurationPropertyRegistry = SpringInjector.getInstance(SpringConfigurationPropertyRegistry.class);
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    Class<?> clazz = bean.getClass();
    ConfigurationProperties configurationPropertiesAnnotation = clazz.getDeclaredAnnotation(
        ConfigurationProperties.class);
    // match beans with annotated `@ConfigurationProperties` and `@ApolloConfigurationPropertiesRefresh`,
    // or `@ConfigurationProperties` and `@RefreshScope`
    if (configurationPropertiesAnnotation != null && annotatedRefresh(clazz)) {
      String prefix = configurationPropertiesAnnotation.prefix();
      // cache prefix and bean name
      beanFactory.getBean(SpringConfigurationPropertyRegistry.class)
          .register(this.beanFactory, prefix, beanName);
      logger.debug("Monitoring ConfigurationProperties bean {}", beanName);
    }
    return bean;
  }

  private boolean annotatedRefresh(Class<?> clazz) {
    ApolloConfigurationPropertiesRefresh apolloConfigurationPropertiesRefreshAnnotation = clazz.getDeclaredAnnotation(
        ApolloConfigurationPropertiesRefresh.class);
    return apolloConfigurationPropertiesRefreshAnnotation != null || isRefreshScope(
        clazz.getDeclaredAnnotations());
  }

  private boolean isRefreshScope(Annotation[] annotations) {
    return Arrays.stream(annotations).anyMatch(annotation -> annotation.annotationType().getName().equals(REFRESH_SCOPE_NAME));
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }
}

/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.com.integrity.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.spin.plugin.impl.SpinProcessEnginePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import ua.com.integrity.filter.HistoryFilter;
import ua.com.integrity.process.common.CreateTaskListener;

@Configuration
@EnableConfigurationProperties
public class Config extends WebMvcConfigurerAdapter {

    @Resource
    private HistoryFilter historyFilter;

    @Value("${camunda.database.url}")
    private String url;
    @Value("${camunda.database.user}")
    private String username;
    @Value("${camunda.database.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public ProcessEngineConfigurationImpl processEngineConfiguration() throws IOException {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(dataSource());
        config.setTransactionManager(transactionManager());
        config.setDatabaseSchemaUpdate("true");
        config.setJobExecutorActivate(true);
        config.setAuthorizationEnabled(true);
        config.setDefaultSerializationFormat("application/json");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        config.setDeploymentResources(resolver.getResources("classpath:/bpmn/*.bpmn"));
        List<ProcessEnginePlugin> processEnginePlugins = new ArrayList<>();
        SpinProcessEnginePlugin spinProcessEnginePlugin = new SpinProcessEnginePlugin();
        processEnginePlugins.add(spinProcessEnginePlugin);
        processEnginePlugins.add(CamundaReactor.plugin());
        config.setProcessEnginePlugins(processEnginePlugins);
        return config;
    }

    @Bean
    @Autowired
    public CreateTaskListener createTaskListener(ProcessEngine processEngine) {
        return new CreateTaskListener(processEngine);
    }

    @Bean
    public FilterRegistrationBean setRestApiFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(historyFilter);
        registration.addUrlPatterns("/api/engine/engine/default/task/*");
        registration.setName("historyFilter");
        registration.setOrder(Integer.MAX_VALUE - 1);
        return registration;
    }

}


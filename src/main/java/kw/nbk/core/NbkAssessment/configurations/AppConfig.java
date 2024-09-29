package kw.nbk.core.NbkAssessment.configurations;

import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hazelcast.config.Config;

@Configuration
public class AppConfig {

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance")
                .addMapConfig(new MapConfig()
                        .setName("productsCache")
                        .setTimeToLiveSeconds(3600)
                );
        return config;
    }

}

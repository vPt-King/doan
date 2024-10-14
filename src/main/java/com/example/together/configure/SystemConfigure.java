package com.example.together.configure;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfigure {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setDeepCopyEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(ctx -> ctx.getSource() != null);
        return modelMapper;
    }
}

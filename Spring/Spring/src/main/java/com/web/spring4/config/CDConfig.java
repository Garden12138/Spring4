package com.web.spring4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.web.spring4.bean.CompactDisc;
import com.web.spring4.bean.impl.JayChouAlbum;

@Configuration
public class CDConfig {
	
	@Bean
	public  CompactDisc jayChouAlbum(){
		return new JayChouAlbum();
	}
	
}

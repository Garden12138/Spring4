package com.web.spring4.bean.impl;

import org.springframework.stereotype.Component;

import com.web.spring4.bean.CompactDisc;

/**
 * CD实现类-周杰伦专辑
 * @author Garden
 * 2018年3月5日
 */
@Component
public class JayChouAlbum2 implements CompactDisc{

	private String songs[] = {"告白气球"};
	private String artist = "周杰伦";
	
	@Override
	public void play() {
		// TODO Auto-generated method stub
		System.out.println("正在播放："+ songs[0] + "-" +artist);
	}
	
}

package com.web.spring4.bean.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.web.spring4.annotation.JCA;
import com.web.spring4.annotation._2;
import com.web.spring4.bean.CompactDisc;
import com.web.spring4.bean.condition.ChooseJayChouAlbum;

/**
 * CD实现类-周杰伦专辑
 * @author Garden
 * 2018年3月5日
 */
@Component
//@Qualifier("JCA")
//@Qualifier("_2")
//@JCA
//@_2
public class JayChouAlbum2 implements CompactDisc{

	private String songs[] = {"告白气球"};
	private String artist = "周杰伦";
	
	@Override
	public void play() {
		// TODO Auto-generated method stub
		System.out.println("正在播放："+ songs[0] + "-" +artist);
	}
	
}

package com.web.spring4.bean.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.web.spring4.bean.MediaPlayer;
/**
 * 播放器实现类-CD播放器
 * @author Garden
 * 2018年3月5日
 */
@Component
public class CDPlayer implements MediaPlayer{

//	@Resource
	private JayChouAlbum cd1;
	
//	@Autowired /*@Resource不可用于构造函数*/
	public CDPlayer(JayChouAlbum cd1) {
		super();
		this.cd1 = cd1;
	}
	

	public CDPlayer() {
		super();
	}


	public JayChouAlbum getCd1() {
		return cd1;
	}

	@Resource /*setter构造必需含有空构造函数*/
	public void setCd1(JayChouAlbum cd1) {
		this.cd1 = cd1;
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		cd1.play();
	}

}

package com.web.spring4.bean.impl;

import org.springframework.stereotype.Component;

import com.web.spring4.bean.CompactDisc;

/**
 * CD实现类-周杰伦专辑
 * @author Garden
 * 2018年3月5日
 */
@Component
public class JayChouAlbum3 implements CompactDisc{

	private String songs;
	private String artist;
	
	public JayChouAlbum3() {
		super();
	}

	public JayChouAlbum3(String songs, String artist) {
		super();
		this.songs = songs;
		this.artist = artist;
	}
	
	public String getSongs() {
		return songs;
	}

	public void setSongs(String songs) {
		this.songs = songs;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		System.out.println("正在播放："+ songs + "-" +artist);
	}
	
}

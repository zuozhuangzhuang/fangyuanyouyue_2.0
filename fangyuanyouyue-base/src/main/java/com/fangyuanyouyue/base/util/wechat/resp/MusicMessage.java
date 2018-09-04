package com.fangyuanyouyue.base.util.wechat.resp;

/**
 * 音乐消息
 * 
 * @author wuzhimin  2014-2-27
 * 
 */
public class MusicMessage extends BaseMessage {
	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}
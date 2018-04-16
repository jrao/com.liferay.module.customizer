package com.liferay.blade.samples.servicebuilder.service.impl;

public class MySpringBean {
	
	public MySpringBean(String text) {
		_text = text;
	}
	
	public String getText() {
		return _text;
	}

	public void setText(String text) {
		_text = text;
	}

	private String _text;

}

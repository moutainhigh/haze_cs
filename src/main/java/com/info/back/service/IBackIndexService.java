package com.info.back.service;

import com.info.web.pojo.cspojo.IndexPage;

public interface IBackIndexService {
	public void update(IndexPage zbIndex);

	public IndexPage searchIndex();
}

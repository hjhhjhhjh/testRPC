package com.hjh.test.testRPC.producer;

import com.hjh.test.testRPC.api.ProductService;
import com.hjh.test.testRPC.common.Person;

public class Producer1 implements ProductService {

	@Override
	public Person queryById(String id) {
		if (!"123".equals(id)) {

			return null;
		}
		
		Person p = new Person();
		p.setAge(1);
		p.setName("用户" + id);
		return p;

	}

}

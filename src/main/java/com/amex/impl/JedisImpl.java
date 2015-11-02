package com.amex.impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Jedis")
public class JedisImpl {
	
	@RequestMapping("/read")
	public void read(){
		System.out.println("sss");
	}
	
	@RequestMapping("/write")
	public void write(){
		
	}

}

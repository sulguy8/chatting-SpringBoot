package com.bdi.sb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdi.sb.socket.WebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SocketController {

	@Resource
	private WebSocketHandler wsh;
	
	@GetMapping("/orderAlert")
	public Map<String,String> SendNotification(int viNum, int oiNum) {
		log.debug("viNum==>{}",viNum);
		Map<String,String> rMap = new HashMap<>();
		rMap.put("result", "False");
		try{
			rMap.put("result", "True");
			return rMap;
		}catch(Exception e){
			e.printStackTrace();
		}
		return rMap;
	}
} 

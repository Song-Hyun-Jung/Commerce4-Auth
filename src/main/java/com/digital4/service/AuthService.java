package com.digital4.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class AuthService {
	//Map<Token, <personId, loginTime>>
	private static Map<Long, Map> authMap = new HashMap<>();

	public long generateToken(long personId) throws Exception{
		long tokenId = System.currentTimeMillis();
		Thread.sleep(10);
		Map<Long, Long> loginMap = new HashMap<>(); //<personId, token생성시간>
		loginMap.put(personId, System.currentTimeMillis());
		 
		authMap.put(tokenId, loginMap); //<token, loginMap>
		return tokenId;
	}
	public boolean isValidToken(Long token) throws Exception{
		
		Map<Long, Long> loginMap = authMap.get(token);
		if(loginMap != null) {
			Set<Long> set = loginMap.keySet();
			if(set.size() != 0) {
				Iterator<Long> iterator = set.iterator();
				
				if (iterator.hasNext()) {
					long personId = iterator.next();
					long startTime = loginMap.get(personId);
					long currentTime = System.currentTimeMillis();
					long elapse = currentTime - startTime;
					
					if (elapse > 30 * 60 * 1000) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	public synchronized boolean updateValidTime (long token) throws Exception{
		
		Map<Long, Long> loginMap = authMap.get(token);
		System.out.println("이전 접속: " + loginMap );
		if(loginMap != null) {
			Set<Long> set = loginMap.keySet();
			Iterator<Long> iterator = set.iterator();
			
			if (iterator.hasNext()) {
				long personId = iterator.next();
				long currentTime = System.currentTimeMillis();
				loginMap.put(personId, currentTime);
				
				authMap.replace(token, loginMap);
				System.out.println("접속 갱신: " + loginMap);
				return true;
			}
		}
		return false;
	}
	
	public synchronized Map<Long, Map> getAuthMap() {
		return authMap;
	}

	public synchronized void setAuthMap(Map<Long, Map> authMap) {
		AuthService.authMap = authMap;
	}

	public synchronized Long getAuthPersonId(long token) throws Exception{ //token으로 personId 반환
		if(authMap.get(token) != null) {
			Iterator<Long> itr = authMap.get(token).keySet().iterator();
			
			return itr.next();
		}
		else {
			return (long) 0;
		}
	}
	
	
}

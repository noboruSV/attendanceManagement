package jp.co.softventure.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.softventure.domain.LoginData;
import jp.co.softventure.domain.SearchLoginData;
import jp.co.softventure.persistence.LoginDataMapper;

@Service
public class DBLoginDataService {
	
	@Autowired
	private LoginDataMapper mapper;
	
	// データ登録処理メソッド
		@Transactional
		public void insertLoginData(LoginData loginData) {
			// データ登録
			mapper.insert(loginData);
		}
		
		// データ検索処理メソッド
		public List<LoginData> searchLoginData(SearchLoginData searchLoginData) {
			// データ検索
			List<LoginData> list = mapper.select(searchLoginData);
			return list;
		}
}

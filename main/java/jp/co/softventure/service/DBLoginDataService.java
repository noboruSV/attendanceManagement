package jp.co.softventure.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.softventure.domain.LoginData;
import jp.co.softventure.persistence.LoginDataMapper;

/**
 * 
 * @author n.matsu
 * 2020/09/29 新規作成
 * 
 */
@Service
public class DBLoginDataService {
	
	@Autowired
	private LoginDataMapper mapper;
	
//	//登録処理
//	@Transactional
//	public void insert(LoginData loginData) {
//		// データ登録
//		mapper.insert(loginData);
//	}
		
	//検索処理
	public List<LoginData> selectLoginData(LoginData loginData) {
		List<LoginData> list = mapper.select(loginData);
		return list;
	}
	
	
	//更新処理
//	@Transactional
//	public void update(LoginData loginData) {
//		// データ登録
//		mapper.update(loginData);
//	}
	
	// add mtk start
	//オートログイン用認証文字列を更新
	public void updateAutoLogin(LoginData loginData) {
		mapper.updateAutoLogin(loginData);
	}
	
	public List<LoginData> selectLoginDataByAutoLogin(LoginData loginData) {
		List<LoginData> list = mapper.selectLoginDataByAutoLogin(loginData);
		return list;
	}
	//add mtk end
	
	// add mtk start
	//ログアウトページ用メソッド
	public List<LoginData> selectUserName(LoginData loginData) {
		List<LoginData> list = mapper.selectUserName(loginData);
		return list;
	}
	//add mtk end
	
}
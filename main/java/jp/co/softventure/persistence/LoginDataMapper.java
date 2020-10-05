package jp.co.softventure.persistence;

import java.util.List;

import jp.co.softventure.domain.LoginData;
import jp.co.softventure.domain.SearchLoginData;

public interface LoginDataMapper {
	
	public void insert(LoginData loginData);

	public List<LoginData> select(SearchLoginData searchLoginData);
	
	//ログアウトページ用メソッド
	public List<LoginData> selectUserName(LoginData loginData);
}
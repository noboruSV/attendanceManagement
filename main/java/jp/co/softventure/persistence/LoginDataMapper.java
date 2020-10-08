package jp.co.softventure.persistence;

import java.util.List;
import jp.co.softventure.domain.dto.LoginDto;

import jp.co.softventure.domain.LoginData;
import jp.co.softventure.domain.SearchLoginData;

/**
 * 
 * @author n.matsu
 * 2020/09/29 新規作成
 */
public interface LoginDataMapper {
	
	//登録
//	public void insert(LoginDto loginDto);
	
	//検索
	public List<LoginData> select(LoginData loginData);
	
	//更新
//	public void update(LoginDto loginDto);
	
	//ログアウトページ用メソッド
	public List<LoginData> selectUserName(LoginData loginData);
}
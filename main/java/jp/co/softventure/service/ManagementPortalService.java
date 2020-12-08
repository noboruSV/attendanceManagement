package jp.co.softventure.service;

import java.util.List;

//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import jp.co.softventure.bean.SessionBean;
import jp.co.softventure.domain.ManagementPortalInfo;
import jp.co.softventure.persistence.ManagementPortalInfoMapper;

@Service
public class ManagementPortalService {

	@Autowired
	private ManagementPortalInfoMapper mapper;
	
	@Autowired
	private SessionBean sessionBean;
	
	public List<ManagementPortalInfo> selectLoginData(ManagementPortalInfo managementPortalInfo) {
		List<ManagementPortalInfo> list = mapper.selectLoginData(managementPortalInfo);
		return list;
	}
	
	public List<ManagementPortalInfo> selectUserData(ManagementPortalInfo managementPortalInfo) {
		List<ManagementPortalInfo> list = mapper.selectUserData(managementPortalInfo);
		return list;
	}
	
	public List<ManagementPortalInfo> selectWorkingReport(ManagementPortalInfo managementPortalInfo) {
		List<ManagementPortalInfo> list = mapper.selectWorkingReport(managementPortalInfo);
		return list;
	}
	
	public List<ManagementPortalInfo> selectId() {
		List<ManagementPortalInfo> list = mapper.selectId();
		return list;
	}
	
	public void insertLoginData(ManagementPortalInfo managementPortalInfo) {
		mapper.insertLoginData(managementPortalInfo);
	}
	
	public boolean updateLoginData(ManagementPortalInfo managementPortalInfo) {
		//排他制御のために再度セレクト文をクエリする
		ManagementPortalInfo forMutualExclusion = new ManagementPortalInfo();
		List<ManagementPortalInfo> list = mapper.selectUserData(managementPortalInfo);
		if ( list.size() == 1 ) {
			forMutualExclusion = list.get(0);
			//画面表示時に取得した更新日時と現時点でのそれを比較する
			if ( StringUtils.equals(forMutualExclusion.getRecUpdtDt(), sessionBean.getRecUpdtDt()) ) {
				mapper.updateLoginData(managementPortalInfo);
				return true;
			}
		}
		return false;
	}
	
	public void updateEmploymentStatus(ManagementPortalInfo managementPortalInfo) {
		mapper.updateEmploymentStatus(managementPortalInfo);
	}
	
	public void deleteUserData(ManagementPortalInfo managementPortalInfo) {
		mapper.deleteUserData(managementPortalInfo);
	}
	
}
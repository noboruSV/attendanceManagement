package jp.co.softventure.persistence;

import java.util.List;

import jp.co.softventure.domain.*;

public interface ManagementPortalInfoMapper {
	
	public List<ManagementPortalInfo> selectLoginData(ManagementPortalInfo managementPortalInfo);
	public List<ManagementPortalInfo> selectUserData(ManagementPortalInfo managementPortalInfo);
	public List<ManagementPortalInfo> selectWorkingReport(ManagementPortalInfo managementPortalInfo);
	public List<ManagementPortalInfo> selectId();
	public void insertLoginData(ManagementPortalInfo managementPortalInfo);
	public void updateLoginData(ManagementPortalInfo managementPortalInfo);
	public void updateEmploymentStatus(ManagementPortalInfo managementPortalInfo);
	public void deleteUserData(ManagementPortalInfo managementPortalInfo);
	
}
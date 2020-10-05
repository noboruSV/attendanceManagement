package jp.co.softventure.persistence;

import java.util.List;

import jp.co.softventure.domain.*;

public interface RegistrationInfoMapper {
	
	public void insert(RegistrationInfo registrationInfo);
	public List<RegistrationInfo> selectWorkingTime(RegistrationInfo registrationInfo);
	public List<RegistrationInfo> selectWorkingDate(RegistrationInfo registrationInfo);
}
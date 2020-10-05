package jp.co.softventure.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.softventure.domain.*;
import jp.co.softventure.persistence.RegistrationInfoMapper;

@Service
public class RegistrationService {

	@Autowired
	private RegistrationInfoMapper mapper;
	
	public void insertRegistrationInfo(RegistrationInfo registrationInfo) {
		mapper.insert(registrationInfo);
	}
	
	public List<RegistrationInfo> selectWorkingTime(RegistrationInfo registrationInfo) {
		List<RegistrationInfo> list = mapper.selectWorkingTime(registrationInfo);
		return list;
	}
	
	public List<RegistrationInfo> selectWorkingDate(RegistrationInfo registrationInfo) {
		List<RegistrationInfo> list = mapper.selectWorkingDate(registrationInfo);
		return list;
	}
	
}
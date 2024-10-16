package com.easyeng.mschoi.service;

import com.easyeng.mschoi.model.dto.Member;

public interface AuthService {

	Member getMemberInfoById(String memberId);
}

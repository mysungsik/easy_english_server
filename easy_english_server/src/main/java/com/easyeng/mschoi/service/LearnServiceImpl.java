package com.easyeng.mschoi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.easyeng.mschoi.model.dao.LearnDAO;
import com.easyeng.mschoi.model.dto.Member;
import com.easyeng.mschoi.model.dto.WordData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearnServiceImpl implements LearnService {
	
	private final LearnDAO dao;

	@Override
	public WordData getCurrentWordForMemeber(int memberNo) {
		WordData result = dao.findCurrentWordDataByMemberId(memberNo);
		
		return result;
	}
}

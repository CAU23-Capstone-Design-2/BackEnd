package com.cau.vostom.celebrity.service;

import com.cau.vostom.celebrity.domain.Celebrity;
import com.cau.vostom.celebrity.dto.response.ResponseCelebrityDto;
import com.cau.vostom.celebrity.dto.response.ResponsePlayListDto;
import com.cau.vostom.celebrity.repository.CelebrityRepository;
import com.cau.vostom.user.domain.User;
import com.cau.vostom.util.api.ResponseCode;
import com.cau.vostom.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CelebrityService {

    private final CelebrityRepository celebrityRepository;

    //노래 재생

    //pretrained 연예인 리스트 보기
    @Transactional(readOnly = true)
    public List<ResponseCelebrityDto> getCelebrityList() {
        List<Celebrity> celebrity = celebrityRepository.findAll();

        return celebrity.stream()
                .map(ResponseCelebrityDto::from)
                .distinct()
                .collect(Collectors.toList());
    }

    //특정 연예인 노래 리스트 보기
    @Transactional(readOnly = true)
    public List<ResponsePlayListDto> getPlayList(String celebrityName) {
        List<Celebrity> celebrity = getCelebrityByName(celebrityName);

        return celebrity.stream()
                .map(ResponsePlayListDto::from)
                .collect(Collectors.toList());
    }

    private List<Celebrity> getCelebrityByName(String celebrityName) {
        return celebrityRepository.findByCelebrityName(celebrityName);
    }

}

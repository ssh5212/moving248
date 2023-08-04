package com.ssafy.move.service;

import com.ssafy.move.domain.*;
import com.ssafy.move.dto.request.ApplyFormRequestDto;
import com.ssafy.move.dto.response.*;
import com.ssafy.move.repository.ApplyFormRepository;
import com.ssafy.move.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyFormService {

    private final ApplyFormRepository applyFormRepository;
    private final SuggestionRepository suggestionRepository;

    // 신청서 유무
    @Transactional
    public boolean existForm(int uId){
        List<ApplyForm> applyForms = applyFormRepository.existForm(uId);

        System.out.println(applyForms.size());

        if (applyForms.size()==0)
            return true;
        else
            return false;
    }

    //신청서 작성
    @Transactional
    public void writeForm(ApplyFormRequestDto applyFormRequestDto) {

        ApplyForm applyForm = new ApplyForm();

        Members member = applyFormRepository.findMemberById(applyFormRequestDto.getM_id());

        applyForm.setUId(member);
        applyForm.setFCategory(applyFormRequestDto.getF_category());
        applyForm.setFDate(applyFormRequestDto.getF_date());
        applyForm.setFDepSido(applyFormRequestDto.getF_dep_sido());
        applyForm.setFDepGungu(applyFormRequestDto.getF_dep_gungu());
        applyForm.setFDepEv(applyFormRequestDto.getF_dep_ev());
        applyForm.setFDepLadder(applyFormRequestDto.getF_dep_ladder());
        applyForm.setFArrSido(applyFormRequestDto.getF_arr_sido());
        applyForm.setFArrGungu(applyFormRequestDto.getF_arr_gungu());
        applyForm.setFArrEv(applyFormRequestDto.getF_arr_ev());
        applyForm.setFArrLadder(applyFormRequestDto.getF_arr_ladder());
        applyForm.setFRoomVideoUrl(applyFormRequestDto.getVideo_File());
        applyForm.setFReqDesc(applyFormRequestDto.getF_req_desc());


        applyFormRepository.save(applyForm);
    }


    // 신청서 수정
    @Transactional
    public void updateApplyForm(int f_id, ApplyFormRequestDto applyFormRequestDto){

        ApplyForm applyForm = applyFormRepository.findApplyFormById(f_id);

        applyForm.setFCategory(applyFormRequestDto.getF_category());
        applyForm.setFDate(applyFormRequestDto.getF_date());
        applyForm.setFDepSido(applyFormRequestDto.getF_dep_sido());
        applyForm.setFDepGungu(applyFormRequestDto.getF_dep_gungu());
        applyForm.setFDepEv(applyFormRequestDto.getF_dep_ev());
        applyForm.setFDepLadder(applyFormRequestDto.getF_dep_ladder());
        applyForm.setFArrSido(applyFormRequestDto.getF_arr_sido());
        applyForm.setFArrGungu(applyFormRequestDto.getF_arr_gungu());
        applyForm.setFArrEv(applyFormRequestDto.getF_arr_ev());
        applyForm.setFArrLadder(applyFormRequestDto.getF_arr_ladder());
        applyForm.setFRoomVideoUrl(applyFormRequestDto.getVideo_File());
        applyForm.setFReqDesc(applyFormRequestDto.getF_req_desc());
        applyForm.setFCreateTime(applyForm.getFCreateTime());
        applyForm.setFModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        applyFormRepository.updateApplyForm(applyForm);
        //applyFormRepository.save(applyForm);

    }

    // 신청서 삭제
    @Transactional
    public void deleteApplyForm(int f_id){

        applyFormRepository.deleteApplyForm(f_id);

    }

    // 신청서 전체조회
    @Transactional
    public List<ApplyFormResponseDto> findAll(){

        List<ApplyFormResponseDto> list = new ArrayList<>();

        //List<ApplyFormWithStatus> allForm = applyFormRepository.findAll();

        List<Tuple> allForm = applyFormRepository.findAll();


        for(Tuple af : allForm){

            ApplyFormResponseDto applyFormResponseDto = new ApplyFormResponseDto();

            ApplyForm applyForm = af.get(0,ApplyForm.class);
            FormStatus formStatus = af.get(1, FormStatus.class);
            MoveCategory moveCategory = af.get(2, MoveCategory.class);


            applyFormResponseDto.setF_id(applyForm.getId());
            applyFormResponseDto.setF_status(formStatus.getStatusName());
            applyFormResponseDto.setF_date(applyForm.getFDate());
            applyFormResponseDto.setF_dep_sido(applyForm.getFDepSido());
            applyFormResponseDto.setF_dep_gungu(applyForm.getFDepGungu());
            applyFormResponseDto. setF_arr_sido(applyForm.getFArrSido());
            applyFormResponseDto.setF_arr_gungu(applyForm.getFArrGungu());
            applyFormResponseDto.setF_category(moveCategory.getCategoryName());

            list.add(applyFormResponseDto);

        }
        return list;
    }

    // 카테고리별 신청서 조회
    @Transactional
    public List<ApplyFormResponseDto> findApplyByCategory(String sido, String gungu, int category, int pId){

        List<ApplyFormResponseDto> list = new ArrayList<>();

        List<Tuple> option = applyFormRepository.findByOption(sido, gungu, category, pId);


        for(Tuple af : option){

            ApplyForm applyForm = af.get(0,ApplyForm.class);
            FormStatus formStatus = af.get(1, FormStatus.class);
            MoveCategory moveCategory = af.get(2, MoveCategory.class);


            ApplyFormResponseDto applyFormResponseDto = new ApplyFormResponseDto(

                    applyForm.getId(), formStatus.getStatusName(), applyForm.getFDate(),
                    applyForm.getFDepSido(), applyForm.getFDepGungu(), applyForm.getFArrSido(),
                    applyForm.getFArrGungu(), moveCategory.getCategoryName()
            );
            list.add(applyFormResponseDto);
        }
        return list;
    }

    // 신청서 상세조회
    public DetailApplyFormResponseDto findDetailApplyById(int fId){

        List<Suggestion> suggestionList = suggestionRepository.findAllSuggestionByFid(fId);

        List<Tuple> detail = applyFormRepository.findDetailApplyById(fId);

        DetailApplyFormResponseDto detailApply = new DetailApplyFormResponseDto();

        for(Tuple af : detail){

            ApplyForm applyForm = af.get(0, ApplyForm.class);
            FormStatus formStatus = af.get(1, FormStatus.class);
            MoveCategory moveCategory = af.get(2, MoveCategory.class);

            detailApply.setF_id(applyForm.getId());
            detailApply.setU_id(applyForm.getUId().getId());
            detailApply.setP_id(applyForm.getPId().getId());
            detailApply.setF_category(moveCategory.getCategoryName());
            detailApply.setF_date(applyForm.getFDate());
            detailApply.setF_dep_sido(applyForm.getFDepSido());
            detailApply.setF_dep_gungu(applyForm.getFDepGungu());
            detailApply.setF_dep_ev(applyForm.getFDepEv());
            detailApply.setF_dep_ladder(applyForm.getFDepLadder());
            detailApply.setF_arr_sido(applyForm.getFArrSido());
            detailApply.setF_arr_gungu(applyForm.getFArrGungu());
            detailApply.setF_arr_ev(applyForm.getFArrEv());
            detailApply.setF_arr_ladder(applyForm.getFArrLadder());
            detailApply.setF_room_video_url(applyForm.getFRoomVideoUrl());
            detailApply.setF_req_desc(applyForm.getFReqDesc());
            detailApply.setF_status(formStatus.getStatusName());
        }

        List<DetailSuggestionResponseDto> suggestionResponseDtoList = new ArrayList<>();

        for(Suggestion s : suggestionList){

            DetailSuggestionResponseDto detailSuggestion = new DetailSuggestionResponseDto();

            detailSuggestion.setP_id(s.getPId().getId());
            detailSuggestion.setName(s.getPId().getName());
            detailSuggestion.setProfile_url(s.getPId().getProfileUrl());
            detailSuggestion.setP_move_cnt(s.getPId().getPMoveCnt());
            detailSuggestion.setS_money(s.getSMoney());
            detailSuggestion.setS_desc(s.getSDesc());
            detailSuggestion.setS_create_time(s.getSModifyTime());

            suggestionResponseDtoList.add(detailSuggestion);
        }

        detailApply.setList(suggestionResponseDtoList);


        return detailApply;
    }


    // 시도 가져오기
    @Transactional
    public List<SidoResponseDto> getSido(){

        List<Sido> sido = applyFormRepository.getSido();

        List<SidoResponseDto> list = new ArrayList<>();

        for(Sido si : sido){

            SidoResponseDto sidoResponseDto = new SidoResponseDto(si.getSidoCode(),si.getSidoName());

            list.add(sidoResponseDto);

        }

        return list;
    }

    // 군구 가져오기
    @Transactional
    public List<GuResponseDto> getGu(String sido){

        List<Gu> gu = applyFormRepository.getGu(sido);

        List<GuResponseDto> list = new ArrayList<>();

        for(Gu g : gu){

            GuResponseDto guResponseDto = new GuResponseDto(g.getGuCode(), g.getGuName());
            list.add(guResponseDto);
        }

        return list;
    }

    // 신청서 상태 수정
    @Transactional
    public void updateFormStatus(int fId){
        applyFormRepository.updateFormStatus(fId);
    }


}
package study;

import domain.Study;
import member.MemberService;

import java.lang.reflect.Member;

public class StudyService {

    private final MemberService memberService;

    private final StudyRepository studyRepository;

    public StudyService(MemberService memberService, StudyRepository studyRepository) {
        this.memberService = memberService;
        this.studyRepository = studyRepository;
    }

//    public Study createNewStudy(Long memberId, Study study) {
//        Member member = memberService.findById(memberId);
//        if(member == null){
//            throw new IllegalArgumentException("Member doesn't exist for id: '" + member);
//        }
//        study.setOwner(member);
//
//        return studyRepository.save(study);
//    }
}

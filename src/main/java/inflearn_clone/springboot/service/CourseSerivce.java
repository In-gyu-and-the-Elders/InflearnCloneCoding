package inflearn_clone.springboot.service;

import inflearn_clone.springboot.dto.course.CourseDTO;

import java.util.List;

public interface CourseSerivce {
    // 강의 목록 가져오기
    List<CourseDTO> courseList();
    // 강의 상세 정보 가져오기
    CourseDTO courseView(int idx);

    //아이디에 해당하는 강의 정보 확인하기(관리자 사용)
    List<Integer> selectCourseByMemberId(String memberId);

    boolean deleteCourseByMemberId(String memberId);

    //삭제 대상 강의 idx 가져옴

    //삭제 대상 강의 삭제
}

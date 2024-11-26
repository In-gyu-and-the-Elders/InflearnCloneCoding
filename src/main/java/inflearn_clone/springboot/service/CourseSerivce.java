package inflearn_clone.springboot.service;

import inflearn_clone.springboot.dto.course.CourseDTO;

import java.util.List;

public interface CourseSerivce {
    // 강의 목록 가져오기
    List<CourseDTO> courseList();
    // 강의 상세 정보 가져오기
    CourseDTO courseView(int idx);
}
package inflearn_clone.springboot.service.lessonStatus;

public interface LessonStatusService {
    void updateLessonStatus(int lessonIdx, String memberId);
    boolean studyCheck(int courseIdx, String memberId);
}

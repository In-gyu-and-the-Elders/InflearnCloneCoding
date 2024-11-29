package inflearn_clone.springboot.service.like;


public interface LikeService {
    void toggleLike(int courseIdx, String memberId);
    int getLikeCount(int courseIdx);
    boolean likeCheck(int courseIdx, String memberId);
}

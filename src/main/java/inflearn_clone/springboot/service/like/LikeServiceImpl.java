package inflearn_clone.springboot.service.like;

import inflearn_clone.springboot.mappers.LikeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Log4j2
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeMapper likeMapper;

    @Override
    public void toggleLike(int courseIdx, String memberId) {
        if (likeMapper.likeCheck(courseIdx, memberId) > 0) {
            likeMapper.delete(courseIdx, memberId);
        } else {
            likeMapper.regist(courseIdx, memberId);
        }
    }

    @Override
    public int getLikeCount(int courseIdx) {
        return likeMapper.likeCnt(courseIdx);
    }

    @Override
    public boolean likeCheck(int courseIdx, String memberId) {
        return likeMapper.likeCheck(courseIdx, memberId) > 0;
    }
}

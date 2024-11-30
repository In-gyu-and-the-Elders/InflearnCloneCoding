package inflearn_clone.springboot.utils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class ValidateList {
    public static boolean validateMemberListParameters(int pageNo, String searchCategory,
                                                       String searchValue, String sortType, String sortOrder, HttpServletResponse response) {
        if (pageNo < 1) {
            JSFunc.alertBack("페이지 번호는 1 이상이어야 합니다.", response);
            return false;
        }

        if (searchCategory != null && !searchCategory.trim().isEmpty()
                && searchValue != null && !searchValue.trim().isEmpty()) {
            if (!("name".equals(searchCategory) || "memberId".equals(searchCategory)
                    || "email".equals(searchCategory) || "status".equals(searchCategory))) {
                JSFunc.alertBack("유효하지 않은 검색 카테고리입니다: " + searchCategory, response);
                return false;
            }
        }
        if (sortType != null && !sortType.trim().isEmpty()
                && sortOrder != null && !sortOrder.trim().isEmpty()) {
            if (!("regDate".equals(sortType) || "name".equals(sortType)
                    || "phone".equals(sortType))) {
                JSFunc.alertBack("유효하지 않은 정렬 조건입니다: " + sortType, response);
                return false;
            }
        }
        if (sortOrder != null && !sortOrder.trim().isEmpty()) {
            if (!("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder))) {
                JSFunc.alertBack("유효하지 않은 정렬 순서입니다: " + sortOrder, response);
                return false;
            }
        }
        return true;
    }
}


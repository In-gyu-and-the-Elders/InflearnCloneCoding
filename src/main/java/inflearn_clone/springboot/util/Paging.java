package inflearn_clone.springboot.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Paging {
    private int pageNo;
    private int pageSize;
    private int blockSize;
    private int totalCnt;
    private String sortType;
    private String sortOrder;


    public int getStartIdx(){
        return  (pageNo - 1) * pageSize;
    }

    public int getTotalPage(){
        return  (int) Math.ceil((double) totalCnt / pageSize);
    }
    public int getStartBlockPage(){
        return ((pageNo - 1) / blockSize) * blockSize + 1;
    }
    public int getEndBlockPage() {
        int endPage = getStartBlockPage() + blockSize - 1;
        return Math.min(endPage, getTotalPage());
    }
    public boolean getPrevBlock () {
        return getStartBlockPage() > 1;
    }
    public boolean getNextBlock () {
        return getEndBlockPage() < getTotalPage();
    }

    // 정렬조건
    public String getSortType(){
        if(sortType == null && sortType.trim().isEmpty()){
            return "";
        }
        String direction  = (sortOrder != null && sortOrder.equals("DESC")) ? "DESC" : "ASC";
        return "ORDER BY " + sortType + " " + direction ;
    }
}
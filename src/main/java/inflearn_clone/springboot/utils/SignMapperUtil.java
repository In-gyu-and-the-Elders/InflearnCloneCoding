package inflearn_clone.springboot.utils;

import inflearn_clone.springboot.domain.SignVO;
import inflearn_clone.springboot.dto.sign.SignDTO;

public class SignMapperUtil {

  public static SignVO toVO(SignDTO dto) {
    if (dto == null) return null;
    
    return SignVO.builder()
        .memberId(dto.getMemberId())
        .pwd(dto.getPwd())
        .name(dto.getName())
        .phone(dto.getPhone())
        .email(dto.getEmail())
        .status(dto.getStatus())
        .memberType(dto.getMemberType())
        .build();
  }

  public static SignDTO toDTO(SignVO vo) {
    if (vo == null) return null;
    
    return SignDTO.builder()
        .memberId(vo.getMemberId())
        .pwd(vo.getPwd())
        .name(vo.getName())
        .phone(vo.getPhone())
        .email(vo.getEmail())
        .status(vo.getStatus())
        .memberType(vo.getMemberType())
        .build();
  }
}

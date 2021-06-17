package sigma.training.ctp.mapper;

import org.springframework.stereotype.Component;
import sigma.training.ctp.dto.AuditTrailDto;
import sigma.training.ctp.persistence.entity.AuditTrail;
import sigma.training.ctp.persistence.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditTrailMapper implements Mapper<AuditTrail, AuditTrailDto> {

  @Override
  public AuditTrailDto toRestDto(AuditTrail auditTrail) {
    return new AuditTrailDto(
      auditTrail.getId(),
      auditTrail.getDate(),
      auditTrail.getUser().getId(),
      auditTrail.getDescription());

  }

  @Override
  public AuditTrail toEntity(AuditTrailDto auditTrailDto) {
    UserEntity user = new UserEntity();
    user.setId(auditTrailDto.getUserId());
    return new AuditTrail(auditTrailDto.getId(), auditTrailDto.getDate(),user,auditTrailDto.getDescription());
  }

  public List<AuditTrailDto> toRestDto(List<AuditTrail> auditTrailList) {
    return auditTrailList.stream().map(
      auditTrail -> toRestDto(auditTrail)).collect(Collectors.toList());

  }
}

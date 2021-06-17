package sigma.training.ctp.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sigma.training.ctp.dto.AuditTrailDto;
import sigma.training.ctp.persistence.entity.AuditTrail;
import sigma.training.ctp.persistence.entity.UserEntity;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditTrailMapperTest {
  private static final UserEntity USER = new UserEntity();
  private static final Long ID = 1L;
  private static final AuditTrail AUDIT_TRAIL = new AuditTrail(1L, Instant.now(), USER, "ok");
  private static final AuditTrailDto AUDIT_TRAIL_DTO = new AuditTrailDto(1L, AUDIT_TRAIL.getDate(), ID, "ok");

  AuditTrailMapper auditTrailMapper;

  @BeforeEach
  void setUp() {
    auditTrailMapper = new AuditTrailMapper();
    USER.setId(ID);
  }

  @Test
  void toRestDto() {
    AuditTrailDto auditTrailDto = auditTrailMapper.toRestDto(AUDIT_TRAIL);
    assertEquals(AUDIT_TRAIL_DTO,auditTrailDto);
  }

  @Test
  void toEntity() {
    AuditTrail auditTrail = auditTrailMapper.toEntity(AUDIT_TRAIL_DTO);
    assertEquals(AUDIT_TRAIL,auditTrail);
  }
}

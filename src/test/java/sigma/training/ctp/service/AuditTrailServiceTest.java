package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sigma.training.ctp.dto.AuditTrailDto;
import sigma.training.ctp.mapper.AuditTrailMapper;
import sigma.training.ctp.persistence.entity.AuditTrail;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.repository.AuditTrailRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuditTrailServiceTest {

  private static final List<AuditTrail> AUDIT_TRAIL_LIST = new ArrayList<>();
  private static final List<AuditTrailDto> AUDIT_TRAIL_LIST_DTO = new ArrayList<>();
  private static final UserEntity USER = new UserEntity();
  private static final Long ID = 1L;
  private static final AuditTrail AUDIT_TRAIL_1 = new AuditTrail(1L, Instant.now(),USER, "ok1");
  private static final AuditTrail AUDIT_TRAIL_2 = new AuditTrail(2L, Instant.now(),USER, "ok2");
  private static final AuditTrailDto AUDIT_TRAIL_DTO_1 = new AuditTrailDto(1L, AUDIT_TRAIL_1.getDate(),ID, "ok1");
  private static final AuditTrailDto AUDIT_TRAIL_DTO_2 = new AuditTrailDto(2L, AUDIT_TRAIL_2.getDate(),ID, "ok2");


  @Mock
  AuditTrailRepository auditTrailRepository;
  @Mock
  AuditTrailMapper auditTrailMapper;

  @InjectMocks
  AuditTrailService auditTrailService;


  @Test
  void getAllAuditTrails() {
    AUDIT_TRAIL_LIST.add(AUDIT_TRAIL_1);
    AUDIT_TRAIL_LIST.add(AUDIT_TRAIL_2);
    AUDIT_TRAIL_LIST_DTO.add(AUDIT_TRAIL_DTO_1);
    AUDIT_TRAIL_LIST_DTO.add(AUDIT_TRAIL_DTO_2);
    when(auditTrailRepository.findAll()).thenReturn(AUDIT_TRAIL_LIST);
    when(auditTrailMapper.toRestDto(AUDIT_TRAIL_LIST)).thenReturn(AUDIT_TRAIL_LIST_DTO);
    assertEquals(AUDIT_TRAIL_LIST_DTO,auditTrailService.getAllAuditTrails());
  }
}

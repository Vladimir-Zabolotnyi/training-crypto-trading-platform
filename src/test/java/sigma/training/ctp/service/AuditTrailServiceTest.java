package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
  private static final UserEntity USER = new UserEntity();
  private static final AuditTrail AUDIT_TRAIL_1 = new AuditTrail(1L, Instant.now(),USER, "ok1");
  private static final AuditTrail AUDIT_TRAIL_2 = new AuditTrail(2L, Instant.now(),USER, "ok2");


  @Mock
  AuditTrailRepository auditTrailRepository;

  @InjectMocks
  AuditTrailService auditTrailService;


  @Test
  void getAllAuditTrails() {
    AUDIT_TRAIL_LIST.add(AUDIT_TRAIL_1);
    AUDIT_TRAIL_LIST.add(AUDIT_TRAIL_2);
    when(auditTrailRepository.findAll()).thenReturn(AUDIT_TRAIL_LIST);
    assertEquals(AUDIT_TRAIL_LIST,auditTrailService.getAllAuditTrails());
  }
}

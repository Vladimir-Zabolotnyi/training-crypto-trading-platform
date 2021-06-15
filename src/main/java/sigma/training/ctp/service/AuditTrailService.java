package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.AuditTrailDto;
import sigma.training.ctp.mapper.AuditTrailMapper;
import sigma.training.ctp.persistence.entity.AuditTrail;
import sigma.training.ctp.persistence.repository.AuditTrailRepository;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class AuditTrailService {

  @Autowired
  AuditTrailRepository auditTrailRepository;
  @Autowired
  AuditTrailMapper auditTrailMapper;

  @Transactional
  public List<AuditTrailDto> getAllAuditTrails() {
    return auditTrailMapper.toRestDto(auditTrailRepository.findAll());
  }
}

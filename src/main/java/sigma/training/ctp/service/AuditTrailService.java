package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.persistence.entity.AuditTrail;
import sigma.training.ctp.persistence.repository.AuditTrailRepository;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class AuditTrailService {

  @Autowired
  AuditTrailRepository auditTrailRepository;



  @Transactional
  public List<AuditTrail> getAllAuditTrails() {
    return auditTrailRepository.findAll();
  }
}

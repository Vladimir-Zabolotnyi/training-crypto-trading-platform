package sigma.training.ctp.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.exception.InvalidEntityIdException;
import sigma.training.ctp.persistence.entity.UserWalletEntity;
import sigma.training.ctp.persistence.repository.UserWalletRepository;

@Service
public class UserWalletService {

    @Autowired
    private UserWalletRepository repository;

    public UserWalletEntity getWalletByUserId(Long id) {
        if (id != null) {
            if (id > 0) {
                return repository.findUserWalletEntityByUserId(id);
            }
            else {
                String message = "The user id is incorrect or hasn't been proposed";
                throw new InvalidEntityIdException(message);
            }
        }
        else {
            String message = "The user id has no reference to an object";
            throw new NullPointerException(message);
        }
    }
}
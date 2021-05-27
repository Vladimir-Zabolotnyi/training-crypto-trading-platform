package sigma.training.ctp.service;

import com.sun.tools.javac.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.exception.InvalidEntityIdException;
import sigma.training.ctp.model.UserWalletModel;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.UserWalletEntity;
import sigma.training.ctp.persistence.repository.UserWalletRepository;

import java.math.BigDecimal;

@Service
public class UserWalletService {

    @Autowired
    private UserWalletRepository repository;

    public List<UserWalletEntity> getAllWalletsByUser(UserEntity user) {
        if (user != null) {
            if (user.getId() != null && user.getId() > 0) {
                return repository.findAllByUser(user);
            }
            else {
                String message = "The user id is incorrect or hasn't been proposed";
                throw new InvalidEntityIdException(message);
            }
        }
        else {
            String message = "The user has no reference to an object UserEntity";
            throw new NullPointerException(message);
        }
    }
}
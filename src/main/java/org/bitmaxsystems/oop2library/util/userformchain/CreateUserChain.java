package org.bitmaxsystems.oop2library.util.userformchain;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.UserRepository;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Date;

public class CreateUserChain implements IUserFormChain {

    private IUserFormChain nextChain;
    private UserRepository userRepository = UserRepository.getInstance();


    @Override
    public void setNextChain(IUserFormChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(UserDataDTO formData) throws Exception {

            int count = userRepository.countExistingUsersByUsername(formData.getUsernameField());

            if (count>0)
            {
                throw new DataAlreadyExistException("- A user with this username already exists!");
            }
            else {
                formData.setUser(createUser(formData));

                if (nextChain != null)
                {
                    nextChain.execute(formData);
                }

        }
    }

    private User createUser (UserDataDTO formData)
    {
        int age;
        User user;
        Credentials credentials;
        try
        {
            age = Integer.parseInt(formData.getAge());
        }
        catch (NumberFormatException _e)
        {
            throw new NumberFormatException("Error parsing age field");
        }

        User.Builder userBuilder =  new User.Builder(formData.getFirstName().strip(),
                formData.getLastName().strip(),
                age,
                formData.getPhoneField().strip(),
                formData.getRole());

        if (formData.getRole() != UserRole.UNAPPROVED_READER)
        {
            userBuilder.setDateOfApproval(new Date());
        }

        user = userBuilder.build();
        credentials = new Credentials(formData.getUsernameField().strip(),
                BCrypt.hashpw(formData.getPasswordField().strip(), BCrypt.gensalt()),
                user);

        userRepository.createUserInDatabase(user,credentials);
//        Transaction transaction = null;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            userGenericRepository.save(user);
//
//
//
//            credentialsGenericRepository.save(credentials);
//        }

        return user;
    }
}

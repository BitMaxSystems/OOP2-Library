package org.bitmaxsystems.oop2library.util.userformchain;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.exceptions.UserAlreadyExistException;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class CreateUserChain implements IUserFormChain {

    private IUserFormChain nextChain;
    private GenericRepository<Credentials> credentialsRepository = new GenericRepository<>(Credentials.class);
    private GenericRepository<User> userRepository = new GenericRepository<>(User.class);


    @Override
    public void setNextChain(IUserFormChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(UserDataDTO formData) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Credentials where username =:username ", Long.class);
            query.setParameter("username",formData.getUsernameField());
            int count = Math.toIntExact(query.getSingleResult());

            if (count>0)
            {
                throw new UserAlreadyExistException("- A user with this username already exists!");
            }
            else {
                formData.setUser(createUser(formData));

                if (nextChain != null)
                {
                    nextChain.execute(formData);
                }
            }
        }
    }

    private User createUser (UserDataDTO formData)
    {
        Integer age;
        User user;
        Credentials credentials;
        try
        {
            age = Integer.parseInt(formData.getAge());
        }
        catch (NumberFormatException _e)
        {
            age = null;
        }

        User.Builder userBuilder =  new User.Builder(formData.getFirstName().strip(),
                formData.getLastName().strip(),
                formData.getPhoneField().strip(),
                UserRole.UNAPPROVED_USER);

        if (age != null)
        {
            userBuilder.setAge(age);
        }

        user = userBuilder.build();

        userRepository.save(user);

        credentials = new Credentials(formData.getUsernameField().strip(),
                BCrypt.hashpw(formData.getPasswordField().strip(),BCrypt.gensalt()),
                user);

        credentialsRepository.save(credentials);

        return user;
    }
}

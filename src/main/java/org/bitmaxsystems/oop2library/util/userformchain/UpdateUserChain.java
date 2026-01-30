package org.bitmaxsystems.oop2library.util.userformchain;

import org.bitmaxsystems.oop2library.models.form.UserFormDTO;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;

public class UpdateUserChain implements IUserFormChain {
    private IUserFormChain nextChain;
    private GenericRepository<User> userRepository = new GenericRepository<>(User.class);

    @Override
    public void setNextChain(IUserFormChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(UserFormDTO formData) throws Exception {
        int age, loyaltyPoints;
        User user  = formData.getUser();

        user.setFirstName(formData.getFirstName());
        user.setLastName(formData.getLastName());

        try
        {
            age = Integer.parseInt(formData.getAge());

        }
        catch (NumberFormatException e)
        {
            age = 0;
        }
        user.setAge(age);
        try
        {
            loyaltyPoints = Integer.parseInt(formData.getLoyaltyPoints());
            user.setLoyaltyPoints(loyaltyPoints);
        }
        catch (NumberFormatException e)
        {
            throw new NumberFormatException("Error parsing loyalty points field");
        }

        userRepository.update(user);

        if (nextChain != null)
        {
            nextChain.execute(formData);
        }
    }
}

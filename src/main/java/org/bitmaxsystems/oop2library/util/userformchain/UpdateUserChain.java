package org.bitmaxsystems.oop2library.util.userformchain;

import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
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
    public void execute(UserDataDTO formData) throws Exception {
        int age, loyaltyPoints;
        User user  = formData.getUser();

        user.setFirstName(formData.getFirstName());
        user.setLastName(formData.getLastName());
        user.setPhone(formData.getPhoneField());
        user.setRole(formData.getRole());
        try
        {
            age = Integer.parseInt(formData.getAge());

        }
        catch (NumberFormatException e)
        {
            throw new NumberFormatException("Error parsing age field");
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

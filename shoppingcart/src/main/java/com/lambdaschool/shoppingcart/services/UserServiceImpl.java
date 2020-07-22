package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.exceptions.ResourceFoundException;
import com.lambdaschool.shoppingcart.exceptions.ResourceNotFoundException;
import com.lambdaschool.shoppingcart.models.Role;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.models.UserRoles;
import com.lambdaschool.shoppingcart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "userService")
public class UserServiceImpl
        implements UserService
{
    /**
     * Connects this service to the users repository
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Connects this service to the Role table
     */
    @Autowired
    private RoleService roleService;

    /**
     * Connects this service to the Cart table
     */
    @Autowired
    private CartService cartService;

    @Override
    public List<User> findAll()
    {
        List<User> list = new ArrayList<>();
        /*
         * findAll returns an iterator set.
         * iterate over the iterator set and add each element to an array list.
         */
        userRepository.findAll()
                .iterator()
                .forEachRemaining(list::add);
        return list;
    }

    @Override
    public User findUserById(long id)
    {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found!"));
    }

    @Override
    public User findByName(String name) {

        User user = userRepository.findByUsername(name.toLowerCase());

        if (user == null) {
            throw new ResourceNotFoundException("Username " + name + " not found");
        }

        return user;
    }

    @Transactional
    @Override
    public void delete(long id)
    {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found!"));
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User save(User user)
    {
        User newUser = new User();

        if (user.getUserid() != 0) {
            User oldUser = userRepository.findById(user.getUserid())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User id " + user.getUserid() + " not found"));
            newUser.setUserid(user.getUserid());
        }

        newUser.setUsername(user.getUsername());
        newUser.setPasswordNoEncrypt(user.getPassword());
        newUser.setComments(user.getComments());

        newUser.getRoles()
                .clear();
        for (UserRoles ur : user.getRoles())
        {
            Role addRole = roleService.findRoleById(ur.getRole()
                    .getRoleid());

            newUser.getRoles()
                    .add(new UserRoles(newUser, addRole));
        }


        if (user.getCarts()
                .size() > 0)
        {
            throw new ResourceFoundException("Carts are not added through users");
        }
        return userRepository.save(newUser);
    }
}

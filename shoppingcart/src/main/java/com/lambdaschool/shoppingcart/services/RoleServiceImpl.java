package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.exceptions.ResourceFoundException;
import com.lambdaschool.shoppingcart.exceptions.ResourceNotFoundException;
import com.lambdaschool.shoppingcart.models.Role;
import com.lambdaschool.shoppingcart.repositories.RoleRepository;
import com.lambdaschool.shoppingcart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the RoleService Interface
 */
@Transactional
@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {
    /**
     * Connects this service to the Role Model
     */
    @Autowired
    RoleRepository roleRepository;

    /**
     * Connect this service to the User Model
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Connects this service to the auditing service in order to get current user name
     */
    @Autowired
    private UserAuditing userAuditing;

    @Override
    public List<Role> findAll()
    {
        List<Role> list = new ArrayList<>();
        /*
         * findAll returns an iterator set.
         * iterate over the iterator set and add each element to an array list.
         */
        roleRepository.findAll()
                .iterator()
                .forEachRemaining(list::add);
        return list;
    }


    @Override
    public Role findRoleById(long id)
    {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found!"));
    }

    @Override
    public Role findByName(String name)
    {
        Role rr = roleRepository.findByNameIgnoreCase(name);

        if (rr != null)
        {
            return rr;
        } else
        {
            throw new ResourceNotFoundException(name);
        }
    }

    @Transactional
    @Override
    public Role save(Role role)
    {
        if (role.getUsers()
                .size() > 0)
        {
            throw new ResourceFoundException("User Roles are not updated through Role.");
        }

        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public void deleteAll()
    {
        roleRepository.deleteAll();
    }

    @Transactional
    @Override
    public Role update(long id,
                       Role role)
    {
        if (role.getName() == null)
        {
            throw new ResourceNotFoundException("No role name found to update!");
        }

        if (role.getUsers()
                .size() > 0)
        {
            throw new ResourceFoundException("User Roles are not updated through Role. See endpoint POST: users/user/{userid}/role/{roleid}");
        }

        Role newRole = findRoleById(id); // see if id exists

        roleRepository.updateRoleName(userAuditing.getCurrentAuditor()
                        .get(),
                id,
                role.getName());
        return findRoleById(id);
    }
}

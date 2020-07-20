package com.arshak.blog.services.user;

import com.arshak.blog.exceptions.ServiceLayerException;
import com.arshak.blog.models.Role;
import com.arshak.blog.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getByRole(String role) {
        try {
            return this.roleRepository.findByRole(role);
        }catch (Exception e){
            throw new ServiceLayerException(e);
        }
    }
}

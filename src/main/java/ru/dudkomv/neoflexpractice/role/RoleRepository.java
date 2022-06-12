package ru.dudkomv.neoflexpractice.role;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    @Query("""
            select r.id, r.name
            from roles as r
            inner join users_roles ur on r.id = ur.role_id
            where ur.user_id = :user_id;
            """)
    List<Role> findAllByUserId(@Param("user_id") Long userId);

    @Modifying
    @Query("""
        insert into users_roles (user_id, role_id)
            values (:user_id, :role_id);
        """)
    void setUserRole(@Param("user_id") Long userId, @Param("role_id") Long roleId);

    Role findByName(String roleName);
}

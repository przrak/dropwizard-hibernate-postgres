package com.udemy.db;

import com.udemy.core.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import com.google.common.base.Optional;

public class UserDAO extends AbstractDAO<User> {

    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<User> findAll() {
        return list(
                namedQuery(
                        "com.udemy.core.User.findAll"
                )
        );
    }

    public Optional<User> findByUsernamePassword(String username,
                                                 String password) {
        return Optional.fromNullable(
                uniqueResult(
                        namedQuery("com.udemy.core.User.findByUsernamePassword")
                                .setParameter("username", username)
                                .setParameter("password", password)
                )
        );
    }
}

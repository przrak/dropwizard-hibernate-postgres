package com.udemy;

import com.udemy.auth.DBAuthenticator;
import com.udemy.auth.HelloAuthenticator;
import com.udemy.core.Bookmark;
import com.udemy.core.User;
import com.udemy.db.BookmarkDAO;
import com.udemy.db.UserDAO;
import com.udemy.resources.BookmarksResource;
import com.udemy.resources.HelloResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BookMarks3Application extends Application<BookMarks3Configuration> {

    private final HibernateBundle<BookMarks3Configuration> hibernateBundle
            = new HibernateBundle<BookMarks3Configuration>(User.class, Bookmark.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(BookMarks3Configuration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(final String[] args) throws Exception {
        new BookMarks3Application().run(args);
    }

    @Override
    public String getName() {
        return "BookMarks3";
    }

    @Override
    public void initialize(final Bootstrap<BookMarks3Configuration> bootstrap) {
        // TODO: application initialization
        bootstrap.addBundle(new MigrationsBundle<BookMarks3Configuration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(BookMarks3Configuration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final BookMarks3Configuration configuration,
                    final Environment environment) {
        final UserDAO userDAO
                = new UserDAO(hibernateBundle.getSessionFactory());

        final BookmarkDAO bookmarkDAO
                = new BookmarkDAO(hibernateBundle.getSessionFactory());

        environment.jersey().register(
                new BookmarksResource(bookmarkDAO)
        );

        // TODO: implement application
        environment.jersey().register(
                new HelloResource()
        );
        environment.jersey().register(
                AuthFactory.binder(
                        new BasicAuthFactory<>(
                                new DBAuthenticator(userDAO),
                                "SECURITY_REALM",
                                User.class
                        )
                )
        );
    }

}

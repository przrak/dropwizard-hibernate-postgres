package com.udemy.db;

import com.udemy.core.User;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDAOTest {

    private static final SessionFactory SESSION_FACTORY
            = HibernateUtil.getSessionFactory();

    private static Liquibase liquibase = null;
    private Session session;
    private Transaction tx;
    private UserDAO dao;

    @BeforeClass
    public static void setUpClass() throws LiquibaseException, SQLException {
        SessionFactoryImpl sessionFactory = (SessionFactoryImpl) SESSION_FACTORY;
        DriverManagerConnectionProviderImpl provider
                = (DriverManagerConnectionProviderImpl) sessionFactory.getConnectionProvider();
        Connection connection = provider.getConnection();

        Database database = DatabaseFactory
                .getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));

        liquibase = new Liquibase("migrations.xml",
                new ClassLoaderResourceAccessor(),
                database);
    }

    @AfterClass
    public static void tearDownClass() {
        SESSION_FACTORY.close();
    }

    @Before
    public void setUp() throws LiquibaseException {
        liquibase.update("DEV");
        session = SESSION_FACTORY.openSession();
        dao = new UserDAO(SESSION_FACTORY);
        tx = null;
    }

    @After
    public void tearDown() throws DatabaseException, LockException {
        liquibase.dropAll();
    }

    @Test
    public void testFindAll() {
        List<User> users = null;

        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            // do something here with dao
            users = dao.findAll();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        Assert.assertNotNull(users);
        Assert.assertFalse(users.isEmpty());
        Assert.assertEquals(1, users.size());
    }

    @Test
    public void testFindByUsernamePassword() {
        String expectedUsername = "user1";
        String expectedPassword = "pwd1";

        Optional<User> user;

        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            // do something here with dao
            session.createSQLQuery(
                    "insert into users values(null, :username, :password)"
            )
                    .setString("username", expectedUsername)
                    .setString("password", expectedPassword)
                    .executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        session = SESSION_FACTORY.openSession();
        tx = null;

        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            // do something here with dao
            user = dao.findByUsernamePassword(expectedUsername, expectedPassword);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        Assert.assertNotNull(user);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(expectedUsername, user.get().getUsername());
        Assert.assertEquals(expectedPassword, user.get().getPassword());
    }
}

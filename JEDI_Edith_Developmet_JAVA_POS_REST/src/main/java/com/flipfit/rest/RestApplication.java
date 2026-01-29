package com.flipfit.rest;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import org.jdbi.v3.core.Jdbi;

import com.flipfit.rest.resources.PingResource;
import com.flipfit.rest.resources.UserResource;
import com.flipfit.rest.resources.BookingResource;
import com.flipfit.rest.resources.GymCenterResource;
import com.flipfit.dao.UserJdbiDAO;
import com.flipfit.dao.GymCustomerJdbiDAO;
import com.flipfit.dao.GymOwnerJdbiDAO;
import com.flipfit.dao.BookingDAOImpl;
import com.flipfit.dao.GymCenterDAOImpl;
import com.flipfit.business.AccountServiceImpl;

public class RestApplication extends Application<RestConfiguration> {

    public static void main(String[] args) throws Exception {
        new RestApplication().run(args);
    }

    @Override
    public String getName() {
        return "flipfit-rest";
    }

    @Override
    public void initialize(Bootstrap<RestConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(RestConfiguration configuration, Environment environment) throws Exception {
        // Set up Jdbi
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");

        // Create Jdbi-backed DAOs
        final UserJdbiDAO userDao = new UserJdbiDAO(jdbi);
        final GymCustomerJdbiDAO customerDao = new GymCustomerJdbiDAO(jdbi);
        final GymOwnerJdbiDAO ownerDao = new GymOwnerJdbiDAO(jdbi);

        // Create AccountService with injected DAOs
        final AccountServiceImpl accountService = new AccountServiceImpl(userDao, customerDao, ownerDao, null);

        // Register resources
        environment.jersey().register(new PingResource());
        environment.jersey().register(new UserResource(accountService));

        // Register booking and center resources using legacy JDBC DAO implementations (they use DBConnection)
        environment.jersey().register(new BookingResource(new BookingDAOImpl()));
        environment.jersey().register(new GymCenterResource(new GymCenterDAOImpl()));

        // TODO: register additional DAOs/services
    }
}

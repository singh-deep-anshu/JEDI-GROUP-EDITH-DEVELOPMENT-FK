package com.flipfit.rest.resources;

import com.flipfit.business.AccountService;
import com.flipfit.business.AccountServiceImpl;
import com.flipfit.bean.GymCustomer;
import com.flipfit.rest.dto.AuthResponse;
import com.flipfit.rest.dto.LoginRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final AccountService accountService;

    // Default constructor uses legacy AccountServiceImpl that creates DAOs internally
    public UserResource() {
        this.accountService = new AccountServiceImpl();
    }

    // Constructor for tests/DI
    public UserResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        String role = accountService.login(request.getEmail(), request.getPassword());
        if (role == null) {
            AuthResponse resp = new AuthResponse(false, null, "Invalid credentials");
            return Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
        }
        AuthResponse resp = new AuthResponse(true, role, "Login successful");
        return Response.ok(resp).build();
    }

    @POST
    @Path("/register/customer")
    public Response registerCustomer(GymCustomer customer) {
        try {
            boolean created = accountService.registerCustomer(customer);
            if (created) {
                return Response.status(Response.Status.CREATED).entity("User registered").build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("Registration failed").build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }
}

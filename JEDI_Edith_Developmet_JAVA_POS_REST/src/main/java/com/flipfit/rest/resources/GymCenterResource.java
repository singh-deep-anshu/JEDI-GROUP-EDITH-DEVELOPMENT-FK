package com.flipfit.rest.resources;

import com.flipfit.bean.GymCenter;
import com.flipfit.dao.GymCenterDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/centers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymCenterResource {

    private final GymCenterDAO centerDao;

    public GymCenterResource(GymCenterDAO centerDao) {
        this.centerDao = centerDao;
    }

    @POST
    public Response addCenter(GymCenter center) {
        if (center.getCenterId() == null || center.getCenterId().isEmpty()) {
            center.setCenterId(UUID.randomUUID().toString());
        }
        boolean ok = centerDao.addGymCenter(center);
        if (ok) return Response.status(Response.Status.CREATED).entity(center).build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add center").build();
    }

    @GET
    @Path("/{id}")
    public Response getCenter(@PathParam("id") String id) {
        GymCenter c = centerDao.getGymCenterById(id);
        if (c == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(c).build();
    }

    @GET
    public Response getAllCenters() {
        List<GymCenter> list = centerDao.getAllGymCenters();
        return Response.ok(list).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCenter(@PathParam("id") String id, GymCenter center) {
        center.setCenterId(id);
        boolean ok = centerDao.updateGymCenter(center);
        if (ok) return Response.ok(center).build();
        return Response.status(Response.Status.NOT_FOUND).entity("Center not found").build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCenter(@PathParam("id") String id) {
        boolean ok = centerDao.deleteGymCenter(id);
        if (ok) return Response.noContent().build();
        return Response.status(Response.Status.NOT_FOUND).entity("Center not found").build();
    }
}

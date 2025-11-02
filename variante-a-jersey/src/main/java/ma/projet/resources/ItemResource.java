package ma.projet.resources;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import ma.projet.common.model.Item;
import ma.projet.dao.ItemDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.Optional;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON) // Produit du JSON
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    private final ItemDao itemDao = new ItemDao();


    @GET
    public Response getItems(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("categoryId") Long categoryId, // Paramètre pour le filtrage
            @QueryParam("useJoinFetch") @DefaultValue("false") boolean useJoinFetch // Flag N+1
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemPage;

        if (categoryId != null) {
            // Endpoint : GET /items?categoryId=...
            if (useJoinFetch) {
                itemPage = itemDao.findByCategoryIdWithFetch(categoryId, pageable);
            } else {
                itemPage = itemDao.findByCategory(categoryId, pageable);
            }
        } else {
            // Endpoint : GET /items?page=&size=
            itemPage = itemDao.findAll(pageable);
        }

        return Response.ok(itemPage).build();
    }

    /**
     * Endpoint: GET /items/{id}
     */
    @GET
    @Path("/{id}")
    public Response getItemById(@PathParam("id") Long id) {
        Optional<Item> item = itemDao.findById(id);
        if (item.isPresent()) {
            return Response.ok(item.get()).build(); // 200 OK
        } else {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404
        }
    }

    /**
     * Endpoint: POST /items
     */
    @POST
    public Response createItem(Item item, @Context UriInfo uriInfo) {
        Item savedItem = itemDao.save(item);
        URI location = uriInfo.getAbsolutePathBuilder().path(savedItem.getId().toString()).build();
        return Response.created(location).entity(savedItem).build(); // 201 Created
    }

    /**
     * Endpoint: PUT /items/{id}
     */
    @PUT
    @Path("/{id}")
    public Response updateItem(@PathParam("id") Long id, Item item) {
        if (!itemDao.existsById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404
        }
        item.setId(id);
        Item updatedItem = itemDao.save(item);
        return Response.ok(updatedItem).build(); // 200 OK
    }

    /**
     * Endpoint: DELETE /items/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        if (!itemDao.existsById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404
        }
        itemDao.deleteById(id);
        return Response.noContent().build(); // 204 No Content
    }
}



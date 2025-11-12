package ma.projet.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import ma.projet.model.Category;
import ma.projet.dao.CategoryDao;
import ma.projet.dao.ItemDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.Optional;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private final CategoryDao categoryDao = new CategoryDao();
    private final ItemDao itemDao = new ItemDao();

    /**
     * Endpoint: GET /categories?page=&size=
     */
    @GET
    public Response getCategories(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryDao.findAll(pageable);
        return Response.ok(categoryPage).build(); //
    }

    /**
     * Endpoint: GET /categories/{id}
     */
    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        Optional<Category> category = categoryDao.findById(id);
        if (category.isPresent()) {
            return Response.ok(category.get()).build(); // 200 OK
        } else {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404
        }
    }

    /**
     * Endpoint: POST /categories
     */
    @POST
    public Response createCategory(Category category, @Context UriInfo uriInfo) {
        Category savedCategory = categoryDao.save(category);

        URI location = uriInfo.getAbsolutePathBuilder().path(savedCategory.getId().toString()).build();
        return Response.created(location).entity(savedCategory).build(); // 201 Created
    }

    /**
     * Endpoint: PUT /categories/{id}
     */
    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") Long id, Category category) {
        if (!categoryDao.existsById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404
        }
        category.setId(id);
        Category updatedCategory = categoryDao.save(category);
        return Response.ok(updatedCategory).build(); // 200 OK
    }

    /**
     * Endpoint: DELETE /categories/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        if (!categoryDao.existsById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404
        }
        categoryDao.deleteById(id);
        return Response.noContent().build(); // 204 No Content
    }

    /**
     * Endpoint: GET /categories/{id}/items
     */
    @GET
    @Path("/{id}/items")
    public Response getItemsForCategory(
            @PathParam("id") Long id,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("useJoinFetch") @DefaultValue("false") boolean useJoinFetch) {

        if (!categoryDao.existsById(id)) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404
        }

        Pageable pageable = PageRequest.of(page, size);

        if(useJoinFetch) {
            return Response.ok(itemDao.findByCategoryIdWithFetch(id, pageable)).build();
        } else {
            return Response.ok(itemDao.findByCategory(id, pageable)).build();
        }
    }
}
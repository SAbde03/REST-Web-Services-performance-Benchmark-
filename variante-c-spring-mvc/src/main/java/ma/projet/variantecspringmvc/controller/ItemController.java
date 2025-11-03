package ma.projet.variantecspringmvc.controller;


import ma.projet.variantecspringmvc.model.Item;
import ma.projet.variantecspringmvc.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping
public class ItemController {


    @Autowired
    private ItemService itemService;

    // Endpoint: GET /items?page=&size=
    @GetMapping("/items")
    public Page<Item> getItems(Pageable pageable){
        return itemService.findAll(pageable);
    }

    // Endpoint: GET /items/{id}
    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id){
        return itemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint: POST /items
    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@RequestBody Item item){
        Item savedItem = itemService.save(item);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedItem.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedItem);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item){
        if(!itemService.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        item.setId(id);
        Item savedItem = itemService.save(item);
        return ResponseEntity.ok(savedItem);
    }

    // Endpoint: DELETE /items/{id}
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        if(!itemService.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        itemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/items", params = "categoryId")
    public Page<Item> getItemsByCategoryId(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "false") boolean useJoinFetch,
            Pageable pageable) {

        return itemService.findByCategoryId(categoryId, useJoinFetch, pageable);
    }

    @GetMapping("/categories/{id}/items")
    public Page<Item> getItemsForCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean useJoinFetch,
            Pageable pageable) {
        return itemService.findByCategoryId(id, useJoinFetch, pageable);
    }

}

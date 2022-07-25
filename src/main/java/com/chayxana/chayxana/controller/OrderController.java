package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.entity.Order;
import com.chayxana.chayxana.exceptions.PageSizeException;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.OrderDto;
import com.chayxana.chayxana.service.OrderService;
import com.chayxana.chayxana.utills.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mobile/order")
@CrossOrigin(maxAge = 3600)
public class OrderController {

    @Autowired
    OrderService service;

    @PostMapping
    public HttpEntity<?> create(@RequestBody OrderDto orderDto) throws Exception {
        ApiResponse response = service.create(orderDto);
        return ResponseEntity.status(response.isSuccess()? 200:409).body(response);
    }

//    @GetMapping("{chayxanaId}/{offset}/{pageSize}")
//    public Page<Order> getByChayxanaId(@PathVariable UUID chayxanaId, @PathVariable int offset, @PathVariable int pageSize) {
//        return service.getByChayxanaId(chayxanaId, offset, pageSize);
//    }

    @GetMapping("/all-order/{chayxana-id}")
    public HttpEntity<?> getAllOrder(@PathVariable(name = "chayxana-id") UUID chayxanaId,
                                     @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
                                     @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size) throws PageSizeException {
        ApiResponse response = service.getByChayxanaId(chayxanaId, page, size);
        return ResponseEntity.status(response.isSuccess()? 200 : 409).body(response);
    }
    @PutMapping("/{id}")
    public HttpEntity<?> update(@PathVariable UUID id) {
        ApiResponse response = service.nexStatus(id);
        return ResponseEntity.status(response.isSuccess()? 200 : 409).body(response);
    }

    @GetMapping("orders/by/userId/{id}")
    public HttpEntity<?> getOrderByUserId(@PathVariable UUID id){
        ApiResponse apiResponse = service.getAllOrdersByUserId(id);
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Change order status (DONE or CANCEL)
     * @param doneOrCancel boolean
     * @param id UUID
     * @return ResponseEntity
     * If ApiResponse return true, we return 202 (ACCEPTED) at HttpStatus else 404 (NOT FOUND) at HttpStatus
     */
    @PutMapping("/doneOrCancel/{id}")
    public HttpEntity<?> changeOrderStatus(@RequestParam(name = "doneOrCancel") boolean doneOrCancel,
                                           @PathVariable UUID id){
        ApiResponse apiResponse = service.changeOrderStatus(doneOrCancel, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 404).body(apiResponse);
    }

    @PutMapping("/changeNewStatus/{id}")
    public  HttpEntity<?> changeOrderNewStatus(  @PathVariable UUID id){
        ApiResponse apiResponse = service.changeNewStatus(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 404).body(apiResponse);

    }
}
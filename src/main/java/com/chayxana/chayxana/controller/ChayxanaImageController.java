package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.service.ChayxanaImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mobile/image")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class ChayxanaImageController {

    private final ChayxanaImageService chayxanaImageService;

    /**
     * save Chayxana images.
     *
     * @param request
     * @return ApiResponse
     * @throws IOException
     *
     * If ApiResponse return true, we return 201 (CREATED) else 409 (CONFLICTED)
     */
    @PostMapping("/save/{id}")
    public HttpEntity<?> saveImageToFileSystem(@PathVariable UUID id, MultipartHttpServletRequest request) throws Exception {
        ApiResponse response = chayxanaImageService.getFileFromRequest(request, id);
        return ResponseEntity.status(response.isSuccess()? 201 : 409).body(response);
    }

    /**
     * get one image by id
     * @param id
     * @param response
     * @return image
     * @throws IOException
     */
    @GetMapping("/byId/{id}")
    public HttpEntity<?> getByIdImage(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        ApiResponse responseApi = chayxanaImageService.getById(id, response);
        return ResponseEntity.status(responseApi.isSuccess()? 200:408).build();
    }

    /**
     * get all chayxana image
     *
     * @param id
     * @return ApiResponse
     * @throws IOException
     */
    @GetMapping("/getImages")
    public HttpEntity<?> getAllImages(@RequestParam(name = "id") UUID id) throws IOException {
        return ResponseEntity.status(201).body(chayxanaImageService.getAllImages(id));
    }

    /**
     * Delete chayxana image by id
     * @param id
     * @return ApiResponse
     *
     */
    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> editImage(@PathVariable UUID id){
        ApiResponse response = chayxanaImageService.deleteChayxanaImage(id);
        return ResponseEntity.status(response.isSuccess()? 204 : 409).body(response);
    }
}

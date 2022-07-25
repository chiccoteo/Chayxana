package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.Chayxana;
import com.chayxana.chayxana.entity.ChayxanaImage;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.ImageDTO;
import com.chayxana.chayxana.repo.ChayxanaImageRepo;
import com.chayxana.chayxana.repo.ChayxanaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChayxanaImageService {


    @Value(value = "${image.folder.path}")
    private String uploadedFiles;

    private final ChayxanaImageRepo chayxanaImageRepo;
    private final ChayxanaRepo chayxanaRepo;
    private final MapperDTO mapperDTO;

    public UUID saveToFileSystem(MultipartFile file, boolean main, UUID id) throws Exception {
        ChayxanaImage image = new ChayxanaImage();
        image.setOriginalName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setMainImage(main);
        Optional<Chayxana> chayxanas = chayxanaRepo.findById(id);
        if (chayxanas.isEmpty()){
            throw new Exception("Not Found Chayxana");
        }
        Set<Chayxana> chayxanaSet = new HashSet<>();
        chayxanaSet.add(chayxanas.get());
        image.setChayxanas(chayxanaSet);

        image = chayxanaImageRepo.save(image);
        String[] split = file.getOriginalFilename().split("\\.");
        String name = UUID.randomUUID() + "." + split[split.length - 1];
        image.setName(name);
        Path path = Paths.get(uploadedFiles + name);
        Files.copy(file.getInputStream(), path);
        image.setImageUrl(path.toString());
        chayxanaImageRepo.save(image);
        return image.getId();
    }

    public ApiResponse getFileFromRequest(MultipartHttpServletRequest request, UUID id) throws Exception {
        Iterator<String> fileNames = request.getFileNames();
        List<UUID> imagesId = new ArrayList<>();
        while (fileNames.hasNext()) {
            String filename = fileNames.next();
            UUID imageId;
            if (filename.equals("mainImage")) {
                MultipartFile file = request.getFile(filename);
                Optional<ChayxanaImage> chayxanaImageByMainImageTrue = chayxanaImageRepo.findByMainImageTrueAndChayxanasId(id);
                if (chayxanaImageByMainImageTrue.isPresent()) {
                    ChayxanaImage chayxanaImage = chayxanaImageByMainImageTrue.get();
                    chayxanaImage.setMainImage(false);
                    chayxanaImageRepo.save(chayxanaImage);
                }
                if(file != null) {
                    imageId = saveToFileSystem(file, true, id);
                    imagesId.add(imageId);
                }
            } else if (filename.equals("images")) {
                List<MultipartFile> files = request.getFiles(filename);
                for (MultipartFile multipartFile : files) {
                    if(multipartFile != null) {
                        imageId = saveToFileSystem(multipartFile, false,id);
                        imagesId.add(imageId);
                    }
                }
            }
        }
        return new ApiResponse(true, imagesId.size() > 0 ? "Saved" : "Not saved", imagesId.size() > 0 ? imagesId : null);
    }

    public ApiResponse getAllImages(UUID id) throws IOException {
        List<ChayxanaImage> chayxanaImageList = chayxanaImageRepo.findChayxanaImagesByChayxanasId(id);
        List<ImageDTO> imageDTOList = new ArrayList<>();
        for (ChayxanaImage image : chayxanaImageList) {
            ImageDTO imageDTO = mapperDTO.generateImageToImageDTO(image);
            imageDTOList.add(imageDTO);
        }
        return new ApiResponse(true, "All Images", imageDTOList);
    }

    public ApiResponse deleteChayxanaImage(UUID id) {
        Optional<ChayxanaImage> chayxanaImageOptional = chayxanaImageRepo.findById(id);
        if (chayxanaImageOptional.isPresent()){
            chayxanaImageRepo.delete(chayxanaImageOptional.get());
            return new ApiResponse(true, "Deleted image");
        }
        return new ApiResponse(false, "Not Fount");
    }

    public ApiResponse getById(UUID id, HttpServletResponse response) throws IOException {
        Optional<ChayxanaImage> optionalChayxanaImage = chayxanaImageRepo.findById(id);
        if (optionalChayxanaImage.isPresent()){
            ChayxanaImage image = optionalChayxanaImage.get();
            response.setHeader("Content-Disposition",
                    "attachment; filename=\""
                            + image.getOriginalName() + "\"");

            // File ni Content Type
            response.setContentType(image.getContentType());

            // inputStream va response.getOutputStream berishimiz kerak, endi shu yerda unikal name qilganimizni ishlatamiz
            // Buning uchun bitta FileInputStream ochvolamiz va uni ichiga olmoqchi bo'lgan file limizni yo'lini va
            // name ni bervoramiz va pasdagi FileCopyUtils.copy(); ==> methodiga bervoramiz:

            FileInputStream inputStream = new FileInputStream(image.getImageUrl());
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            return new ApiResponse(true, "Success");
        }
        return new ApiResponse(false, "Image Not Found!");
    }


}

package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.Icon;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.IconMultiSendDto;
import com.chayxana.chayxana.payload.IconUpdateDto;
import com.chayxana.chayxana.repo.IconRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class IconService {

//    public static final String uploadedFiles = "src/main/resources/static/Icons/";
//    public static final String uploadedFiles = "static/icons/";

    @Value(value = "${icon.folder.path}")
    private String uploadedFiles;

    private final IconRepo iconRepo;


    /*
    The method
    which uploads

    icon(s) from user FileSystem.
            *
            *
    @param
    request MultipartHttpServletRequest
     *@return ApiResponse
     */

    public ApiResponse uploadIcon(MultipartHttpServletRequest request) {
        Iterator<String> fileNames = request.getFileNames();
        List<UUID> iconsIdList = new ArrayList<>();
//        int count = 0;
        while (fileNames.hasNext()) {
            MultipartFile file = request.getFile(fileNames.next());
            if (file != null) {
                String originalFilename = file.getOriginalFilename();
                Icon icon = new Icon(originalFilename, file.getSize(), file.getContentType());
                String[] split = originalFilename.split("\\.");
                String generatedName = UUID.randomUUID() + "." + split[split.length - 1];
                icon.setName(generatedName);
                icon.setIconUrl(uploadedFiles);

                Path path = Paths.get(uploadedFiles + generatedName);
                try {
                    Files.copy(file.getInputStream(), path);
//                    count++;
                    icon = iconRepo.save(icon);
                    iconsIdList.add(icon.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ApiResponse(iconsIdList.size() > 0, iconsIdList.size() > 0 ? "Saved" : "Error", iconsIdList.size() > 0 ? iconsIdList : null);
    }


    /*
    The method

    which downloads(sends) icon (s)
    to user.
            *
            *
    @param
    id UUID
     *
    @param
    response HttpServletResponse
     *@return ApiResponse
     *@throws
    IOException IOException
     */

    public ApiResponse getIconById(UUID id, HttpServletResponse response) throws IOException {
        Optional<Icon> iconOptional = iconRepo.findById(id);
        if (iconOptional.isPresent()) {
            Icon icon = iconOptional.get();
            // File ni name
            response.setHeader("Content-Disposition",
                    "attachment; filename=\""
                            + icon.getFileOriginalName() + "\"");

            // File ni Content Type
            response.setContentType(icon.getContentType());

            // inputStream va response.getOutputStream berishimiz kerak, endi shu yerda unikal name qilganimizni ishlatamiz
            // Buning uchun bitta FileInputStream ochvolamiz va uni ichiga olmoqchi bo'lgan file limizni yo'lini va
            // name ni bervoramiz va pasdagi FileCopyUtils.copy(); ==> methodiga bervoramiz:

            FileInputStream inputStream = new FileInputStream(icon.getIconUrl() + icon.getName());
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            return new ApiResponse(true, "Success");
        }
        return new ApiResponse(false, "Icon Not Found!");
    }
    /*
    The method
    which gets
    all Icons' data and main content in bytes.
            *
            *@return ApiResponse
     */

    public ApiResponse getAllIcons() {
        List<IconMultiSendDto> iconMultiSendDtoList = new ArrayList<>();
        try {
            List<Icon> iconList = iconRepo.findAll();
            for (Icon icon : iconList) {
                String iconUrl = icon.getIconUrl();
                Path path = Paths.get(iconUrl + icon.getName());
                byte[] data = Files.readAllBytes(path);
                iconMultiSendDtoList.add(new IconMultiSendDto(icon.getId(), icon.getFileOriginalName(), icon.getSize(),
                        icon.getContentType(), icon.getName(), data));
            }
            return new ApiResponse(true, "Success", iconMultiSendDtoList);
        } catch (Exception e) {
            return new ApiResponse(false, "Error");
        }
    }


    /*
    The method
    which deletes

    icon(Icon from database and file system fully).
            *
            *
    @param
    id UUID
     *@return ApiResponse
     */

    public ApiResponse deleteIcon(UUID id) {
        try {
            Optional<Icon> optionalIcon = iconRepo.findById(id);
            if (optionalIcon.isPresent()) {
                String iconUrl = optionalIcon.get().getIconUrl();
                File file = new File(iconUrl + optionalIcon.get().getName());
                if (file.delete()) {
                    iconRepo.deleteById(id);
                    return new ApiResponse(true, "Deleted");
                } else
                    return new ApiResponse(false, "Error on deleting!");
            }
            return new ApiResponse(false, "Icon not found!");
        } catch (Exception e) {
            return new ApiResponse(false, "Error!");
        }
    }


    /**
     * The method which updates the name of icon.
     *
     * @param dto      IconUpdateDto
     * @param response HttpServletResponse
     * @return ApiResponse
     * @throws IOException IOException
     */
    public ApiResponse updateIconById(IconUpdateDto dto, HttpServletResponse response) throws IOException {
        Optional<Icon> iconOptional = iconRepo.findById(dto.getId());
        if (iconOptional.isPresent()) {
            Icon icon = iconOptional.get();
            // File ni name
            icon.setFileOriginalName(dto.getName());
            iconRepo.save(icon);

            response.setHeader("Content-Disposition",
                    "attachment; filename=\""
                            + icon.getFileOriginalName() + "\"");

            response.setContentType(icon.getContentType());

            FileInputStream inputStream = new FileInputStream(icon.getIconUrl() + icon.getName());
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            return new ApiResponse(true, "Updated");
        }
        return new ApiResponse(false, "Icon not Found!");
    }
}
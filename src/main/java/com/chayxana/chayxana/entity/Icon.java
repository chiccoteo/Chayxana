package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityLong;
import com.chayxana.chayxana.entity.template.AbsEntityUUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(value = {"createdBy", "createdAt", "updatedBy", "updatedAt", "iconUrl"}) // idisini beramizmi yo'mi shuni maslahat qilamiz.
public class Icon extends AbsEntityUUID {

    // File quyidagi field lardan iborat

    // Client ga ko'rsatish uchun:
    private String fileOriginalName; // file ni xaqiqiy nomi pdp.jpg, inn.pdf

    private long size;  // file ni xajmi 2 mb ==> byte da esa 2048000 bytes

    private String contentType; // File ning formati, tipi (application/pdf  || image/png)

    /**
     * Agar iikita bir xil nomlik file saqlansa unda ularni olohida boshqa nom bilan saqlashi kerak,
     * shuning uchun shu filed ni qo'yyapmiz, Ms: aligax.mp4 (100Mb) huddi shu file nomi bilan bir xil
     * yana aligax.mp4 (500Mb) file saqlanyapdi lekin biz ularni alohida alohida saqlashimkiz kerak,
     * shuning uchun name degan field ovoldik uni random generatsiya qivoramiz
     * Papkani ichida unikal nom bo'lishi uchun: Ya'ni File Sytemga saqlaganda kerak bo'ladi:
     */
    private String name;

    @Column(nullable = false)
    private String iconUrl;

    public Icon(String fileName, long size, String contentType) {
        this.fileOriginalName = fileName;
        this.size = size;
        this.contentType = contentType;
    }
}

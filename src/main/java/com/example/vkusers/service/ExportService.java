package com.example.vkusers.service;

import com.example.vkusers.domain.VkUser;
import com.example.vkusers.repository.VkUserRepository;
import com.example.vkusers.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ExportService {
    @Autowired
    VkUserRepository userRepository;

    /**
     * Экспорт в xlsx
     */
    public String exportAllToXlsx(String path, String fileName, Boolean printHeader) {
        String exportPath = (StringUtils.isEmpty(path) || StringUtils.isBlank(path)) ? "." : path;
        exportPath = exportPath.replace("\\", "/");
        String fullPath = !exportPath.endsWith("/") ? exportPath+"/" : exportPath;
        fullPath += (StringUtils.isEmpty(fileName) || StringUtils.isBlank(fileName) ? "out" : fileName);
        if(!fullPath.endsWith(".xlsx")){
            fullPath += ".xlsx";
        }
        List<VkUser> vkUsers = userRepository.findAll();
        XSSFWorkbook book = new XSSFWorkbook();
        try(FileOutputStream fileOut = new FileOutputStream(fullPath)){
            XSSFSheet sheet = book.createSheet("VK Users data");
            AtomicInteger counter = new AtomicInteger(0);
            if(printHeader!=null && printHeader){
                XSSFRow row = sheet.createRow(counter.getAndIncrement());
                XSSFCell cell = row.createCell(0);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("id");
                cell = row.createCell(1);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("Имя пользователя");
                cell = row.createCell(2);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("Фамилия пользователя");
                cell = row.createCell(3);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("День рождения");
                cell = row.createCell(4);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("Город");
                cell = row.createCell(5);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("Телефон");
            }
            vkUsers.stream().forEach(vkUser -> {
                XSSFRow row = sheet.createRow(counter.getAndIncrement());
                XSSFCell cell = row.createCell(0);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(vkUser.getId());
                cell = row.createCell(1);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(vkUser.getИмяПользователя());
                cell = row.createCell(2);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(vkUser.getФамилияПользователя());
                cell = row.createCell(3);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(vkUser.getДеньРождения() != null ?
                        DateTimeUtil.toString(vkUser.getДеньРождения(), null)
                        : "");
                cell = row.createCell(4);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(vkUser.getГород());
                cell = row.createCell(5);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(vkUser.getКонтактнаяИнформация());
            });

            book.write(fileOut);
        }catch (IOException e){
            log.error("Ошибка создания файла {}: {}", fullPath, e);
        }catch (Exception e){
            log.error("Ошибка экспорта данных: {}",e);
        }
//        userRepository

        return fullPath;
    }

}

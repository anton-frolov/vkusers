package com.example.vkusers;

import com.example.vkusers.service.ExportService;
import com.example.vkusers.service.VkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VkusersApplication implements CommandLineRunner {

    @Value("${app.export.path:.}")
    private String path;

    @Value("${app.export.fileName:out.xlsx}")
    private String fileName;

    @Value("${app.export.printHeader:true}")
    private Boolean printHeader;

    @Autowired
    VkUserService userService;

    @Autowired
    ExportService exportService;

    public static void main(String[] args) {
        SpringApplication.run(VkusersApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userService.getVkUsersInfo();
        exportService.exportAllToXlsx(path, fileName, printHeader);
    }
}

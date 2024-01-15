package com.example.vkusers.service;

import com.example.vkusers.domain.VkUser;
import com.example.vkusers.repository.VkUserRepository;
import com.example.vkusers.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@TestPropertySource(properties = { "app.export.path: ./export", "app.export.fileName: export.xlsx" })
@ExtendWith(MockitoExtension.class)
class ExportServiceTest {
    @Value("${app.export.path}")
    private String path;

    @Value("${app.export.fileName}")
    private String fileName;

    @Mock
    private VkUserRepository userRepository;
    @InjectMocks
    private ExportService exportService;

    @BeforeEach
    void init(){
        List<VkUser> vkUsers = Arrays.asList(
                new VkUser().toBuilder()
                        .id(174909507L)
                        .имяПользователя("Тест")
                        .фамилияПользователя("Тест")
                        .город("Санкт-Петербург")
                        .деньРождения(DateTimeUtil.fromStringToLocalDate("1982-05-15"))
                        .контактнаяИнформация("+7(902)1233211221")
                        .build(),
                new VkUser().toBuilder()
                        .id(174909508L)
                        .имяПользователя("Тест2")
                        .фамилияПользователя("Тест2")
                        .город("Москва")
                        .деньРождения(DateTimeUtil.fromStringToLocalDate("1980-01-15"))
                        .контактнаяИнформация("+7(902)1233211221")
                        .build(),
                new VkUser().toBuilder()
                        .id(174909509L)
                        .имяПользователя("Тест3")
                        .фамилияПользователя("Тест3")
                        .город("Пермь")
                        .деньРождения(DateTimeUtil.fromStringToLocalDate("1970-05-01"))
                        .контактнаяИнформация("+7(902)1233211221")
                        .build()
        );
        when(userRepository.findAll()).thenReturn(vkUsers);
    }

    @Test
    void exportAllToXlsx() {
        exportService.exportAllToXlsx(path, fileName, true);
    }
}
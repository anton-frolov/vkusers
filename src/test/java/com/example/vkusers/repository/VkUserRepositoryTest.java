package com.example.vkusers.repository;

import com.example.vkusers.domain.VkUser;
import com.example.vkusers.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VkUserRepositoryTest {
    @Mock
    private VkUserRepository userRepository;
    private static final Long id = 174909507L;

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
        when(userRepository.findById(anyLong()))
                .then(invocationOnMock -> vkUsers.stream()
                        .filter(a -> a.getId()==(long)invocationOnMock.getArgument(0))
                        .findFirst());
    }
    @Test
    void vkUserEntityTest(){
        VkUser user = VkUser.builder()
                .id(id)
                .имяПользователя("Тест")
                .фамилияПользователя("Тест")
                .город("Санкт-Петербург")
                .деньРождения(DateTimeUtil.fromStringToLocalDate("1982-05-15"))
                .контактнаяИнформация("+7(902)1233211221")
                .build();
        Optional<VkUser> userOpt = userRepository.findById(id);
        assertTrue(userOpt.isPresent());
        assertEquals(user, userOpt.get());
    }

}
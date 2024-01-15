package com.example.vkusers.service;

import com.example.vkusers.domain.VkUser;
import com.example.vkusers.exception.WrongSettingException;
import com.example.vkusers.mapper.VkUserMapper;
import com.example.vkusers.repository.VkUserRepository;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class VkUserService {

    @Value("${app.processing.appId}")
    private Integer appId;
    @Value("${app.processing.accessToken}")
    private String accessToken;
    @Value("${app.processing.threadCount:3}")
    private Integer threadCount;
    @Value("${app.processing.timeout:300}")
    private Integer timeout;
    @Value("${app.processing.batchSize:2000}")
    private Integer batchSize;

    private final VkUserRepository userRepository;
    private final VkUserMapper userMapper;
    private final TransactionTemplate transactionTemplate;

    public void getVkUsersInfo(){
        if(appId==null)
            throw new WrongSettingException("The application ID is missing");
        if(accessToken==null)
            throw new WrongSettingException("Access token is missing");

        long userCount = userRepository.count();
        if(threadCount<1){
            threadCount = 1;
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for(int i = 0; i <= (userCount / batchSize); i++ ){
            int finalI = i;
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                log.debug("batch n: {}", finalI);
                Pageable pageable = PageRequest.of(finalI, batchSize);
                List<VkUser> users = transactionTemplate.execute(status -> {
                            Slice<VkUser> slice = userRepository.findAll(pageable);
                            return slice.getContent();
                });
                log.debug("users count: {}, start id: {}, finish id: {}", users.size(), users.get(0).getId(),
                        users.get(!users.isEmpty() ? users.size()-1 : 0).getId());

                TransportClient transportClient = new HttpTransportClient();
                VkApiClient vk = new VkApiClient(transportClient);
                List<GetResponse> vkUserList = new ArrayList<>();
                try {
                    vkUserList = vk.users()
                            .get(new UserActor(appId, accessToken))
                            .userIds(users.stream()
                                    .map(VkUser::getId)
                                    .map(Object::toString)
                                    .toList()
                                )
                            .fields(Fields.CITY, Fields.BDATE, Fields.CONTACTS)
                            .lang(Lang.RU)
                            .execute();
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                } catch (ClientException e) {
                    throw new RuntimeException(e);
                }
                if(!vkUserList.isEmpty()){
                    List<GetResponse> finalVkUserList = vkUserList;
                    transactionTemplate.executeWithoutResult(status -> {
                                finalVkUserList.forEach(response -> {
                                    userRepository.save(userMapper.mapToEntity(response));
                                });
                            });
                }
            });
        }

        try {
            log.info("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(timeout, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            log.error("tasks interrupted {}", e);
        }
        finally {
            if (!executor.isTerminated()) {
                log.info("cancel non-finished tasks");
            }
            executor.shutdownNow();
            log.info("shutdown finished");
        }
    }
}

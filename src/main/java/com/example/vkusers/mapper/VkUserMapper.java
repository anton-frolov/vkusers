package com.example.vkusers.mapper;

import com.example.vkusers.domain.VkUser;
import com.example.vkusers.util.DateTimeUtil;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface VkUserMapper {

    @Mapping(target = "id", source = "response.id")
    @Mapping(target = "имяПользователя", source = "response.firstName", qualifiedByName = "getTrimmedString")
    @Mapping(target = "фамилияПользователя", source = "response.lastName", qualifiedByName = "getTrimmedString")
    @Mapping(target = "деньРождения", source = "response.bdate",qualifiedByName = "getBirthDate")
    @Mapping(target = "город", source = "response.city.title", qualifiedByName = "getTrimmedString")
    @Mapping(target = "контактнаяИнформация", source = "response", qualifiedByName = "getContacts")
    VkUser mapToEntity(GetResponse response);

    @Named("getBirthDate")
    default LocalDate getBirthDate(String bDate){
        if (bDate==null)
            return null;
        try{
            return DateTimeUtil.fromVkString(bDate.trim());
        }catch (Exception e) {
            return null;
        }
    }

    @Named("getContacts")
    default String getContacts(GetResponse response){
        if(response.getHomePhone()==null && response.getMobilePhone()==null)
            return null;
        String homePhone = !StringUtils.isEmpty(response.getHomePhone()) ? response.getHomePhone().trim() :"";
        String mobilePhone = !StringUtils.isEmpty(response.getMobilePhone()) ? response.getMobilePhone().trim() :"";
        String result = (homePhone+" "+mobilePhone).trim();
        return result.length()>256 ? result.substring(0, 256) : result;
    }

    @Named("getTrimmedString")
    default String getTrimmedString(String value){
        if(value== null)
            return null;
        if(value.trim().length()>256)
            return value.trim().substring(0, 256);
        return value.trim();
    }
}

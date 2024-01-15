package com.example.vkusers.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "user_info")
public class VkUser {

    @Id
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_f_name")
    private String имяПользователя;
    @Column(name = "user_l_name")
    private String фамилияПользователя;
    @Column(name = "user_b_date")
    private LocalDate деньРождения;
    @Column(name = "user_city")
    private String город;
    @Column(name = "user_contacts")
    private String контактнаяИнформация;
}

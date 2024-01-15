truncate table user_info;
insert into user_info(user_id) select x from system_range(76436119, 76436119+5000);
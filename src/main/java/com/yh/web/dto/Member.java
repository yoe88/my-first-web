package com.yh.web.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Member {
	private String id;
	private String password;
	private String name;
	private String email;
	private String zoneCode;
	private String address;
	private String extraAddress;
	private String detailAddress;
	private LocalDateTime regDate;
	private String profileImage;
	private boolean enable;
}

package com.yh.web.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Member {
	private String id;
	private String passwd;
	private String name;
	private String email;
	private String zonecode;
	private String address;
	private String extraAddress;
	private String detailAddress;
	private LocalDateTime regdate;
	private String profileImage;
	private boolean enable;
}

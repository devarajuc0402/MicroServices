package com.org.help.kafka.entity;

import lombok.Data;

@Data
public class HelpEntity {

	private String helpId;
	private String info;
	private int seq;
	private String topic;
}

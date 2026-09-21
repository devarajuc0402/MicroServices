package com.org.help.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Help")
@Data
public class HelpEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "help_seq_generator")
	@SequenceGenerator(
			name = "help_seq_generator",
            sequenceName = "help_id_seq",
            allocationSize = 1
    )
	@Column(name = "help_id")
	private Long helpId;
	
	@Column(name = "topic", length=2000)
	private String topic;
	
	@Column(name = "seq")
	private int seq;
	
	@Column(name = "info")
	private String info;
	
}

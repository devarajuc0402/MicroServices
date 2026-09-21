package com.org.help.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.help.entity.HelpEntity;

@Repository
public interface HelpRepository extends JpaRepository<HelpEntity, Long> {

	List<HelpEntity> findAll();
	
	@Query(value = 
			"""
			select h 
			from HelpEntity h 
			where h.helpId = :id
			""")
	Optional<HelpEntity> findByHelpId(@Param("id") int id);
	
	
	@Query(value = """
			select h
			from HelpEntity h
			where h.helpId between :startHelpId and :endHelpId
			""")
	List<HelpEntity> getByHelpIdLimit(
			@Param("startHelpId") int startHelpId, 
			@Param("endHelpId") int endHelpId);
	
}

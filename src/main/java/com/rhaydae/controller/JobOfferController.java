package com.rhaydae.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhaydae.dto.JobOfferRequest;
import com.rhaydae.dto.JobOfferResponse;
import com.rhaydae.service.JobOfferService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobOffer")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class JobOfferController {
	
	private final JobOfferService jobOfferService;
	@PostMapping("/saveJob")
	@Operation(security = { 
			@SecurityRequirement(name = "bearerAuth") }) 
	public JobOfferResponse createJobOffer( @Valid @RequestBody JobOfferRequest request) { 
		
		return jobOfferService.createJob(request);
		}
	
	@GetMapping("/{id}")
	@Operation(security = { 
			@SecurityRequirement(name = "bearerAuth") }) 
	public JobOfferResponse getJobOfferById( @PathVariable Long id) { 
		return jobOfferService.getJobById(id); } 
	
	@GetMapping("/AllJobs") 
	@Operation(security = { 
			@SecurityRequirement(name = "bearerAuth") }) 
	public List<JobOfferResponse> getAllJobOffers() { 
		return jobOfferService.getAllJobOffers();
		}
	
	@PutMapping("/updateJob/{id}")
	@Operation(security = { 
			@SecurityRequirement(name = "bearerAuth") }) 
	public JobOfferResponse updateJobOffer( @PathVariable Long id, @Valid @RequestBody JobOfferRequest request) {
		return jobOfferService.updateJobOffer(id, request);
		}

	@DeleteMapping("/delete/{id}") 
	@Operation(security = { 
			@SecurityRequirement(name = "bearerAuth") }) 
	public void deleteJobOffer( @PathVariable Long id) { 
		jobOfferService.deleteJobOffer(id); 
		}
	
}

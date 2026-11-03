package com.rhaydae.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rhaydae.dto.JobOfferRequest;
import com.rhaydae.dto.JobOfferResponse;
import com.rhaydae.entities.JobOffers;
import com.rhaydae.repositories.JobOffersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobOfferService {
	
	private final JobOffersRepository jobOffersRepository;
	
	
	public JobOfferResponse createJob(JobOfferRequest dto) {
		
		JobOffers job = JobOffers.builder()
				.title(dto.title())
				.company(dto.company())
				.location(dto.location())
				.description(dto.description())
				.contractType(dto.contractType())
			    .createdAt(LocalDateTime.now())
			    .active(true)
				.build();
		
		JobOffers savedJob = jobOffersRepository.save(job);
		
		return mapToResponse(savedJob);
	}

    public JobOfferResponse getJobById(Long id) {
    	
    	JobOffers jobOffer = jobOffersRepository.findById(id) 
    			.orElseThrow(() -> new RuntimeException( "Job offer not found with id: " + id ) );
    	return mapToResponse(jobOffer);
    }
    
    
    
	private JobOfferResponse mapToResponse(JobOffers jobOffer) {
		return new JobOfferResponse( 
				jobOffer.getId(), 
				jobOffer.getTitle(),
				jobOffer.getCompany(),
				jobOffer.getLocation(),
				jobOffer.getDescription(), 
				jobOffer.getContractType(),
				jobOffer.getCreatedAt(),
				jobOffer.getActive()
				);
	}
	
	public List<JobOfferResponse> getAllJobOffers() { 
		return jobOffersRepository.findAll()
				                 .stream() 
				                 .map(this::mapToResponse) 
				                 .toList();
		}
	
	
	public JobOfferResponse updateJobOffer( Long id, JobOfferRequest request) { 
		JobOffers jobOffer = jobOffersRepository.findById(id)
				.orElseThrow(() -> new RuntimeException( "Job offer not found with id: " + id ) ); 
		
		jobOffer.setTitle(request.title());
		jobOffer.setCompany(request.company());
		jobOffer.setLocation(request.location());
		jobOffer.setDescription(request.description()); 
		jobOffer.setContractType(request.contractType());
		
		JobOffers updatedOffer = jobOffersRepository.save(jobOffer);
		
		return mapToResponse(updatedOffer);
		}

	public void deleteJobOffer(Long id) {
		jobOffersRepository.deleteById(id);
		
	}

}

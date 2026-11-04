package com.rhaydae.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rhaydae.dto.JobOfferRequest;
import com.rhaydae.dto.JobOfferResponse;
import com.rhaydae.entities.JobOffers;
import com.rhaydae.entities.User;
import com.rhaydae.enums.Role;
import com.rhaydae.repositories.JobOffersRepository;
import com.rhaydae.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobOfferService {
	
	private final JobOffersRepository jobOffersRepository;
	private final UserRepository userRepository;
	
	
	private User getRecruiter(String email) {

	    User recruiter = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    if (recruiter.getRole() != Role.RECRUITER) {
	        throw new RuntimeException("Only recruiters can manage job offers");
	    }

	    return recruiter;
	}
	
	
	public JobOfferResponse createJob(JobOfferRequest dto, String email) {
		
		User recruiter = getRecruiter(email);
		
		JobOffers job = JobOffers.builder()
				.title(dto.title())
				.company(dto.company())
				.location(dto.location())
				.description(dto.description())
				.contractType(dto.contractType())
			    .createdAt(LocalDateTime.now())
			    .active(true)
			    .recruiter(recruiter)
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

		public JobOfferResponse closeJobOffer(Long id, String email) {
	
		    User recruiter = getRecruiter(email);
	
		    JobOffers jobOffer = jobOffersRepository.findById(id)
		            .orElseThrow(() -> new RuntimeException("Job offer not found"));
	
		    if (!jobOffer.getRecruiter().getId().equals(recruiter.getId())) {
		        throw new RuntimeException("You cannot close another recruiter's job offer");
		    }
	
		    jobOffer.setActive(false);
	
		    JobOffers updatedOffer = jobOffersRepository.save(jobOffer);
	
		    	return mapToResponse(updatedOffer);
		}
	
	
	
	
	public void deleteJobOffer(Long id) {
		jobOffersRepository.deleteById(id);
		
	}

}

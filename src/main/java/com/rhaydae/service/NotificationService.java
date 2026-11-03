package com.rhaydae.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.rhaydae.dto.NotificationResponse;
import com.rhaydae.entities.Notification;
import com.rhaydae.entities.User;
import com.rhaydae.repositories.ApplicationRepository;
import com.rhaydae.repositories.NotificationRepository;
import com.rhaydae.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	private final ApplicationRepository applicationRepository;
	
	@Transactional
	public void createNotif(Long userId, String message) {
		
		User candidate = userRepository.findById(userId)
				.orElseThrow(()-> new RuntimeException("email not found"));
		
	    Notification notif = Notification.builder()
			  .message(message)
			  .read(false)
			  .createdAt(LocalDateTime.now())
			  .user(candidate)
			  .build();
	  
	    notificationRepository.save(notif);
		
	}
	
	@Transactional(readOnly = true)
	public List<NotificationResponse> getMyNotifications(String email){
		
		User candidate = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("user not found"));
		
	   return notificationRepository.findByUser_IdOrderByCreatedAtDesc(candidate.getId())
	     .stream()
	     .map(this::toResponse)
	     .toList();
		
	}
	
	  @Transactional
	    public void markAsRead(Long notificationId, String email) {

	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new ResponseStatusException(
	                        HttpStatus.NOT_FOUND,
	                        "User not found"));

	        Notification notification = notificationRepository
	                .findById(notificationId)
	                .orElseThrow(() -> new ResponseStatusException(
	                        HttpStatus.NOT_FOUND,
	                        "Notification not found"));

	        // Un utilisateur ne peut modifier que ses propres notifications.
	        if (!notification.getUser().getId().equals(user.getId())) {
	            throw new ResponseStatusException(
	                    HttpStatus.FORBIDDEN,
	                    "You cannot access this notification");
	        }

	        notification.setRead(true);
	        notificationRepository.save(notification);
	    }
	
	public NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getRead(),
                notification.getCreatedAt()
        );
    }

}

package com.cee.business_match_backend.contactrequest.model;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.auth.model.User;
import com.cee.business_match_backend.offer.model.Offer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "contact_requests",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"offer_id", "sender_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role senderRole;

    @Column(nullable = false, length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactRequestStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}

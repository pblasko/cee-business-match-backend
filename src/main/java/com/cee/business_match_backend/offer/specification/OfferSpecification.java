package com.cee.business_match_backend.offer.specification;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.offer.model.Offer;
import com.cee.business_match_backend.offer.model.OfferCategory;
import com.cee.business_match_backend.offer.model.OfferStatus;
import org.springframework.data.jpa.domain.Specification;

public class OfferSpecification {

    private OfferSpecification() {
    }

    public static Specification<Offer> hasCategory(OfferCategory category) {
        return (root, query, cb) ->
                category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Offer> hasCountry(String country) {
        return (root, query, cb) ->
                (country == null || country.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("country")), "%" + country.toLowerCase() + "%");
    }

    public static Specification<Offer> hasTargetRole(Role targetRole) {
        return (root, query, cb) ->
                targetRole == null ? null : cb.equal(root.get("targetRole"), targetRole);
    }

    public static Specification<Offer> hasStatus(OfferStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Offer> hasNeedsLegalSupport(Boolean needsLegalSupport) {
        return (root, query, cb) ->
                needsLegalSupport == null ? null : cb.equal(root.get("needsLegalSupport"), needsLegalSupport);
    }

}

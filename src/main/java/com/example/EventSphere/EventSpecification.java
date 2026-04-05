package com.example.EventSphere;

import com.example.EventSphere.entity.Event;
import com.example.EventSphere.enums.EventStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class EventSpecification {
    public static Specification<Event> filterEvents(
            String location,
            Double minPrice,
            Double maxPrice,
            EventStatus status
    ){
        return (root, query, cb) -> {
            var predicate=cb.conjunction();
            if(location != null){
                predicate=cb.and(predicate,cb.equal(root.get("location"),location));
            }
            if(minPrice != null){
                predicate=cb.and(predicate,cb.greaterThanOrEqualTo(root.get("price"),minPrice));
            }
            if(maxPrice != null){
                predicate=cb.and(predicate,cb.lessThanOrEqualTo(root.get("price"),maxPrice));
            }
            if(status != null){
                predicate=cb.and(predicate,
                        cb.equal(root.get("status"), status));
            }
            return  predicate;
        };
    }
}

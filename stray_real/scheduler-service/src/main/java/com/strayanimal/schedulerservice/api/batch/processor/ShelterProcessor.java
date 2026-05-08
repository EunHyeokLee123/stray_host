package com.strayanimal.schedulerservice.api.batch.processor;

import com.strayanimal.schedulerservice.api.entity.PetShelter;
import com.strayanimal.schedulerservice.api.entity.StrayAnimalEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ShelterProcessor implements ItemProcessor<PetShelter, PetShelter> {

    @Override
    public PetShelter process(PetShelter item) throws Exception {
        return item;
    }
}

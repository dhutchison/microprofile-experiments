package com.devwithimagination.microprofile.experiments;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

import org.openapitools.api.PetApi;
import org.openapitools.model.Pet;

public class PetController implements PetApi {

    @Override
    public Response addPet(@Valid @NotNull Pet body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response addPets(@Valid @NotNull List<Pet> body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response deletePet(Long petId, String apiKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response findPetsByStatus(@NotNull List<String> status) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response findPetsByTags(@NotNull Set<String> tags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response getPetById(Long petId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response updatePet(@Valid @NotNull Pet body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response updatePetWithForm(Long petId, String name, String status) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response uploadFile(Long petId, String additionalMetadata, InputStream fileInputStream) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response uploadFileWithRequiredFile(Long petId, InputStream requiredFileInputStream,
                                               String additionalMetadata) {
        // TODO Auto-generated method stub
        return null;
    }

}

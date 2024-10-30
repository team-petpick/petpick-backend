package com.petpick.service.pet;

import com.petpick.domain.Pet;
import com.petpick.domain.User;
import com.petpick.domain.type.PetGender;
import com.petpick.domain.type.PetKind;
import com.petpick.model.PetInfoResponse;
import com.petpick.repository.PetRepository;
import com.petpick.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String regionName;

    // Create a new pet
    public PetInfoResponse createPet(Integer userId, String petName, PetKind petKind,
                                     Integer petAge, PetGender petGender, MultipartFile petImg) throws IOException {

        // Retrieve the User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String petImgUrl = null;

        if (petImg != null && !petImg.isEmpty()) {
            // Upload the image to S3
            String fileName = generateFileName(petImg);
            String s3Key = "petprofile/" + fileName;

            // Upload to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .acl(ObjectCannedACL.PUBLIC_READ) // Make the object publicly readable
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(petImg.getInputStream(), petImg.getSize()));

            // Get the URL
            petImgUrl = getFileUrl(bucketName, s3Key);
        }

        // Create a new Pet entity
        Pet pet = Pet.builder()
                .user(user)
                .petName(petName)
                .petKind(petKind)
                .petAge(petAge)
                .petGender(petGender)
                .petImg(petImgUrl)
                .build();

        Pet savedPet = petRepository.save(pet);

        return new PetInfoResponse(savedPet);
    }

    // Get pet by ID
    public PetInfoResponse getPet(Integer petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        return new PetInfoResponse(pet);
    }

    // Get all pets
    public List<PetInfoResponse> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream()
                .map(PetInfoResponse::new)
                .collect(Collectors.toList());
    }

    // Update pet
    public PetInfoResponse updatePet(Integer petId, String petName, PetKind petKind,
                                     Integer petAge, PetGender petGender, MultipartFile petImg) throws IOException {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        String newPetName = (petName != null) ? petName : pet.getPetName();
        PetKind newPetKind = (petKind != null) ? petKind : pet.getPetKind();
        Integer newPetAge = (petAge != null) ? petAge : pet.getPetAge();
        PetGender newPetGender = (petGender != null) ? petGender : pet.getPetGender();
        String petImgUrl = pet.getPetImg();

        // Delete old image from S3 if petImg is provided
        if (petImg != null && !petImg.isEmpty()) {
            if (pet.getPetImg() != null) {
                deleteImageFromS3(pet.getPetImg());
            }

            // Upload new image
            String fileName = generateFileName(petImg);
            String s3Key = "petprofile/" + fileName;

            // Upload to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(petImg.getInputStream(), petImg.getSize()));

            // Get URL
            petImgUrl = getFileUrl(bucketName, s3Key);
        }

        // Update pet
        Pet updatedPet = pet.withUpdatedFields(newPetName, newPetKind, newPetAge, newPetGender, petImgUrl);
        updatedPet = petRepository.save(updatedPet);

        return new PetInfoResponse(updatedPet);
    }

    // Delete pet
    public void deletePet(Integer petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        // Delete pet image from S3 if exists
        if (pet.getPetImg() != null) {
            deleteImageFromS3(pet.getPetImg());
        }

        petRepository.deleteById(petId);
    }

    // Update pet image only
    public PetInfoResponse updatePetImage(Integer petId, MultipartFile petImg) throws IOException {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        // Delete old image from S3 if exists
        if (pet.getPetImg() != null) {
            deleteImageFromS3(pet.getPetImg());
        }

        // Upload new image
        String fileName = generateFileName(petImg);
        String s3Key = "petprofile/" + fileName;

        // Upload to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(petImg.getInputStream(), petImg.getSize()));

        // Get URL
        String petImgUrl = getFileUrl(bucketName, s3Key);

        // Update pet with new image
        Pet updatedPet = pet.withUpdatedFields(pet.getPetName(), pet.getPetKind(), pet.getPetAge(), pet.getPetGender(), petImgUrl);
        updatedPet = petRepository.save(updatedPet);

        return new PetInfoResponse(updatedPet);
    }

    // Delete pet image only
    public void deletePetImage(Integer petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        if (pet.getPetImg() != null) {
            deleteImageFromS3(pet.getPetImg());

            // Update petImg to null
            Pet updatedPet = pet.withUpdatedFields(pet.getPetName(), pet.getPetKind(), pet.getPetAge(), pet.getPetGender(), null);
            petRepository.save(updatedPet);
        }
    }

    // Helper methods

    private void deleteImageFromS3(String imageUrl) {
        String fileName = extractFileNameFromUrl(imageUrl);
        String s3Key = "petprofile/" + fileName;

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private String generateFileName(MultipartFile multiPart) {
        return UUID.randomUUID().toString() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private String getFileUrl(String bucketName, String key) {
        Region region = Region.of(regionName);

        S3Utilities utilities = S3Utilities.builder()
                .region(region)
                .build();

        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        URL url = utilities.getUrl(request);

        return url.toString();
    }
}